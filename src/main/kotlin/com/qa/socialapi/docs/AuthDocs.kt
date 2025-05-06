package com.qa.socialapi.docs

const val AUTH_RESPONSE = "로그인 및 회원가입 성공" +
        "\n" +
        "**data 필드 설명**\n" +
        "- `platform` : 소셜 플랫폼 구분 (KAKAO, GOOGLE, APPLE)\n" +
        "- `accessToken` : 액세스 토큰\n" +
        "- `refreshToken` : 리프레시 토큰"

const val REFRESH_TOKEN_RESPONSE = "액세스 토큰 재발급 성공" +
        "\n" +
        "**data 필드 설명**\n" +
        "- `accessToken` : 새로 발급받은 액세스 토큰"