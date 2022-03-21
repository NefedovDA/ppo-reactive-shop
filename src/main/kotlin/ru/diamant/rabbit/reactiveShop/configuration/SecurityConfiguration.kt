package ru.diamant.rabbit.reactiveShop.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import reactor.core.publisher.Mono
import ru.diamant.rabbit.reactiveShop.domain.Authority
import ru.diamant.rabbit.reactiveShop.repository.UserRepository
import ru.diamant.rabbit.reactiveShop.security.SecurityMode
import ru.diamant.rabbit.reactiveShop.security.UserDetails
import ru.diamant.rabbit.reactiveShop.security.jwt.JwtTokenAuthenticationFilter


@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration {
    @Bean
    fun securityWebFilterChain(
        @Value("\${reactive.shop.security.mode}") securityMode: SecurityMode,

        httpSecurity: ServerHttpSecurity,
        reactiveAuthenticationManager: ReactiveAuthenticationManager,
        jwtTokenAuthenticationFilter: JwtTokenAuthenticationFilter
    ): SecurityWebFilterChain =
        httpSecurity
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)

            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .logout(ServerHttpSecurity.LogoutSpec::disable)

            .authenticationManager(reactiveAuthenticationManager)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

            .exceptionHandling { spec ->
                spec
                    .authenticationEntryPoint { swe, _ ->
                        Mono.fromRunnable { swe.response.statusCode = HttpStatus.UNAUTHORIZED }
                    }
                    .accessDeniedHandler { swe, _ ->
                        Mono.fromRunnable { swe.response.statusCode = HttpStatus.FORBIDDEN }
                    }
            }

            .authorizeExchange { spec ->
                spec
                    .pathMatchers("/favicon.ico").permitAll()

                    .pathMatchers(HttpMethod.POST, "/api/v1/auth/create").permitAll()
                    .pathMatchers(HttpMethod.POST, "/api/v1/auth/token").permitAll()

                    .pathMatchers(HttpMethod.GET, "/api/v1/users").permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/v1/users/*").permitAll()
                    .pathMatchers(HttpMethod.POST, "/api/v1/users/*/role").hasAuthority(Authority.OWNER)

                    .pathMatchers(HttpMethod.GET, "/api/v1/products").permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/v1/products/*").permitAll()
                    .pathMatchers(HttpMethod.POST, "/api/v1/products").hasAuthority(Authority.USER)
                    .pathMatchers(HttpMethod.PUT, "/api/v1/products/*").hasAuthority(Authority.USER)
                    .pathMatchers(HttpMethod.DELETE, "/api/v1/products/*").hasAuthority(Authority.USER)

                    .anyExchange().let { access ->
                        when (securityMode) {
                            SecurityMode.EXPERIMENT -> access.permitAll()
                            SecurityMode.NORMAL -> access.denyAll()
                        }
                    }
            }

            .addFilterAt(jwtTokenAuthenticationFilter, SecurityWebFiltersOrder.HTTP_BASIC)

            .build()


    @Bean
    fun userDetailsService(userRepository: UserRepository): ReactiveUserDetailsService =
        ReactiveUserDetailsService { email ->
            userRepository.findByLogin(email).map(::UserDetails)
        }

    @Bean
    @Suppress("DEPRECATION")
    fun passwordEncoder(): PasswordEncoder =
        NoOpPasswordEncoder.getInstance()

    @Bean
    fun reactiveAuthenticationManager(
        userDetailsService: ReactiveUserDetailsService,
        passwordEncoder: PasswordEncoder
    ): ReactiveAuthenticationManager =
        UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService)
            .apply {
                setPasswordEncoder(passwordEncoder)
            }
}