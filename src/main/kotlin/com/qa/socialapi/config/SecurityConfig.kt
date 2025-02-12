package com.qa.socialapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll()
            }
            .csrf { it.disable() }  // CSRF 보호 비활성화
            .formLogin { it.disable() }  // 로그인 폼 비활성화
            .httpBasic { it.disable() }  // 기본 인증 비활성화

        return http.build()
    }
}
