package com.qa.socialapi.fixture

import com.qa.socialapi.repository.QuestionEntity
import java.util.UUID

fun getQuestionEntityFixture(appId: UUID? = null) = QuestionEntity(
    category = "category",
    content = "content",
    appId = appId
)