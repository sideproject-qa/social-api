package com.qa.socialapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface QuestionRepository: JpaRepository<QuestionEntity, UUID> {
    fun findByAppId(appId: UUID): MutableList<QuestionEntity>

    fun findAllByAppIdIsNull(): MutableList<QuestionEntity>
}