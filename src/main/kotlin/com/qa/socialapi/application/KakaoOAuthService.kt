package com.qa.socialapi.application

import com.qa.socialapi.dto.KakaoAuthDto.KakaoAuthResponse
import com.qa.socialapi.util.JwtUtil
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class KakaoOAuthService(
    private val restTemplate: RestTemplate,
    private val userService: UserService,
    private val jwtUtil: JwtUtil,

    @Value("\${kakao.oauth2.client-id}") private val clientId: String,
    @Value("\${kakao.oauth2.redirect-uri}") private val redirectUri: String,
    @Value("\${kakao.oauth2.token-uri}") val tokenUri: String,
    @Value("\${kakao.oauth2.user-info-uri}") val userInfoUri: String
) {
    private val logger = KotlinLogging.logger {}

    fun handleKakaoAuth(code: String): KakaoAuthResponse {
        val kakaoAccessToken = getAccessToken(code)
        val userInfo = getUserInfo(kakaoAccessToken)

        val user = userService.findByProviderId(userInfo.id)
            ?: userService.saveUser(
                provider = "kakao",
                providerId = userInfo.id,
                email = userInfo.email,
                nickname = userInfo.nickname
            )

        return KakaoAuthResponse(
            provider = user.provider,
            accessToken = jwtUtil.generateAccessToken(user.id.toString()),
            refreshToken = jwtUtil.generateRefreshToken(user.id.toString()),
        )
    }

    fun getAccessToken(code: String): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val body: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("client_id", clientId)
            add("redirect_uri", redirectUri)
            add("code", code)
        }

        val request = HttpEntity(body, headers)
        val response = restTemplate.postForEntity(tokenUri, request, Map::class.java)
        if (!response.statusCode.is2xxSuccessful) {
            throw RuntimeException("Failed to get access token from kakao")
        }

        logger.info { "Received response: $response" }

        val responseBody = response.body
        return responseBody?.get("access_token") as String
    }

    fun getUserInfo(accessToken: String): KakaoUserInfo {
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $accessToken")
        }

        val request = HttpEntity<String>(headers)
        val response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, Map::class.java)
        if (!response.statusCode.is2xxSuccessful) {
            throw RuntimeException("Failed to get user info from kakao")
        }
        val responseBody = response.body as Map<*, *>
        val id = responseBody["id"] as String

        val kakaoAccount = responseBody["kakao_account"] as String as Map<String, Any>
        val email = kakaoAccount["email"] as String
        val profile = kakaoAccount["profile"] as? Map<*, *>
        val nickname = profile?.get("nickname") as? String

        return KakaoUserInfo(
            id = id,
            email = email,
            nickname = nickname,
        )
    }

    companion object {
        data class KakaoUserInfo(
            val id: String,
            val email: String,
            val nickname: String?,
        )
    }
}