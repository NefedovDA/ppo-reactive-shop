package ru.diamant.rabbit.reactiveShop.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.diamant.rabbit.reactiveShop.domain.User
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


    fun createToken(user: User): String {
        val username = user.login
        val claims = Jwts.claims().setSubject(username)
        val issuedAt = Date()
        val expiration = Date(issuedAt.time + jwtValidity)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .signWith(this.secretKey, SignatureAlgorithm.HS256)
            .compact()
    }


    fun getSubject(authToken: String?): String? =
        getClaimsFromToken(authToken)?.subject

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
        private val LOGGER = LoggerFactory.getLogger(JwtUtils::class.java)
    }
}