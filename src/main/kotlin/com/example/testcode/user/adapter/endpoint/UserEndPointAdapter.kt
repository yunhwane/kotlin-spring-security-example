package com.example.testcode.user.adapter.endpoint

import com.example.testcode.global.annotation.Adapter
import com.example.testcode.user.application.api.UserInfoResponse
import com.example.testcode.user.application.api.UserRequest
import com.example.testcode.user.application.api.UserSaveResponse
import com.example.testcode.user.application.mapper.UserMapper.toUser
import com.example.testcode.user.application.mapper.UserMapper.toUserInfoResponse
import com.example.testcode.user.application.mapper.UserMapper.toUserSaveResponse
import com.example.testcode.user.core.port.UserEndpointPort
import com.example.testcode.user.core.usecase.LoadUserByIdUseCase
import com.example.testcode.user.core.usecase.UserSaveUseCase


@Adapter
class UserEndPointAdapter(
    private val userSaveUseCase: UserSaveUseCase,
    private val userByIdUseCase: LoadUserByIdUseCase
): UserEndpointPort {
    override fun saveUser(request: UserRequest): UserSaveResponse {
        val user = toUser(request)
        val saveUser = userSaveUseCase.execute(user)
        return toUserSaveResponse(saveUser)
    }

    override fun loadUserById(id: Long): UserInfoResponse {
        val user =  userByIdUseCase.execute(id)
        return toUserInfoResponse(user)
    }
}