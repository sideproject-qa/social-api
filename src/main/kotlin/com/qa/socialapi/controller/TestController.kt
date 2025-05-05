package com.qa.socialapi.controller

import com.qa.socialapi.application.TestService
import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.dto.test.CreateTestDto.CreateTestRequest
import com.qa.socialapi.dto.test.CreateTestDto.CreateTestResponse
import com.qa.socialapi.dto.test.CreateTestDto.CreateTestResponse.Companion.toCreateTestResponse
import com.qa.socialapi.dto.test.GetTestListDto
import com.qa.socialapi.dto.test.GetTestListDto.GetTestListRequest
import com.qa.socialapi.dto.test.GetTestListDto.GetTestListResponse
import com.qa.socialapi.dto.test.GetTestListDto.Test.Companion.toTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class TestController(
    val service: TestService
) {
    @PostMapping("test")
    fun createTest(@RequestBody dto: CreateTestRequest):
            ResponseEntity<ResponseWrapper<CreateTestResponse>> {
        return wrap(httpStatus = HttpStatus.CREATED, data = service.save(dto).toCreateTestResponse())

    }

    @GetMapping("tests")
    fun getTestList(): ResponseEntity<ResponseWrapper<GetTestListResponse>> {
        val data = GetTestListResponse(service.findAll().map { it.toTest() })
        return wrap(httpStatus = HttpStatus.OK, data = data)
    }

    fun <T> wrap(httpStatus: HttpStatus, message: String = "success", data: T): ResponseEntity<ResponseWrapper<T>> {
        return ResponseEntity
            .status(httpStatus)
            .body(ResponseWrapper.from(httpStatus = httpStatus, message = message, data = data))
    }

}