package com.qa.socialapi.integration

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.dto.question.GetQuestionListDto
import com.qa.socialapi.fixture.getQuestionEntityFixture
import com.qa.socialapi.repository.QuestionRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class QuestControllerTest(
    val repository: QuestionRepository,
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
): FunSpec({

    afterEach {
        repository.deleteAll()
    }

    context("getQuestionList") {
        test("getQuestionList 성공") {
            // given
            val appId = UUID.randomUUID()
            repository.save(getQuestionEntityFixture())
            repository.save(getQuestionEntityFixture(appId))
            val returnType = object: TypeReference<ResponseWrapper<GetQuestionListDto.GetQuestionListResponse>>() {}

            // when
            val response = mockMvc.get("/api/questions") {
                param("appId", appId.toString())
            }
                .andReturn()
                .response.contentAsString

            // then
            val actual = objectMapper.readValue(response, returnType)
            actual.data.list.size shouldBe 2
        }
    }
})