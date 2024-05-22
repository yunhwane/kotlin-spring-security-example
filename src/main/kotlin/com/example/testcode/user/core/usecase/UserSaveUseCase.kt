package com.example.testcode.user.core.usecase

import com.example.testcode.user.core.domain.User

interface UserSaveUseCase {
    fun execute(user: User) : User
}