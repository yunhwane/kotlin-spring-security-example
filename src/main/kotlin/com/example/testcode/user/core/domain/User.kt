package com.example.testcode.user.core.domain

import org.springframework.security.crypto.password.PasswordEncoder

data class User(
    val id: Long?,
    val email: String,
    var password: String,
    val name: String,
)
{
    fun encodePassword(passwordEncoder: PasswordEncoder) {
        password = passwordEncoder.encode(password)
    }

    fun matchPassword(passwordEncoder: PasswordEncoder, rawPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, password)
    }

}