package com.qa.socialapi.controller

import com.qa.socialapi.application.UserService
import com.qa.socialapi.docs.GET_USER_RESPONSE
import com.qa.socialapi.docs.UPDATE_USER_RESPONSE
import com.qa.socialapi.dto.user.GetUserDto.GetUserResponse
import com.qa.socialapi.dto.user.GetUserDto.GetUserResponse.Companion.toGetUserResponse
import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.dto.user.UpdateUserDto.UpdateUserRequest
import com.qa.socialapi.dto.user.UpdateUserDto.UpdateUserResponse
import com.qa.socialapi.dto.user.UpdateUserDto.UpdateUserResponse.Companion.toUpdateUserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping
    @Operation(summary = "사용자 정보 조회", description = "로그인 토큰으로 인증된 사용자의 정보를 가져옵니다.")
    @ApiResponse(responseCode = "200", description = GET_USER_RESPONSE)
    @ApiResponse(
        responseCode = "401",
        description = "잘못된 access token",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ResponseWrapper::class)
        )]
    )
    @ApiResponse(
        responseCode = "404",
        description = "존재하지 않는 사용자",
        content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ResponseWrapper::class)
        )]
    )
    fun getUser(@RequestHeader("Authorization") auth: String): ResponseEntity<ResponseWrapper<GetUserResponse>> {
        val accessToken = auth.removePrefix("Bearer ")
        return wrap(HttpStatus.OK, data = userService.findById(accessToken).toGetUserResponse())
    }

    @Operation(summary = "사용자 정보 제거", description = "로그인 토큰으로 해당 사용자를 제거합니다.")
    @ApiResponse(responseCode = "204", description = "사용자 정상 제거")
    @ApiResponse(responseCode = "401", description = "잘못된 access token")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
    @DeleteMapping
    fun deleteUser(@RequestHeader("Authorization") auth: String): ResponseEntity<ResponseWrapper<Unit>> {
        val accessToken = auth.removePrefix("Bearer ")
        userService.deleteById(accessToken)
        return wrap(HttpStatus.NO_CONTENT, data = Unit)
    }

    @Operation(
        summary = "사용자 정보 수정",
        description = "로그인 토큰으로 사용자 정보를 수정합니다.\n\n수정하고자 하는 속성에만 값을 입력해주세요."
    )
    @ApiResponse(
        responseCode = "200",
        description = UPDATE_USER_RESPONSE
    )
    @ApiResponse(
        responseCode = "401", description = "잘못된 access token", content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ResponseWrapper::class)
        )]
    )
    @ApiResponse(
        responseCode = "404", description = "존재하지 않는 사용자", content = [Content(
            mediaType = "application/json",
            schema = Schema(implementation = ResponseWrapper::class)
        )]
    )
    @PutMapping
    fun updateUser(
        @RequestHeader("Authorization") auth: String,
        @RequestBody dto: UpdateUserRequest
    ): ResponseEntity<ResponseWrapper<UpdateUserResponse>> {
        val accessToken = auth.removePrefix("Bearer ")
        return wrap(HttpStatus.OK, data = userService.update(accessToken, dto).toUpdateUserResponse())
    }

    private fun <T> wrap(httpStatus: HttpStatus, message: String = "success", data: T):
            ResponseEntity<ResponseWrapper<T>> {
        return ResponseEntity
            .status(httpStatus)
            .body(ResponseWrapper.from(httpStatus = httpStatus, message = message, data = data))
    }
}