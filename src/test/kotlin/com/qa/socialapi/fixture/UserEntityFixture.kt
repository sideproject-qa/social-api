package com.qa.socialapi.fixture

import com.qa.socialapi.repository.UserEntity

fun getUserEntityFixture(): UserEntity {
    return UserEntity(
        ticket = 10,
        goalPoint = 20,
        currentPoint = 5,
        refreshToken = null,
        platformId = "platformId",
        platform = "platform",
        name = "name",
        email = "email",
        nickname = "nickname"
    )
}