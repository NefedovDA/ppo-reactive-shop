package ru.diamant.rabbit.reactiveShop.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import ru.diamant.rabbit.reactiveShop.domain.Authority

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration(
    @Value("\${reactive.shop.security.mode}")
    private val securityMode: SecurityMode
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()

    @Bean
    fun securityWebFilterChain(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain =
        httpSecurity
            .csrf().disable()
            .formLogin().disable()
            .logout().disable()
            .httpBasic().disable()
            .authorizeExchange().let { spec ->
                spec
                    .pathMatchers("/favicon.ico").permitAll()

                    .pathMatchers(HttpMethod.GET, "/me").hasAuthority(Authority.USER)
                    .pathMatchers(HttpMethod.PUT, "/me/currency").hasAuthority(Authority.USER)

                    .pathMatchers(HttpMethod.GET, "/products").hasAuthority(Authority.USER)
                    .pathMatchers(HttpMethod.GET, "/products/*").hasAuthority(Authority.USER)
                    .pathMatchers(HttpMethod.POST, "/products").hasAuthority(Authority.ADMIN)
                    .pathMatchers(HttpMethod.PUT, "/products/*/amount").hasAuthority(Authority.ADMIN)

                    .pathMatchers(HttpMethod.POST, "/auth/register").permitAll()
                    .pathMatchers(HttpMethod.POST, "/auth/token").permitAll()

                    .pathMatchers(HttpMethod.GET, "/users").hasAuthority(Authority.ADMIN)
                    .pathMatchers(HttpMethod.GET, "/users/*").hasAuthority(Authority.ADMIN)
                    .pathMatchers(HttpMethod.PUT, "/users/*/role").hasAuthority(Authority.OWNER)

                    .anyExchange().let { access ->
                        when (securityMode) {
                            SecurityMode.EXPERIMENT -> access.permitAll()
                            SecurityMode.NORMAL -> access.denyAll()
                        }
                    }

                    .and()
            }
            .build()
}