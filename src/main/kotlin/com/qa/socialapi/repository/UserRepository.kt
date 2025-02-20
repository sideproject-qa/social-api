package com.qa.socialapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<UserEntity, UUID> {

    fun findByProviderId(providerId: String): UserEntity?
}