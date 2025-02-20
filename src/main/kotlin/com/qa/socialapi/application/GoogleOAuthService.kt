package com.qa.socialapi.application

import com.fasterxml.jackson.annotation.JsonProperty
import com.qa.socialapi.dto.GoogleAuthDto.GoogleAuthResponse
import com.qa.socialapi.util.JwtUtil
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Service
class GoogleOAuthService(
    private val restTemplate: RestTemplate,
    private val userService: UserService,
    private val jwtUtil: JwtUtil,

    @Value("\${google.oauth2.client-id}") private val clientId: String,
    @Value("\${google.oauth2.client-secret}") private val clientSecret: String,
    @Value("\${google.oauth2.redirect-uri}") private val redirectUri: String,
    @Value("\${google.oauth2.token-uri}") private val tokenUrl: String,
    @Value("\${google.oauth2.user-info-uri}") private val userInfoUrl: String
) {
    private val logger = KotlinLogging.logger {}

    fun handleGoogleAuth(code: String): GoogleAuthResponse {
        val googleAccessToken = getAccessToken(code)
        val userInfo = getUserInfo(googleAccessToken)

        val user = userService.findByProviderId(userInfo.sub)
            ?: userService.saveUser(
                provider = "google",
                providerId = userInfo.sub,
                name = userInfo.name,
                email = userInfo.email
            )

        return GoogleAuthResponse(
            provider = user.provider,
            accessToken = jwtUtil.generateAccessToken(user.id.toString()),
            refreshToken = jwtUtil.generateRefreshToken(user.id.toString()),
        )
    }

    fun getAccessToken(code: String): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val body: MultiValueMap<String, String> = LinkedMultiValueMap()
        body.add("grant_type", "authorization_code")
        body.add("client_id", clientId)
        body.add("client_secret", clientSecret)
        body.add("redirect_uri", redirectUri)
        body.add("code", code)

        val request = HttpEntity(body, headers)
        val response = restTemplate.postForEntity(tokenUrl, request, Map::class.java)
        if (!response.statusCode.is2xxSuccessful) {
            throw RuntimeException("Failed to get access token from google")
        }

        logger.info { "Received response: $response" }

        val responseBody = response.body
        return responseBody?.get("access_token") as String
    }

    fun getUserInfo(accessToken: String): GoogleUserInfo {
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $accessToken")
        }

        val request = HttpEntity<String>(headers)
        val response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, GoogleUserInfo::class.java)

        return response.body ?: throw RuntimeException("Failed to get user info")
    }

    companion object {
        data class GoogleUserInfo(
            val sub: String,
            val name: String,
            val email: String,
            @JsonProperty("picture") val profilePicture: String
        )
    }
}
