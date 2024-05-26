package com.example.testcode.global.handler

import com.example.testcode.jwt.JwtGateway
import com.example.testcode.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler

class CustomLoginSuccessHandler(private val jwtGateway: JwtGateway) : SimpleUrlAuthenticationSuccessHandler(){
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val username = authentication.name
        logger().info("UserEntity $username has logged in")

        val token = jwtGateway.createToken(username)
        jwtGateway.setHeaderAccessToken(response, token)

        response.status = HttpServletResponse.SC_OK
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write("{\"message\": \"Login success\"}")
        response.writer.flush()
        response.writer.close()
    }

}