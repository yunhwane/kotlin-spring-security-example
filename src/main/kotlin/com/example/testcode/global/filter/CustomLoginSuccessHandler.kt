package com.example.testcode.global.filter

import com.example.testcode.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler

class CustomLoginSuccessHandler : SimpleUrlAuthenticationSuccessHandler(){
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

        val username = authentication.name
        logger().info("UserEntity $username has logged in")
        response.status = HttpServletResponse.SC_OK
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write("{\"message\": \"Login success\"}")
        response.writer.flush()
        response.writer.close()
    }

}