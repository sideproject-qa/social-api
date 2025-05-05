package com.qa.socialapi.dto.app

import com.qa.socialapi.repository.AppEntity
import java.time.LocalDateTime
import java.util.*

object CreateAppDto {

    data class CreateAppRequest(
        val name: String,
        val description: String?,
        val icon: String?,
    )

    data class CreateAppResponse(
        val id: UUID,
        val name: String,
        val description: String?,
        val icon: String?,
        val createdAt: LocalDateTime? = LocalDateTime.now(),
        val updatedAt: LocalDateTime? = LocalDateTime.now()
    ) {
        companion object {
            fun AppEntity.toCreateAppResponse(): CreateAppResponse {
                return CreateAppResponse(
                    id = id,
                    name = name,
                    description = description,
                    icon = icon,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }
        }
    }
}