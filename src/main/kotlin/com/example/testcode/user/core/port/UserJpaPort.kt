package com.example.testcode.user.core.port

import com.example.testcode.user.core.domain.User

interface UserJpaPort {
    fun save(user: User): User
    fun findByEmail(email: String): User?
    fun findById(id: Long): User?
}