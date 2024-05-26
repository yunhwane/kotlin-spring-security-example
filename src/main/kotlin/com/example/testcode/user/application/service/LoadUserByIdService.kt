package com.example.testcode.user.application.service

import com.example.testcode.user.core.domain.User
import com.example.testcode.user.core.port.UserJpaPort
import com.example.testcode.user.core.usecase.LoadUserByIdUseCase
import org.springframework.stereotype.Service


@Service
class LoadUserByIdService(private val userJpaPort: UserJpaPort) : LoadUserByIdUseCase{
    override fun execute(id: Long): User {
        return notFoundCheckId(id)
    }

    fun notFoundCheckId(id: Long): User {
        return userJpaPort.findById(id) ?: throw IllegalArgumentException("존재하지 않는 회원입니다.")
    }
}