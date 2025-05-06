package com.qa.socialapi.integration

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.dto.user.GetUserDto
import com.qa.socialapi.dto.user.UpdateUserDto
import com.qa.socialapi.fixture.getUserEntityFixture
import com.qa.socialapi.repository.UserRepository
import com.qa.socialapi.util.JwtUtil
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import java.util.*

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest
    (
    private val repository: UserRepository,
    private val jwtUtil: JwtUtil,
    private val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) : FunSpec({

    afterEach {
        repository.deleteAll()
    }

    context("getUser test") {
        test("getUser 성공") {
            // given
            val fixture = getUserEntityFixture()
            val user = repository.save(fixture)
            val accessToken = jwtUtil.generateAccessToken(user.id.toString())

            // when
            val response = mockMvc.get("/api/users") {
                header("Authorization", "Bearer $accessToken")
            }.andReturn().response.contentAsString

            val returnType = object : TypeReference<ResponseWrapper<GetUserDto.GetUserResponse>>() {}
            val result = objectMapper.readValue(response, returnType)

            val actual = result.data
            result.code shouldBe HttpStatus.OK.value()
            actual.id shouldBe user.id
            actual.nickname shouldBe user.nickname
            actual.name shouldBe user.name
            actual.email shouldBe user.email
            actual.platform shouldBe user.platform
            actual.platformId shouldBe user.platformId
            actual.currentPoint shouldBe user.currentPoint
            actual.goalPoint shouldBe user.goalPoint
            actual.ticket shouldBe user.ticket
            actual.createdAt shouldBe user.createdAt
            actual.updatedAt shouldBe user.updatedAt
        }

        test("getUser 실패 - 존재하지 않는 accessToken 사용") {
            // given
            val accessToken = "invalid_token"
            val mvcResult = mockMvc.get("/api/users") {
                header("Authorization", "Bearer $accessToken")
            }.andReturn().response.contentAsString

            val actual = objectMapper.readValue(mvcResult, ResponseWrapper::class.java)
            actual.code shouldBe HttpStatus.UNAUTHORIZED.value()
        }

        test("getUser 실패 - 존재하지 않는 사용자") {
            // given
            val accessToken = jwtUtil.generateAccessToken(UUID.randomUUID().toString())
            val response = mockMvc.get("/api/users") {
                header("Authorization", "Bearer $accessToken")
            }.andReturn().response.contentAsString

            val actual = objectMapper.readValue(response, ResponseWrapper::class.java)
            actual.code shouldBe HttpStatus.NOT_FOUND.value()
        }

        test("getUser 실패 - token 값 null") {
            // given
            val accessToken = null
            val response = mockMvc.get("/api/users") {
                header("Authorization", "Bearer $accessToken")
            }.andReturn().response.contentAsString

            val actual = objectMapper.readValue(response, ResponseWrapper::class.java)
            actual.code shouldBe HttpStatus.UNAUTHORIZED.value()
        }
    }

    context("deleteUser test") {
        test("deleteUser 성공") {
            // given
            val user = repository.save(getUserEntityFixture())
            val accessToken = jwtUtil.generateAccessToken(user.id.toString())

            // when
            val response = mockMvc.delete("/api/users") {
                header("Authorization", "Bearer $accessToken")
            }.andReturn().response.contentAsString

            // then
            val actual = objectMapper.readValue(response, ResponseWrapper::class.java)
            actual.code shouldBe HttpStatus.NO_CONTENT.value()
            repository.findAll().size shouldBe 0
        }

        test("deleteUser 실패 - 존재하지 않는 사용자") {
            // given
            val accessToken = jwtUtil.generateAccessToken(UUID.randomUUID().toString())

            // when
            val response = mockMvc.delete("/api/users") {
                header("Authorization", "Bearer $accessToken")
            }.andReturn().response.contentAsString

            // then
            val actual = objectMapper.readValue(response, ResponseWrapper::class.java)
            actual.code shouldBe HttpStatus.NOT_FOUND.value()
        }
    }

    context("updateUser test") {
        test("updateUser 성공") {
            // given
            val user = repository.save(getUserEntityFixture())
            val accessToken = jwtUtil.generateAccessToken(user.id.toString())
            val returnType = object : TypeReference<ResponseWrapper<UpdateUserDto.UpdateUserResponse>>() {}
            val expected = UpdateUserDto.UpdateUserRequest(
                name = "new_name",
                email = "new_email",
                nickname = "new_nickname",
                ticket = 30,
                goalPoint = 30,
                currentPoint = 30
            )

            // when
            val response = mockMvc.put("/api/users") {
                header("Authorization", "Bearer $accessToken")
                contentType = org.springframework.http.MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(expected)
            }.andReturn().response.contentAsString

            // then
            val actual = objectMapper.readValue(response, returnType)
            actual.code shouldBe HttpStatus.OK.value()
            actual.data.name shouldBe expected.name
            actual.data.email shouldBe expected.email
            actual.data.nickname shouldBe expected.nickname
            actual.data.ticket shouldBe expected.ticket
            actual.data.goalPoint shouldBe expected.goalPoint
            actual.data.currentPoint shouldBe expected.currentPoint
        }

        test("updateUser 실패 - 존재하지 않는 사용자") {
            // given
            val accessToken = jwtUtil.generateAccessToken(UUID.randomUUID().toString())
            val body = UpdateUserDto.UpdateUserRequest()

            // when
            val response = mockMvc.put("/api/users") {
                header("Authorization", "Bearer $accessToken")
                contentType = org.springframework.http.MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }.andReturn().response.contentAsString

            // then
            val actual = objectMapper.readValue(response, ResponseWrapper::class.java)
            actual.code shouldBe HttpStatus.NOT_FOUND.value()
        }
    }
})