package com.qa.socialapi.dto.test

import com.qa.socialapi.enum.TestStatus
import com.qa.socialapi.repository.AppEntity
import com.qa.socialapi.repository.TestEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

object GetTestListDto {

    data class GetTestListRequest(
        val status: TestStatus? = null,
    )

    @Schema(description = "테스트 목록 조회 응답")
    data class GetTestListResponse(
        val list: List<Test>
    )

    data class Test(
        @field:Schema(
            description = "사용자 고유 식별자",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
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
            fun TestEntity.toTest(): Test {
                return Test(
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