package com.qa.socialapi.fixture

import com.qa.socialapi.repository.AppEntity

fun getAppEntityFixture() = AppEntity(
    name = "app_name",
    description = "app_description",
    icon = "app_icon"
)