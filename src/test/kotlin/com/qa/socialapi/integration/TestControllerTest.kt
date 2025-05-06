package com.qa.socialapi.integration

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.dto.test.GetTestListDto
import com.qa.socialapi.dto.test.UpdateTestDto
import com.qa.socialapi.enum.TestStatus
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
import org.springframework.test.web.servlet.put
import java.time.LocalDateTime

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

    context("updateTest test") {
        test("updateTest 성공") {
            // given
            val app = appRepository.save(getAppEntityFixture())
            val test = testRepository.save(getTestEntityFixture(app))
            val returnType = object : TypeReference<ResponseWrapper<UpdateTestDto.UpdateTestResponse>>() {}
            val expected = UpdateTestDto.UpdateTestRequest(
                estimatedTime = 30,
                information = "new_information",
                currentAttendees = 40,
                maxAttendees = 50,
                appStart = LocalDateTime.now(),
                appEnd = LocalDateTime.now(),
                eventStart = LocalDateTime.now(),
                eventEnd = LocalDateTime.now(),
                rewardPoint = 50,
                status = TestStatus.PROGRESS,
                iosMinSpec = 14,
                androidMinSpec = 15

            )

            // when
            val response = mockMvc.put("/api/tests/${test.id}") {
                contentType = org.springframework.http.MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(expected)
            }.andReturn().response.contentAsString

            // then
            val actual = objectMapper.readValue(response, returnType)
            actual.code shouldBe HttpStatus.OK.value()
            actual.data.appEnd shouldBe expected.appEnd
            actual.data.appStart shouldBe expected.appStart
            actual.data.eventEnd shouldBe expected.eventEnd
            actual.data.eventStart shouldBe expected.eventStart
            actual.data.iosMinSpec shouldBe expected.iosMinSpec
            actual.data.androidMinSpec shouldBe expected.androidMinSpec
            actual.data.information shouldBe expected.information
            actual.data.rewardPoint shouldBe expected.rewardPoint
            actual.data.status shouldBe expected.status
            actual.data.currentAttendees shouldBe expected.currentAttendees
            actual.data.estimatedTime shouldBe expected.estimatedTime
            actual.data.maxAttendees shouldBe expected.maxAttendees
        }
    }
})