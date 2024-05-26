package com.example.testcode.global.security

import com.example.testcode.user.core.domain.User
import com.example.testcode.user.infrastucture.persistence.UserModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val user: User): UserDetails {

    private val authorities: Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))
    override fun getAuthorities(): Collection<GrantedAuthority> = authorities
    override fun getPassword(): String = user.password
    override fun getUsername(): String = user.email

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

}