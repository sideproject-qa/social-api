package com.qa.socialapi.controller

import com.qa.socialapi.application.AppleOAuthService
import com.qa.socialapi.application.AuthService
import com.qa.socialapi.application.GoogleOAuthService
import com.qa.socialapi.application.KakaoOAuthService
import com.qa.socialapi.docs.AUTH_RESPONSE
import com.qa.socialapi.docs.REFRESH_TOKEN_RESPONSE
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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
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

    @Operation(summary = "구글 로그인 및 회원가입 - Android", description = "구글 OAuth - Android")
    @ApiResponse(responseCode = "200", description = AUTH_RESPONSE)
    @ApiResponse(responseCode = "400", description = "잘못된 구글 토큰 사용")
    @PostMapping("/google/android")
    fun googleAuthByAndroid(@RequestBody request: GoogleAuthRequest): ResponseEntity<ResponseWrapper<GoogleAuthResponse>> {
        val result = googleOAuthService.handleGoogle(request.idToken, OSType.ANDROID)
        return wrap(isNew = result.first, authResult = result.second)
    }

    @Operation(summary = "구글 로그인 및 회원가입 - iOS", description = "구글 OAuth - iOS")
    @ApiResponse(responseCode = "200", description = AUTH_RESPONSE)
    @ApiResponse(responseCode = "400", description = "잘못된 구글 토큰 사용")
    @PostMapping("/google/ios")
    fun googleAuthByIos(@RequestBody request: GoogleAuthRequest): ResponseEntity<ResponseWrapper<GoogleAuthResponse>> {
        val result = googleOAuthService.handleGoogle(request.idToken, OSType.IOS)
        return wrap(isNew = result.first, authResult = result.second)
    }

    @Operation(summary = "카카오 로그인 및 회원가입", description = "카카오 OAuth")
    @ApiResponse(responseCode = "200", description = AUTH_RESPONSE)
    @ApiResponse(responseCode = "400", description = "잘못된 카카오 토큰 사용")
    @PostMapping("/kakao")
    fun kakaoAuth(@RequestBody request: KakaoAuthRequest): ResponseEntity<ResponseWrapper<KakaoAuthResponse>> {
        val result = kakaoOAuthService.handleKakao(request.accessToken)
        return wrap(isNew = result.first, authResult = result.second)
    }

    @Operation(summary = "애플 로그인 및 회원가입", description = "애플 OAuth")
    @ApiResponse(responseCode = "200", description = AUTH_RESPONSE)
    @ApiResponse(responseCode = "400", description = "잘못된 애플 토큰 사용")
    @PostMapping("/apple")
    fun appleAuth(@RequestBody request: AppleAuthRequest): ResponseEntity<ResponseWrapper<AppleAuthResponse>> {
        val result = appleOAuthService.handleApple(request.idToken)
        return wrap(isNew = result.first, authResult = result.second)
    }

    @Operation(summary = "로그아웃", description = "로그아웃으로 Refresh Token을 만료시킵니다.")
    @PostMapping("/logout")
    fun logout(@RequestBody request: RefreshTokenRequest): ResponseEntity<ResponseWrapper<Unit>> {
        authService.logout(request.refreshToken)
        return wrap(authResult = Unit)
    }


    @Operation(summary = "Access Token 재발급", description = "Refresh Token을 통해 새로운 Access Token을 발급받습니다.")
    @ApiResponse(responseCode = "200", description = REFRESH_TOKEN_RESPONSE)
    @ApiResponse(responseCode = "401", description = "잘못된 Refresh Token 사용")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")
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
