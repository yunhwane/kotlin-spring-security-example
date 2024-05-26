package com.example.testcode.user.application.api

import com.example.testcode.user.core.port.UserEndpointPort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI


@RestController
@RequestMapping("/api/v1")
class UserController(private val userEndpointPort: UserEndpointPort) {

    @PostMapping("/users/create")
    fun addUser(@RequestBody request: UserRequest): ResponseEntity<UserSaveResponse> {
        val userSaveResponse = userEndpointPort.saveUser(request)
        val createURI = URI("/api/v1/users/${userSaveResponse.id}")
        return ResponseEntity.created(createURI).body(userSaveResponse)
    }

    @GetMapping("/users/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserInfoResponse> {
        val userInfoResponse = userEndpointPort.loadUserById(id)
        return ResponseEntity.ok(userInfoResponse)
    }
}