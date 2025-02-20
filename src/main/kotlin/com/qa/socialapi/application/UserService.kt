package com.qa.socialapi.application

import com.qa.socialapi.repository.UserEntity
import com.qa.socialapi.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun saveUser(
        provider: String,
        providerId: String,
        name: String? = null,
        email: String? = null,
        nickname: String? = null
    ): UserEntity {
        return userRepository.save(
            UserEntity(
                provider = provider, providerId = providerId, name = name, email = email
            )
        )
    }

    fun findById(id: UUID): UserEntity? {
        return userRepository.findByIdOrNull(id)
    }

    fun findByProviderId(providerId: String): UserEntity? {
        return userRepository.findByProviderId(providerId)
    }
}