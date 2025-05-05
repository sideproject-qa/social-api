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
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("{accessToken}")
    fun getUser(@PathVariable accessToken: String): ResponseEntity<ResponseWrapper<GetUserResponse>> {
        return wrap(HttpStatus.OK, data = userService.findById(accessToken).toGetUserResponse())
    }

    @DeleteMapping("{accessToken}")
    fun deleteUser(@PathVariable accessToken: String): ResponseEntity<ResponseWrapper<Unit>> {
        userService.deleteById(accessToken)
        return wrap(HttpStatus.NO_CONTENT, data = Unit)
    }

    @PutMapping("{accessToken}")
    fun updateUser(
        @PathVariable accessToken: String,
        @RequestBody dto: UpdateUserRequest
    ): ResponseEntity<ResponseWrapper<UpdateUserResponse>> {
        return wrap(HttpStatus.OK, data = userService.update(accessToken, dto).toUpdateUserResponse())
    }

    private fun <T> wrap(httpStatus: HttpStatus, message: String = "success", data: T):
            ResponseEntity<ResponseWrapper<T>> {
        return ResponseEntity
            .status(httpStatus)
            .body(ResponseWrapper.from(httpStatus = httpStatus, message = message, data = data))
    }
}