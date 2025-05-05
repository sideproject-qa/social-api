package com.qa.socialapi.application

import com.qa.socialapi.dto.user.UpdateUserDto
import com.qa.socialapi.exception.error.InvalidAccessTokenException
import com.qa.socialapi.exception.error.UserNotFoundException
import com.qa.socialapi.repository.UserEntity
import com.qa.socialapi.repository.UserRepository
import com.qa.socialapi.util.JwtUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil,
) {
    fun save(user: UserEntity): UserEntity {
        return userRepository.save(user)
    }

    fun findById(accessToken: String): UserEntity {
        val id = UUID.fromString(jwtUtil.getUserIdFromToken(accessToken))

        return userRepository.findByIdOrNull(id)
            ?: throw UserNotFoundException("$id user not found")
    }

    fun deleteById(accessToken: String) {
        val id = UUID.fromString(jwtUtil.getUserIdFromToken(accessToken))
        userRepository.findByIdOrNull(id) ?: throw UserNotFoundException("$id user not found")
        userRepository.deleteById(id)
    }

    fun findByPlatformId(providerId: String): UserEntity? {
        return userRepository.findByPlatformId(providerId)
    }

    fun update(accessToken: String, dto: UpdateUserDto.UpdateUserRequest): UserEntity {
        val id = UUID.fromString(jwtUtil.getUserIdFromToken(accessToken))
        val user = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException("$id user not found")
        return userRepository.save(user.update(dto))
    }
}