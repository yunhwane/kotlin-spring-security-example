package com.example.testcode.user.application.service

import com.example.testcode.user.core.port.UserJpaPort
import com.example.testcode.user.core.usecase.UserSaveUseCase
import com.example.testcode.user.core.domain.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userJpaPort: UserJpaPort): UserSaveUseCase {

    @Transactional
    override fun execute(user: User): User {
        userJpaPort.findByEmail(user.email)?.let {
            throw IllegalArgumentException("이미 존재하는 이메일입니다.")
        }
        return userJpaPort.save(user)
    }
}