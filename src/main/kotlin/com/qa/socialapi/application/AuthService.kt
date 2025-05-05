package com.qa.socialapi.application

import com.qa.socialapi.exception.error.InvalidRefreshTokenException
import com.qa.socialapi.exception.error.UserNotFoundException
import com.qa.socialapi.repository.UserRepository
import com.qa.socialapi.util.JwtUtil
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(private val userRepository: UserRepository, private val jwtUtil: JwtUtil) {
    val logger = KotlinLogging.logger {}

    fun logout(refreshToken: String) {
        runCatching {
            val userId = jwtUtil.getUserIdFromToken(refreshToken, isRefreshToken = true)
            val user = userRepository.findByIdOrNull(UUID.fromString(userId))
                ?: throw UserNotFoundException("$userId user not found")
            userRepository.save(user.copy(refreshToken = null))
        }.onFailure {
            logger.error { "error: $it" }
        }.getOrThrow()
    }

    fun refreshToken(refreshToken: String): String {
        val userId = jwtUtil.getUserIdFromToken(refreshToken, isRefreshToken = true)
        val user = userRepository.findByIdOrNull(UUID.fromString(userId))
            ?: throw UserNotFoundException("$userId user not found")

        if (user.refreshToken == refreshToken && !jwtUtil.validateRefreshToken(refreshToken)) {
            throw InvalidRefreshTokenException("Invalid refresh token")
        }

        return jwtUtil.generateAccessToken(user.id.toString())
    }
}