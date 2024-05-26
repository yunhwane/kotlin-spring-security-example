package com.example.testcode.global.security

import com.example.testcode.user.core.domain.User
import com.example.testcode.user.core.port.UserJpaPort
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class CustomUserDetailsService(private val userJpaPort: UserJpaPort): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userJpaPort.findByEmail(username) ?: throw UsernameNotFoundException("No user found with email: $username")
        return CustomUserDetails(user)
    }

}