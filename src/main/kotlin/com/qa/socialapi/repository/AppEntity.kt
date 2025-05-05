package com.qa.socialapi.repository

import com.qa.socialapi.dto.app.CreateAppDto
import com.qa.socialapi.dto.app.CreateAppDto.CreateAppRequest
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "app")
data class AppEntity(
    @Id
    val id: UUID,
    val name: String,
    val description: String?,
    val icon: String?,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, updatable = false)
    val updatedAt: LocalDateTime? = null
) {
    companion object {
        fun create(dto: CreateAppRequest): AppEntity = AppEntity(
            id = UUID.randomUUID(),
            name = dto.name,
            description = dto.description,
            icon = dto.icon
        )
    }
}
