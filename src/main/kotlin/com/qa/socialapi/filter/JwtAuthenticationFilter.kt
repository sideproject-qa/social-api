package com.qa.socialapi.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.util.JwtAuthenticationToken
import com.qa.socialapi.util.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header.isNullOrEmpty() || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = header.substring(7)
        if (jwtUtil.validateAccessToken(token)) {
            val userId = jwtUtil.getUserIdFromToken(token, isRefreshToken = false)
            if (!userId.isNullOrEmpty()) {
                val authentication = JwtAuthenticationToken(userId, token)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } else {
            val error = ResponseWrapper<Any>(
                code = HttpStatus.UNAUTHORIZED.value(),
                message = "Invalid access token",
                data = Unit
            )

            response.characterEncoding = "UTF-8"
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.writer.write(objectMapper.writeValueAsString(error))
            return
        }

        filterChain.doFilter(request, response)
    }
}