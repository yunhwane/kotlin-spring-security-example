package com.example.testcode.jwt

import com.example.testcode.logger
import com.example.testcode.user.core.domain.User
import com.example.testcode.user.core.port.UserJpaPort
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import javax.crypto.SecretKey
import java.util.Date


const val SECRET_KEY = "secret"
const val EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 10
const val TOKEN_PREFIX = "Bearer "
const val HEADER_STRING = "Authorization"
const val EMAIL_CLAIM = "email"
@Service
class JwtProvider(private val userJpaPort: UserJpaPort): JwtGateway {

    private val secretKey: SecretKey = Keys.hmacShaKeyFor("secret".repeat(16).toByteArray())

    override fun createToken(email: String): String {
        val now = Date()
        val verifyDate = Date(now.time + EXPIRATION_TIME)

        return Jwts.builder()
            .setClaims(mapOf(EMAIL_CLAIM to email))
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .setIssuedAt(now)
            .setExpiration(verifyDate)
            .compact()
    }

    override fun verifyToken(token: String): Boolean {

        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
            return true

        } catch (e: Exception) {
            when (e) {
                is ExpiredJwtException -> logger().error("Token expired: ${e.message}")
                is SignatureException -> logger().error("Invalid JWT signature: ${e.message}")
                is UnsupportedJwtException -> logger().error("Unsupported JWT token: ${e.message}")
                is IllegalArgumentException -> logger().error("JWT claims string is empty or only whitespace: ${e.message}")
                else -> logger().error("Unknown JWT processing error: ${e.message}")
            }
            return false
        }
    }

    override fun getClaimToUserDomain(token: String): User {
        val claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body

        val email = claims[EMAIL_CLAIM] as String

        return userJpaPort.findByEmail(email) ?: throw IllegalArgumentException("User not found")
    }

    override fun extractAccessToken(httpServletRequest: HttpServletRequest): String {
        return httpServletRequest.getHeader(HEADER_STRING)?.removePrefix(TOKEN_PREFIX) ?: ""
    }

    override fun setHeaderAccessToken(httpServletResponse: HttpServletResponse, token: String) {
        httpServletResponse.addHeader(HEADER_STRING, "$TOKEN_PREFIX$token")
    }
}