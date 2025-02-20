package com.qa.socialapi.controller

import com.qa.socialapi.application.GoogleOAuthService
import com.qa.socialapi.application.KakaoOAuthService
import com.qa.socialapi.application.UserService
import com.qa.socialapi.dto.GoogleAuthDto.GoogleAuthResponse
import com.qa.socialapi.dto.KakaoAuthDto.KakaoAuthResponse
import com.qa.socialapi.dto.RefreshTokenRequest
import com.qa.socialapi.util.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val googleOAuthService: GoogleOAuthService,
    private val kakaoOAuthService: KakaoOAuthService,
    private val jwtUtil: JwtUtil,
    private val userService: UserService
) {

    @GetMapping("/google/callback")
    fun googleAuth(@RequestParam("code") code: String): ResponseEntity<GoogleAuthResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(googleOAuthService.handleGoogleAuth(code))
    }

    @GetMapping("/kakao/callback")
    fun kakaoAuth(@RequestParam("code") code: String): ResponseEntity<KakaoAuthResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(kakaoOAuthService.handleKakaoAuth(code))
    }


    // refresh token을 데이터베이스에 저장할지는 고민
    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<Map<String, String>> {
        val refreshToken = request.refreshToken

        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).body(mapOf("error" to "Invalid refresh token"))
        }

        val userId = jwtUtil.getUserIdFromToken(refreshToken, isRefreshToken = true)
        val user = userService.findById(UUID.fromString(userId))
            ?: return ResponseEntity.status(401)
                .body(mapOf("error" to "User not found"))

        val newAccessToken = jwtUtil.generateAccessToken(user.id.toString())
        return ResponseEntity.ok(mapOf("access_token" to newAccessToken))

    }

    @GetMapping("/test")
    fun test(): ResponseEntity<String> {
        return ResponseEntity.ok("Test Success")
    }
}
