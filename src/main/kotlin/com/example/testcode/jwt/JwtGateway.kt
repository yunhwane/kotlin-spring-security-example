package com.example.testcode.jwt

import com.example.testcode.user.core.domain.User
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface JwtGateway {
    fun createToken(email: String): String
    fun verifyToken(token: String): Boolean
    fun getClaimToUserDomain(token: String): User
    fun extractAccessToken(httpServletRequest: HttpServletRequest): String
    fun setHeaderAccessToken(httpServletResponse: HttpServletResponse, token: String)
}
