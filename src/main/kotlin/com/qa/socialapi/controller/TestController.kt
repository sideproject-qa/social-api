package com.qa.socialapi.controller

import com.qa.socialapi.application.TestService
import com.qa.socialapi.docs.GET_TEST_LIST_RESPONSE
import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.dto.test.CreateTestDto.CreateTestRequest
import com.qa.socialapi.dto.test.CreateTestDto.CreateTestResponse
import com.qa.socialapi.dto.test.CreateTestDto.CreateTestResponse.Companion.toCreateTestResponse
import com.qa.socialapi.dto.test.GetTestListDto
import com.qa.socialapi.dto.test.GetTestListDto.GetTestListRequest
import com.qa.socialapi.dto.test.GetTestListDto.GetTestListResponse
import com.qa.socialapi.dto.test.GetTestListDto.Test.Companion.toTest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class TestController(
    val service: TestService
) {
    @Operation(summary = "테스트 생성", description = "테스트 생성")
    @ApiResponse(responseCode = "201", description = "테스트 생성 성공")
    @PostMapping("test")
    fun createTest(@RequestBody dto: CreateTestRequest):
            ResponseEntity<ResponseWrapper<CreateTestResponse>> {
        return wrap(httpStatus = HttpStatus.CREATED, data = service.save(dto).toCreateTestResponse())
    }

    @Operation(summary = "테스트 목록 조회", description = "테스트 목록 제공")
    @ApiResponse(responseCode = "200", description = GET_TEST_LIST_RESPONSE)
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