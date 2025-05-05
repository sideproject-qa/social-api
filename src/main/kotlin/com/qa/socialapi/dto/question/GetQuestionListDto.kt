package com.qa.socialapi.dto.question

import com.qa.socialapi.repository.QuestionEntity
import java.time.LocalDateTime
import java.util.*

object GetQuestionListDto {

    data class GetQuestionListResponse(
        val list: List<Question>
    )

    data class Question(
        val id: UUID,
        val category: String,
        val content: String,
        val createdAt: LocalDateTime? = null,
        val updatedAt: LocalDateTime? = null
    ) {
        companion object {
            fun QuestionEntity.toQuestion(): Question {
                return Question(
                    id = id,
                    category = category,
                    content = content,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                )
            }
        }
    }
}