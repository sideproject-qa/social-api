package com.qa.socialapi.controller

import com.qa.socialapi.application.QuestionService
import com.qa.socialapi.docs.GET_QUESTION_LIST_RESPONSE
import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.dto.question.GetQuestionListDto.GetQuestionListResponse
import com.qa.socialapi.dto.question.GetQuestionListDto.Question.Companion.toQuestion
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api")
class QuestionController(private val service: QuestionService) {

    @Operation(summary = "질문 목록 조회", description = "공통 질문 목록 + 특정 테스트의 질문 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = GET_QUESTION_LIST_RESPONSE)
    @GetMapping("questions")
    fun getQuestionList(
        @RequestParam(name = "appId", required = false) appId: UUID?,
    ): ResponseEntity<ResponseWrapper<GetQuestionListResponse>> {
        val questionList = service.findAllByAppIdIsNull() +
                (appId?.let { service.findByAppId(it) } ?: emptyList())

        return wrap(httpStatus = HttpStatus.OK, data = GetQuestionListResponse(questionList.map { it.toQuestion() }))
    }

    fun <T> wrap(httpStatus: HttpStatus, message: String = "success", data: T): ResponseEntity<ResponseWrapper<T>> {
        return ResponseEntity
            .status(httpStatus)
            .body(ResponseWrapper.from(httpStatus = httpStatus, message = message, data = data))
    }

}