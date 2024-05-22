package com.example.testcode.user.adapter.persistence

import com.example.testcode.global.annotation.Adapter
import com.example.testcode.user.application.mapper.UserMapper
import com.example.testcode.user.core.port.UserJpaPort
import com.example.testcode.user.core.domain.User
import com.example.testcode.user.infrastucture.repository.UserJpaRepository


@Adapter
class UserJpaAdapter(private val userJpaRepository: UserJpaRepository): UserJpaPort {
    override fun save(user: User): User {
        val userModel = UserMapper.toUserModelFromUser(user)
        userJpaRepository.save(userModel)
        return UserMapper.toUserFromUserModel(userModel)
    }

    override fun findByEmail(email: String): User? {
        val userModel = userJpaRepository.findByEmail(email)
        return userModel?.let { UserMapper.toUserFromUserModel(it) }
    }
}