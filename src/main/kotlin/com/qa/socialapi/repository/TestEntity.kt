package com.qa.socialapi.repository

import com.qa.socialapi.dto.test.CreateTestDto.CreateTestRequest
import com.qa.socialapi.dto.test.UpdateTestDto
import com.qa.socialapi.dto.test.UpdateTestDto.UpdateTestRequest
import com.qa.socialapi.enum.TestStatus
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "app_test")
data class TestEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val estimatedTime: Int?,
    val information: String?,
    val currentAttendees: Int = 0,
    val maxAttendees: Int = 0,
    val appStart: LocalDateTime,
    val appEnd: LocalDateTime,
    val eventStart: LocalDateTime,
    val eventEnd: LocalDateTime,
    val rewardPoint: Int,

    @Enumerated(EnumType.STRING)
    val status: TestStatus,

    @OneToOne
    @JoinColumn(name = "app_id", nullable = false)
    val app: AppEntity,
    val iosMinSpec: Int,
    val androidMinSpec: Int,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = false)
    val updatedAt: LocalDateTime? = null
) {
    fun update(dto: UpdateTestRequest): TestEntity {
        return this.copy(
            appStart = dto.appStart ?: this.appStart,
            appEnd = dto.appEnd ?: this.appEnd,
            eventStart = dto.eventStart ?: this.eventStart,
            eventEnd = dto.eventEnd ?: this.eventEnd,
            estimatedTime = dto.estimatedTime ?: this.estimatedTime,
            information = dto.information ?: this.information,
            currentAttendees = dto.currentAttendees ?: this.currentAttendees,
            maxAttendees = dto.maxAttendees ?: this.maxAttendees,
            rewardPoint = dto.rewardPoint ?: this.rewardPoint,
            status = dto.status ?: this.status,
            iosMinSpec = dto.iosMinSpec ?: this.iosMinSpec,
            androidMinSpec = dto.androidMinSpec ?: this.androidMinSpec,
        )
    }
    companion object {
        fun create(dto: CreateTestRequest): TestEntity {
            return TestEntity(
                id = UUID.randomUUID(),
                estimatedTime = dto.estimatedTime,
                information = dto.information,
                currentAttendees = dto.currentAttendees,
                maxAttendees = dto.maxAttendees,
                appStart = dto.appStart,
                appEnd = dto.appEnd,
                eventStart = dto.eventStart,
                eventEnd = dto.eventEnd,
                rewardPoint = dto.rewardPoint,
                status = dto.status,
                app = dto.app,
                iosMinSpec = dto.iosMinSpec,
                androidMinSpec = dto.androidMinSpec,
            )
        }
    }
}