package com.example.testcode.user.application.api

import com.example.testcode.user.core.port.UserEndpointPort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UserController(private val userEndpointPort: UserEndpointPort) {

    @PostMapping("/user")
    fun addUser(request: UserRequest): ResponseEntity<UserSaveResponse> {
        return ResponseEntity.ok(userEndpointPort.saveUser(request))
    }

}