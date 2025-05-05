package com.qa.socialapi.dto.app

import com.qa.socialapi.repository.AppEntity
import java.time.LocalDateTime
import java.util.*

object GetAppDto {

    data class GetAppResponse(
        val id: UUID,
        val name: String,
        val description: String?,
        val icon: String?,
        val createdAt: LocalDateTime? = LocalDateTime.now(),
        val updatedAt: LocalDateTime? = LocalDateTime.now()
    ) {
        companion object {
            fun AppEntity.toGetAppResponse(): GetAppResponse {
                return GetAppResponse(
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