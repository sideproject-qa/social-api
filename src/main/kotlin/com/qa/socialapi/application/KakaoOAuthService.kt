package com.qa.socialapi.application

import com.qa.socialapi.dto.auth.KakaoAuthDto.KakaoAuthResponse
import com.qa.socialapi.enum.Platform
import com.qa.socialapi.exception.error.KakaoTokenInvalidException
import com.qa.socialapi.repository.UserEntity
import com.qa.socialapi.util.JwtUtil
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class KakaoOAuthService(
    private val restTemplate: RestTemplate,
    private val userService: UserService,
    private val jwtUtil: JwtUtil,
    @Value("\${kakao.user-info-uri}") val userInfoUri: String
) {
    private val logger = KotlinLogging.logger {}

    fun handleKakao(accessToken: String): Pair<Boolean, KakaoAuthResponse> {
        val userInfo = getUserInfo(accessToken)

        val existingUser = userService.findByPlatformId(userInfo.platformId)
        val isNewUser: Boolean
        val user: UserEntity

        if (existingUser == null) {
            user = userService.save(
                UserEntity(
                    platform = Platform.KAKAO,
                    platformId = userInfo.platformId,
                    email = userInfo.email,
                    nickname = userInfo.nickname,
                )
            )
            isNewUser = true
        } else {
            user = existingUser
            isNewUser = false
        }

        val refreshToken = jwtUtil.generateRefreshToken(user.id.toString())
        userService.save(user.copy(refreshToken = refreshToken))

        return Pair(
            isNewUser, KakaoAuthResponse(
                platform = Platform.KAKAO.name,
                accessToken = jwtUtil.generateAccessToken(user.id.toString()),
                refreshToken = refreshToken
            )
        )

    }

    fun getUserInfo(accessToken: String): KakaoUserInfo {
        logger.info { "accessToken: $accessToken" }
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $accessToken")
        }

        val request = HttpEntity<String>(headers)
        val result = runCatching {
            restTemplate.exchange(userInfoUri, HttpMethod.GET, request, Map::class.java)
        }.onFailure {
            logger.info { "exception: $it" }
            logger.info { "Failed to get user info from kakao " }
            throw KakaoTokenInvalidException("Failed to get user info from kakao")
        }.getOrThrow()
        val responseBody = result.body as Map<*, *>
        val id = responseBody["id"]?.toString() ?: throw KakaoTokenInvalidException("Kakao token verification failed")


        val kakaoAccount = responseBody["kakao_account"] as? Map<*, *>
            ?: throw KakaoTokenInvalidException("Kakao account not found")
        val email = kakaoAccount["email"] as? String
        val profile = kakaoAccount["profile"] as? Map<*, *>
        val nickname = profile?.get("nickname") as? String

        return KakaoUserInfo(
            platformId = id,
            email = email,
            nickname = nickname,
        )
    }

    data class KakaoUserInfo(
        val platformId: String,
        val email: String?,
        val nickname: String?,
    )
}