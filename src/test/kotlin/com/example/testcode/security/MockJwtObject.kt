package com.example.testcode.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey

object MockJwtObject {

    fun createExpiredToken(email: String): String {
        val now = Date()
        val expiredDate = Date(now.time - 1000 * 60)
        val secretKey: SecretKey = Keys.hmacShaKeyFor("secret".repeat(16).toByteArray())

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiredDate) // 과거의 만료 시간 설정
            .signWith(secretKey, SignatureAlgorithm.HS512)
            .compact()
    }

    fun wrongSignatureToken(email: String): String {
        val now = Date()
        val expiredDate = Date(now.time + 1000 * 60 * 60)
        val incorrectSecretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(now)
            .setExpiration(expiredDate)
            .signWith(incorrectSecretKey, SignatureAlgorithm.HS256)
            .compact()

    }
}