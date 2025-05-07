package com.qa.socialapi.repository

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import java.util.UUID

@Entity
data class UserFeedbackEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val testId: UUID,
    val testQuestionId: UUID,
    val answer: String,
    val image: String? = null,
    @CreationTimestamp
    val createdAt: Timestamp? = null,
    @UpdateTimestamp
    val updatedAt: Timestamp? = null
)