package com.qa.socialapi.repository

import com.qa.socialapi.dto.user.UpdateUserDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
data class UserEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val ticket: Int = 0,
    val goalPoint: Int = 0,
    val currentPoint: Int = 0,
    val refreshToken: String? = null,

    @Column(nullable = false, unique = true)
    val platformId: String,

    @Column(nullable = false)
    val platform: String,

    @Column(nullable = true)
    val name: String? = null,

    @Column(nullable = true, unique = true)
    val email: String? = null,

    @Column(nullable = true)
    val nickname: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null
) {
    fun update(dto: UpdateUserDto.UpdateUserRequest): UserEntity {
        return this.copy(
            name = dto.name ?: this.name,
            email = dto.email ?: this.email,
            nickname = dto.nickname ?: this.nickname,
            ticket = dto.ticket ?: this.ticket,
            goalPoint = dto.goalPoint ?: this.goalPoint,
            currentPoint = dto.currentPoint ?: this.currentPoint,
        )
    }
}
