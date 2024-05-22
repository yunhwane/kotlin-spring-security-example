package com.example.testcode.user.adapter.endpoint

import com.example.testcode.global.annotation.Adapter
import com.example.testcode.user.application.api.UserRequest
import com.example.testcode.user.application.api.UserSaveResponse
import com.example.testcode.user.application.mapper.UserMapper.toUser
import com.example.testcode.user.application.mapper.UserMapper.toUserSaveResponse
import com.example.testcode.user.core.port.UserEndpointPort
import com.example.testcode.user.core.usecase.UserSaveUseCase


@Adapter
class UserEndPointAdapter(private val userSaveUseCase: UserSaveUseCase): UserEndpointPort {
    override fun saveUser(request: UserRequest): UserSaveResponse {
        val user = toUser(request)
        val saveUser = userSaveUseCase.execute(user)
        return toUserSaveResponse(saveUser)
    }
}