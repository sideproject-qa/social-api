//package com.qa.socialapi.controller
//
//import com.qa.socialapi.application.AppService
//import com.qa.socialapi.dto.ResponseWrapper
//import com.qa.socialapi.dto.app.CreateAppDto.CreateAppRequest
//import com.qa.socialapi.dto.app.CreateAppDto.CreateAppResponse
//import com.qa.socialapi.dto.app.CreateAppDto.CreateAppResponse.Companion.toCreateAppResponse
//import com.qa.socialapi.dto.app.GetAppDto.GetAppResponse
//import com.qa.socialapi.dto.app.GetAppDto.GetAppResponse.Companion.toGetAppResponse
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.*
//import java.util.*
//
//@RestController
//@RequestMapping("/api/app")
//class AppController(
//    private val service: AppService
//) {
//
//    @PostMapping
//    fun createApp(@RequestBody dto: CreateAppRequest): ResponseEntity<ResponseWrapper<CreateAppResponse>> {
//        return wrap(httpStatus = HttpStatus.CREATED, data = service.save(dto).toCreateAppResponse())
//    }
//
//    @GetMapping("{id}")
//    fun getApp(@PathVariable id: UUID): ResponseEntity<ResponseWrapper<GetAppResponse>> {
//        return wrap(httpStatus = HttpStatus.OK, data = service.getById(id).toGetAppResponse())
//    }
//
//    @DeleteMapping("{id}")
//    fun deleteApp(@PathVariable id: UUID): ResponseEntity<ResponseWrapper<Unit>> {
//        service.deleteById(id)
//        return wrap(httpStatus = HttpStatus.NO_CONTENT, data = Unit)
//    }
//
//    fun <T> wrap(httpStatus: HttpStatus, message: String = "success", data: T): ResponseEntity<ResponseWrapper<T>> {
//        return ResponseEntity
//            .status(httpStatus)
//            .body(ResponseWrapper.from(httpStatus = httpStatus, message = message, data = data))
//    }
//}