package com.example.testcode.user.core.port

import com.example.testcode.user.application.api.UserRequest
import com.example.testcode.user.application.api.UserSaveResponse
import com.example.testcode.user.core.domain.User

interface UserEndpointPort {
    fun saveUser(request: UserRequest): UserSaveResponse
}