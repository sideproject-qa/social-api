package com.qa.socialapi.integration

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.dto.test.GetTestListDto
import com.qa.socialapi.fixture.getAppEntityFixture
import com.qa.socialapi.fixture.getTestEntityFixture
import com.qa.socialapi.repository.AppRepository
import com.qa.socialapi.repository.TestRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class TestControllerTest(
    val testRepository: TestRepository,
    val appRepository: AppRepository,
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) : FunSpec({

    afterEach {
        testRepository.deleteAll()
    }

    context("getTestList test") {
        test("getTestList 성공") {
            // given
            val app = appRepository.save(getAppEntityFixture())
            val test = testRepository.save(getTestEntityFixture(app))
            val returnType = object : TypeReference<ResponseWrapper<GetTestListDto.GetTestListResponse>>() {}

            // when
            val response = mockMvc.get("/api/tests")
                .andReturn()
                .response.contentAsString

            // then
            val actual = objectMapper.readValue(response, returnType)
            val data = actual.data.list.first()
            actual.code shouldBe HttpStatus.OK.value()
            data.id shouldBe test.id
            data.appEnd shouldBe test.appEnd
            data.appStart shouldBe test.appStart
            data.eventEnd shouldBe test.eventEnd
            data.eventStart shouldBe test.eventStart
            data.iosMinSpec shouldBe test.iosMinSpec
            data.androidMinSpec shouldBe test.androidMinSpec
            data.information shouldBe test.information
            data.rewardPoint shouldBe test.rewardPoint
            data.status shouldBe test.status
            data.createdAt shouldBe test.createdAt
            data.updatedAt shouldBe test.updatedAt
            data.currentAttendees shouldBe test.currentAttendees
            data.estimatedTime shouldBe test.estimatedTime
            data.maxAttendees shouldBe test.maxAttendees
            data.app shouldBe app
        }
    }

})