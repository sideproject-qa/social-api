package com.qa.socialapi.controller

import com.qa.socialapi.application.GoogleOAuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/api/auth/google")
class AuthController(
    private val googleOAuthService: GoogleOAuthService
) {
    private val usedCodes = ConcurrentHashMap.newKeySet<String>()  // 중복 코드 사용 방지

    /** 🔹 Google에서 받은 code를 사용해 로그인 처리 */
    @GetMapping("/callback")
    fun googleCallback(@RequestParam("code") code: String): ResponseEntity<Any> {
        try {
            val decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8.toString())

            if (!usedCodes.add(decodedCode)) {
                println("❌ 이미 사용된 Authorization Code: $decodedCode")
                return ResponseEntity.badRequest().body("Invalid grant: Code already used.")
            }

            println("🔹 Received Authorization Code: $code")
            println("🔹 Decoded Authorization Code: $decodedCode")

            val accessToken = googleOAuthService.getAccessToken(decodedCode)
            println("✅ Access Token Received: $accessToken")

            val userInfo = googleOAuthService.getUserInfo(accessToken)
            println("✅ User Info: $userInfo")

            return ResponseEntity.ok(userInfo)
        } catch (e: Exception) {
            println("❌ Error during OAuth callback: ${e.message}")
            return ResponseEntity.internalServerError().body("OAuth Callback Error: ${e.message}")
        }
    }
}
