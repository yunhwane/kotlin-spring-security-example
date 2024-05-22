package com.example.testcode.user.application.api

data class UserRequest(
    val email: String,
    val password: String,
    val name: String,
)