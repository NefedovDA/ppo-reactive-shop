package ru.diamant.rabbit.reactiveShop.security.jwt

import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import ru.diamant.rabbit.reactiveShop.repository.UserRepository
import ru.diamant.rabbit.reactiveShop.security.UserDetails


@Component
class JwtTokenAuthenticationFilter(
    private val jwtUtils: JwtUtils,
    private val userRepository: UserRepository,
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authToken = resolveToken(exchange.request)
        val subject =
            jwtUtils.getSubject(authToken)
                ?: return chain.filter(exchange)

        val securityContext =
            userRepository
                .findByLogin(subject)
                .map {
                    val userDetails = UserDetails(it)
                    SecurityContextImpl(
                        UsernamePasswordAuthenticationToken(
                            userDetails,
                            authToken,
                            userDetails.authorities
                        )
                    )
                }

        return chain
            .filter(exchange)
            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(securityContext))
    }

    private fun resolveToken(request: ServerHttpRequest): String? {
        val bearerToken: String = request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return null

        if (bearerToken.startsWith(HEADER_PREFIX).not()) return null

        return bearerToken.substring(7)
    }

    companion object {
        private const val HEADER_PREFIX = "Bearer "
    }
}