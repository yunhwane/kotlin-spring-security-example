package com.example.testcode.user.application.service

import com.example.testcode.user.core.port.UserJpaPort
import com.example.testcode.user.core.usecase.UserSaveUseCase
import com.example.testcode.user.core.domain.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserSaveService(private val userJpaPort: UserJpaPort, private val passwordEncoder: PasswordEncoder): UserSaveUseCase {

    @Transactional
    override fun execute(user: User): User {
        duplicateEmailCheck(user)
        user.encodePassword(passwordEncoder)
        return userJpaPort.save(user)
    }

    private fun duplicateEmailCheck(user: User) {
        userJpaPort.findByEmail(user.email)?.let {
            throw IllegalArgumentException("이미 존재하는 이메일입니다.")
        }
    }
}
