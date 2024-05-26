package com.example.testcode.user.core.usecase

import com.example.testcode.user.core.domain.User

interface LoadUserByIdUseCase {
    fun execute(id: Long): User
}