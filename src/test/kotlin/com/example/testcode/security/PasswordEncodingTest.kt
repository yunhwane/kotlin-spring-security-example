package com.example.testcode.security

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.TestConstructor


@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
class PasswordEncodingTest(private val passwordEncoder: PasswordEncoder): FunSpec({

    test("비밀번호 암호화 테스트") {
        val rawPassword = "password"
        val encodedPassword = passwordEncoder.encode(rawPassword)
        val isMatched = passwordEncoder.matches(rawPassword, encodedPassword)
        isMatched shouldBe true
    }
})
