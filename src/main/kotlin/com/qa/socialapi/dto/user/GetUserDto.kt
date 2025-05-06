package com.qa.socialapi.dto.user

import com.qa.socialapi.enum.Platform
import com.qa.socialapi.repository.UserEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.UUID


object GetUserDto {

    data class GetUserResponse(
        val id: UUID,
        val platformId: String,
        val platform: Platform,
        val name: String?,
        val email: String?,
        val nickname: String?,
        val ticket: Int,
        val goalPoint: Int,
        val currentPoint: Int,
        val createdAt: LocalDateTime? = LocalDateTime.now(),
        var updatedAt: LocalDateTime? = LocalDateTime.now()
    ) {
        companion object {
            fun UserEntity.toGetUserResponse(): GetUserResponse {
                return GetUserResponse(
                    id = id,
                    platform = platform,
                    platformId = platformId,
                    name = name,
                    email = email,
                    nickname = nickname,
                    ticket = ticket,
                    goalPoint = goalPoint,
                    currentPoint = currentPoint,
                    createdAt = createdAt,
                    updatedAt = updatedAt
                )
            }
        }
    }

}