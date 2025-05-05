package com.qa.socialapi.controller

import com.qa.socialapi.application.UserService
import com.qa.socialapi.dto.user.GetUserDto.GetUserResponse
import com.qa.socialapi.dto.user.GetUserDto.GetUserResponse.Companion.toGetUserResponse
import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.dto.user.UpdateUserDto.UpdateUserRequest
import com.qa.socialapi.dto.user.UpdateUserDto.UpdateUserResponse
import com.qa.socialapi.dto.user.UpdateUserDto.UpdateUserResponse.Companion.toUpdateUserResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping
    fun getUser(@RequestHeader("Authorization") auth: String): ResponseEntity<ResponseWrapper<GetUserResponse>> {
        val accessToken = auth.removePrefix("Bearer ")
        return wrap(HttpStatus.OK, data = userService.findById(accessToken).toGetUserResponse())
    }

    @DeleteMapping
    fun deleteUser(@RequestHeader("Authorization") auth: String): ResponseEntity<ResponseWrapper<Unit>> {
        val accessToken = auth.removePrefix("Bearer ")
        userService.deleteById(accessToken)
        return wrap(HttpStatus.NO_CONTENT, data = Unit)
    }

    @PutMapping("{accessToken}")
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