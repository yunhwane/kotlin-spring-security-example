package com.example.testcode.global.filter


import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import java.util.*


const val DEFAULT_LOGIN_REQUEST_URL = "/api/v1/login"
const val AUTHORIZATION_HEADER = "Authorization"
const val BASIC_AUTHORIZATION_PREFIX = "Basic "

fun decodeBase64(encodedString: String): ByteArray {
    return Base64.getDecoder().decode(encodedString)
}


class CustomUsernamePasswordAuthenticationFilter: AbstractAuthenticationProcessingFilter(DEFAULT_LOGIN_REQUEST_URL) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val decodedCredentials: String = decodingUsernamePasswordByBasicToken(request)
        val credentials = decodedCredentials.split(":").dropLastWhile { it.isEmpty() }.toTypedArray()

        require (credentials.size == 2) {
            "Invalid Basic authentication token"
        }

        val email = credentials[0]
        val password = credentials[1]
        val authenticationToken = UsernamePasswordAuthenticationToken(email, password)

        return authenticationManager.authenticate(authenticationToken)
    }
    private fun decodingUsernamePasswordByBasicToken(request: HttpServletRequest): String {
        val encodedMime = request.getHeader(AUTHORIZATION_HEADER)
            ?: throw IllegalArgumentException("No Authorization header provided")

        require (encodedMime.startsWith(BASIC_AUTHORIZATION_PREFIX)) { "Invalid Authorization header format" }

        val base64Credentials = encodedMime.substring(6)
        val decodedBytes: ByteArray = decodeBase64(base64Credentials)
        return String(decodedBytes)
    }

}