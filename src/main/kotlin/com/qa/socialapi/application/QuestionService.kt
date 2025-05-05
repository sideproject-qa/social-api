package com.qa.socialapi.application

import com.qa.socialapi.repository.QuestionEntity
import com.qa.socialapi.repository.QuestionRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class QuestionService(
    private val repository: QuestionRepository
) {
    fun findAll(): List<QuestionEntity> {
        return repository.findAllByAppIdIsNull()
    }

    fun findByAppId(appId: UUID): List<QuestionEntity> {
        return repository.findByAppId(appId)
    }
}