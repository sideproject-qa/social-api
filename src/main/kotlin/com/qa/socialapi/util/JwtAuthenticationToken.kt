package com.qa.socialapi.util

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    private val userId: String,
    private val token: String,
    authorities: Collection<GrantedAuthority> = emptyList()
): AbstractAuthenticationToken(authorities) {

    init {
        isAuthenticated = true
    }

    override fun getCredentials(): Any = token

    override fun getPrincipal(): Any = userId
}