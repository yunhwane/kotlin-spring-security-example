package com.example.testcode.global.filter

import com.example.testcode.global.security.CustomUserDetails
import com.example.testcode.jwt.JwtGateway
import com.example.testcode.user.core.domain.User
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter


val NOCHECK_URLS: MutableList<String> = mutableListOf("/api/v1/users/create", "/api/v1/users/login")

class JwtAuthenticationProcessingFilter(private val jwtGateway: JwtGateway): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestURI = request.requestURI

        if (requestURI in NOCHECK_URLS) {
            filterChain.doFilter(request, response)
            return
        }

        if(verifyAccessToken(request)) {
            filterChain.doFilter(request, response)
            return
        }
    }

    private fun verifyAccessToken(request: HttpServletRequest): Boolean {
        val token = jwtGateway.extractAccessToken(request)

        if(jwtGateway.verifyToken(token)) {
            val user: User = jwtGateway.getClaimToUserDomain(token)
            saveAuthentication(user)
            return true
        }

        return false
    }

    private fun saveAuthentication(user: User) {
        val userDetail = CustomUserDetails(user)
        val authentication: Authentication = UsernamePasswordAuthenticationToken(userDetail, null, userDetail.authorities)
        val context: SecurityContext = SecurityContextHolder.createEmptyContext()
        context.authentication = authentication
        SecurityContextHolder.setContext(context)
    }
}