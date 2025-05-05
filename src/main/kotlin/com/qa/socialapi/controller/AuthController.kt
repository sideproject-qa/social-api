package com.qa.socialapi.controller

import com.qa.socialapi.application.AppleOAuthService
import com.qa.socialapi.application.AuthService
import com.qa.socialapi.application.GoogleOAuthService
import com.qa.socialapi.application.KakaoOAuthService
import com.qa.socialapi.dto.auth.AppleAuthDto.AppleAuthRequest
import com.qa.socialapi.dto.auth.AppleAuthDto.AppleAuthResponse
import com.qa.socialapi.dto.auth.GoogleAuthDto.GoogleAuthRequest
import com.qa.socialapi.dto.auth.GoogleAuthDto.GoogleAuthResponse
import com.qa.socialapi.dto.auth.KakaoAuthDto.KakaoAuthRequest
import com.qa.socialapi.dto.auth.KakaoAuthDto.KakaoAuthResponse
import com.qa.socialapi.dto.auth.RefreshTokenDto.RefreshTokenRequest
import com.qa.socialapi.dto.auth.RefreshTokenDto.RefreshTokenResponse
import com.qa.socialapi.dto.ResponseWrapper
import com.qa.socialapi.enum.OSType
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val googleOAuthService: GoogleOAuthService,
    private val kakaoOAuthService: KakaoOAuthService,
    private val appleOAuthService: AppleOAuthService,
    private val authService: AuthService,
) {
    val logger = KotlinLogging.logger {}

    @PostMapping("/google/android")
    fun googleAuthByAndroid(@RequestBody request: GoogleAuthRequest): ResponseEntity<ResponseWrapper<GoogleAuthResponse>> {
        val result = googleOAuthService.handleGoogle(request.idToken, OSType.ANDROID)
        return wrap(isNew = result.first, authResult = result.second)
    }

    @PostMapping("/google/ios")
    fun googleAuthByIos(@RequestBody request: GoogleAuthRequest): ResponseEntity<ResponseWrapper<GoogleAuthResponse>> {
        val result = googleOAuthService.handleGoogle(request.idToken, OSType.IOS)
        return wrap(isNew = result.first, authResult = result.second)
    }

    @PostMapping("/kakao")
    fun kakaoAuth(@RequestBody request: KakaoAuthRequest): ResponseEntity<ResponseWrapper<KakaoAuthResponse>> {
        val result = kakaoOAuthService.handleKakao(request.accessToken)
        return wrap(isNew = result.first, authResult = result.second)
    }

    @PostMapping("/apple")
    fun appleAuth(@RequestBody request: AppleAuthRequest): ResponseEntity<ResponseWrapper<AppleAuthResponse>> {
        val result = appleOAuthService.handleApple(request.idToken)
        return wrap(isNew = result.first, authResult = result.second)
    }

    @PostMapping("/logout")
    fun logout(@RequestBody request: RefreshTokenRequest): ResponseEntity<ResponseWrapper<Unit>> {
        authService.logout(request.refreshToken)
        return wrap(authResult = Unit)
    }


    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<ResponseWrapper<RefreshTokenResponse>> {
        val result = authService.refreshToken(request.refreshToken)
        return wrap(authResult = RefreshTokenResponse(result))
    }

    private fun <T> wrap(isNew: Boolean = false, authResult: T): ResponseEntity<ResponseWrapper<T>> {
        val httpStatus = if (isNew) HttpStatus.CREATED else HttpStatus.OK
        return ResponseEntity
            .status(httpStatus)
            .body(ResponseWrapper.from(httpStatus = httpStatus, data = authResult))
    }
}
