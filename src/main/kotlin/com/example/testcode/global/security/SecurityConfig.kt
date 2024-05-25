package com.example.testcode.global.security

import com.example.testcode.global.filter.CustomLoginFailHandler
import com.example.testcode.global.filter.CustomLoginSuccessHandler
import com.example.testcode.global.filter.CustomUserDetailsService
import com.example.testcode.global.filter.CustomUsernamePasswordAuthenticationFilter
import com.example.testcode.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import jakarta.servlet.Filter
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.SecurityContextHolderFilter

@Configuration
class SecurityConfig(private val customUserDetailsService: CustomUserDetailsService) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf {
            it.disable()
        }
        http.authorizeHttpRequests {
            it.anyRequest().permitAll()
        }

        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        http.formLogin {
            it.disable()
        }

        http.httpBasic {
            it.disable()
        }

        http.addFilterBefore(LoggingFilter(), SecurityContextHolderFilter::class.java)
        http.addFilterBefore(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun bcryptPasswordEncoder() : PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun customUsernamePasswordAuthenticationFilter() : CustomUsernamePasswordAuthenticationFilter {
        CustomUsernamePasswordAuthenticationFilter().apply {
            setAuthenticationSuccessHandler(CustomLoginSuccessHandler())
            setAuthenticationFailureHandler(customLoginFailHandler())
            setAuthenticationManager(authenticationManager())
            return this
        }
    }

    @Bean
    fun authenticationManager() : AuthenticationManager {
        DaoAuthenticationProvider().apply {
            setPasswordEncoder(bcryptPasswordEncoder())
            setUserDetailsService(customUserDetailsService)
            return ProviderManager(this)
        }
    }

    @Bean
    fun customLoginFailHandler() : CustomLoginFailHandler {
        return CustomLoginFailHandler()
    }

    @Bean
    fun customLoginSuccessHandler() : CustomLoginSuccessHandler {
        return CustomLoginSuccessHandler()
    }


    class LoggingFilter : Filter {

        override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
            val req = request as HttpServletRequest
            val res = response as HttpServletResponse

            val authentication = SecurityContextHolder.getContext().authentication
            val userInfo = if (authentication != null && authentication.isAuthenticated) {
                "User: ${authentication.name}, Roles: ${authentication.authorities.joinToString(", ")}"
            } else {
                "Anonymous User"
            }

            val path = req.requestURI
            val method = req.method
            val startTime = System.currentTimeMillis()

            try {
                chain?.doFilter(request, response)
                val endTime = System.currentTimeMillis()
                val duration = endTime - startTime
                logger().info("[$method] $path ($duration ms), $userInfo")
            } catch (e: Exception) {
                val endTime = System.currentTimeMillis()
                val duration = endTime - startTime
                logger().error("[$method] $path ($duration ms), $userInfo - Failed with exception: ${e.message}", e)
            }
        }
    }
}