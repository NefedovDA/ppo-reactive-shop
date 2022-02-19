package ru.diamant.rabbit.reactiveShop.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.stereotype.Component
import ru.diamant.rabbit.reactiveShop.security.SecurityUser
import java.security.SignatureException
import java.util.*
import javax.crypto.SecretKey


@Component
class JwtUtils(
    @Value("\${reactive.shop.security.jwt.secret}")
    jwtSecret: String,

    @Value("\${reactive.shop.security.jwt.validity.sec}")
    jwtValiditySec: Long
) {
    private val secretKey: SecretKey =
        Keys.hmacShaKeyFor(
            Base64.getEncoder()
                .encodeToString(jwtSecret.encodeToByteArray())
                .encodeToByteArray()
        )

    private val jwtValidity: Long = jwtValiditySec * 1000L


    fun createToken(authentication: Authentication): String? {
        val username = authentication.name
        val authorities = authentication.authorities
        val claims = Jwts.claims().setSubject(username)
        if (authorities.isNotEmpty()) {
            claims[AUTHORITIES_KEY] = authorities.joinToString(",") { it.authority }
        }
        val issuedAt = Date()
        val expiration = Date(issuedAt.time + jwtValidity)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .signWith(this.secretKey, SignatureAlgorithm.HS256)
            .compact()
    }


    fun getAuthentication(authToken: String?): Authentication? {
        val claims = getClaimsFromToken(authToken) ?: return null

        val authorities =
            claims[AUTHORITIES_KEY]
                ?.let { AuthorityUtils.commaSeparatedStringToAuthorityList(it.toString()) }
                ?: AuthorityUtils.NO_AUTHORITIES

        val principal = SecurityUser(claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, authToken, authorities)
    }

    private fun getClaimsFromToken(authToken: String?): Claims? {
        try {
            return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(authToken)
                .body
        }
        catch (e: SignatureException) {
            LOGGER.error("Invalid JWT signature: {}", e.message)
        }
        catch (e: MalformedJwtException) {
            LOGGER.error("Invalid JWT token: {}", e.message)
        }
        catch (e: ExpiredJwtException) {
            LOGGER.error("JWT token is expired: {}", e.message)
        }
        catch (e: UnsupportedJwtException) {
            LOGGER.error("JWT token is unsupported: {}", e.message)
        }
        catch (e: IllegalArgumentException) {
            LOGGER.error("JWT claims string is empty: {}", e.message)
        }
        return null
    }

    companion object {
        private const val AUTHORITIES_KEY = "roles"

        private val LOGGER = LoggerFactory.getLogger(JwtUtils::class.java)
    }
}