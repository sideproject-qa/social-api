package com.qa.socialapi.application

import com.qa.socialapi.dto.GoogleTokenResponse
import com.qa.socialapi.dto.GoogleUserInfoResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Service
class GoogleOAuthService(
    private val restTemplate: RestTemplate
) {

    @Value("\${google.oauth2.client-id}")
    private lateinit var clientId: String

    @Value("\${google.oauth2.client-secret}")
    private lateinit var clientSecret: String

    @Value("\${google.oauth2.redirect-uri}")
    private lateinit var redirectUri: String

    @Value("\${google.oauth2.token-uri}")
    lateinit var tokenUrl: String

    @Value("\${google.oauth2.user-info-uri}")
    lateinit var userInfoUrl: String

    /** 🔹 프론트에서 받은 code를 가지고 Google에 access_token 요청 */
    fun getAccessToken(code: String): String {
        try {
            val requestBody: MultiValueMap<String, String> = LinkedMultiValueMap()
            requestBody.add("code", code)
            requestBody.add("client_id", clientId)
            requestBody.add("client_secret", clientSecret)
            requestBody.add("redirect_uri", redirectUri)
            requestBody.add("grant_type", "authorization_code")

            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

            val requestEntity = HttpEntity(requestBody, headers)

            println("🔹 Sending token request to: $tokenUrl")
            println("🔹 Request Body: $requestBody")

            val response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, GoogleTokenResponse::class.java)

            val accessToken = response.body?.accessToken
            println("✅ Received Access Token: $accessToken")

            return accessToken ?: throw RuntimeException("❌ Failed to get access token")
        } catch (e: Exception) {
            println("❌ Error requesting access token: ${e.message}")
            throw e
        }
    }

    /** 🔹 access_token을 이용하여 사용자 정보 요청 */
    fun getUserInfo(accessToken: String): GoogleUserInfoResponse {
        try {
            val headers = HttpHeaders()
            headers.setBearerAuth(accessToken)

            val requestEntity = HttpEntity<Void>(headers)

            println("🔹 Fetching user info from: $userInfoUrl")

            val response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, requestEntity, GoogleUserInfoResponse::class.java)

            return response.body ?: throw RuntimeException("❌ Failed to get user info")
        } catch (e: Exception) {
            println("❌ Error fetching user info: ${e.message}")
            throw e
        }
    }
}
