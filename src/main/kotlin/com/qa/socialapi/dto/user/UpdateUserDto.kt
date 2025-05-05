package com.qa.socialapi.dto.user

import com.qa.socialapi.repository.UserEntity
import java.time.LocalDateTime
import java.util.*

object UpdateUserDto {

    data class UpdateUserRequest(
        val name: String? = null,
        val email: String? = null,
        val nickname: String? = null,
        val ticket: Int? = null,
        val goalPoint: Int? = null,
        val currentPoint: Int? = null,
    )

    data class UpdateUserResponse(
        val id: UUID,
        val platformId: String,
        val platform: String,
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
            fun UserEntity.toUpdateUserResponse(): UpdateUserResponse {
                return UpdateUserResponse(
                    id = this.id,
                    platformId = this.platformId,
                    platform = this.platform,
                    name = this.name,
                    email = this.email,
                    nickname = this.nickname,
                    ticket = this.ticket,
                    goalPoint = this.goalPoint,
                    currentPoint = this.currentPoint,
                    createdAt = this.createdAt,
                    updatedAt = this.updatedAt,
                )
            }
        }
    }
}