package ru.diamant.rabbit.reactiveShop.security.jwt

import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


@Component
class JwtTokenAuthenticationFilter(
    private val jwtUtils: JwtUtils
) : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val authentication =
            jwtUtils.getAuthentication(resolveToken(exchange.request))
                ?: return chain.filter(exchange)

        return chain
            .filter(exchange)
            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
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