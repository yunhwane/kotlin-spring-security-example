package com.example.testcode.user.application.mapper

import com.example.testcode.user.application.api.UserRequest
import com.example.testcode.user.application.api.UserSaveResponse
import com.example.testcode.user.core.domain.User
import com.example.testcode.user.infrastucture.persistence.UserModel

object UserMapper {
    fun toUser(userRequest: UserRequest): User {
        return User(null, userRequest.email, userRequest.password, userRequest.name)
    }

    fun toUserModelFromUser(user: User): UserModel {
        return UserModel(user.email, user.password, user.name)
    }

    fun toUserFromUserModel(userModel: UserModel): User {
        return User(userModel.id, userModel.email, userModel.password, userModel.name)
    }

    fun toUserSaveResponse(user: User): UserSaveResponse {
         return UserSaveResponse(user.id!!, user.email, user.name)
    }
}