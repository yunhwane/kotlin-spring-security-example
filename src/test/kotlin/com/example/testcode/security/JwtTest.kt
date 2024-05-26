package com.example.testcode.security

import com.example.testcode.jwt.JwtGateway
import com.example.testcode.security.MockJwtObject.createExpiredToken
import com.example.testcode.security.MockJwtObject.wrongSignatureToken
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor


@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
class JwtTest(val jwtGateway: JwtGateway): FunSpec({

    test("jwt 생성 테스트") {
        val email = "email1@example.com"
        val token = jwtGateway.createToken(email)

        token shouldNotBe null

        val claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor("secret".repeat(16).toByteArray()))
            .build()
            .parseClaimsJws(token)
            .body

        claims["email"] shouldBe email
    }

    test("jwt 검증 테스트 - 성공") {
        val email = "email1@example.com"
        val token = jwtGateway.createToken(email)
        val result = jwtGateway.verifyToken(token)
        result shouldBe true
    }

    test("jwt 검증 테스트 - 실패 - 만료된 토큰") {
        val email = "email1@example.com"
        val token = createExpiredToken(email)
        val result = jwtGateway.verifyToken(token)
        result shouldBe false
    }

    test("jwt 검증 테스트 - 실패 - 잘못된 토큰") {
        val token = "wrong token"
        val result = jwtGateway.verifyToken(token)
        result shouldBe false
    }

    test("jwt 검증 테스트 - 실패 - 잘못된 서명") {
        val email = "email1@example.com"
        val token = wrongSignatureToken(email)
        val result = jwtGateway.verifyToken(token)
        result shouldBe false
    }
})