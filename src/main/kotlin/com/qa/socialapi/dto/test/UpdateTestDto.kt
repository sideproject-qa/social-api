package com.qa.socialapi.dto.test

import com.qa.socialapi.enum.TestStatus
import com.qa.socialapi.repository.AppEntity
import com.qa.socialapi.repository.TestEntity
import java.time.LocalDateTime
import java.util.*

object UpdateTestDto {
    data class UpdateTestRequest(
        val estimatedTime: Int?,
        val information: String?,
        val currentAttendees: Int?,
        val maxAttendees: Int?,
        val appStart: LocalDateTime?,
        val appEnd: LocalDateTime?,
        val eventStart: LocalDateTime?,
        val eventEnd: LocalDateTime?,
        val rewardPoint: Int?,
        val status: TestStatus?,
        val iosMinSpec: Int?,
        val androidMinSpec: Int?,
    )

    data class UpdateTestResponse(
        val id: UUID,
        val estimatedTime: Int?,
        val information: String?,
        val currentAttendees: Int = 0,
        val maxAttendees: Int = 0,
        val appStart: LocalDateTime,
        val appEnd: LocalDateTime,
        val eventStart: LocalDateTime,
        val eventEnd: LocalDateTime,
        val rewardPoint: Int,
        val status: TestStatus,
        val app: AppEntity,
        val iosMinSpec: Int,
        val androidMinSpec: Int,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime? = null
    ) {
        companion object {
            fun TestEntity.toUpdateTestResponse(): UpdateTestResponse {
                return UpdateTestResponse(
                    id = id,
                    estimatedTime = estimatedTime,
                    information = information,
                    currentAttendees = currentAttendees,
                    maxAttendees = maxAttendees,
                    appStart = appStart,
                    appEnd = appEnd,
                    eventStart = eventStart,
                    eventEnd = eventEnd,
                    rewardPoint = rewardPoint,
                    status = status,
                    app = app,
                    iosMinSpec = iosMinSpec,
                    androidMinSpec = androidMinSpec,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }
        }
    }
}