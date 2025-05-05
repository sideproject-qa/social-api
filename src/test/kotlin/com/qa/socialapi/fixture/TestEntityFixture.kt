package com.qa.socialapi.fixture

import com.qa.socialapi.enum.TestStatus
import com.qa.socialapi.repository.AppEntity
import com.qa.socialapi.repository.TestEntity
import java.time.LocalDateTime

fun getTestEntityFixture(appEntity: AppEntity): TestEntity {
    return TestEntity(
        estimatedTime = 30,
        information = "information",
        currentAttendees = 10,
        maxAttendees = 20,
        appStart = LocalDateTime.now(),
        appEnd = LocalDateTime.now(),
        eventStart = LocalDateTime.now(),
        eventEnd = LocalDateTime.now(),
        rewardPoint = 40,
        status = TestStatus.NEW,
        app = appEntity,
        iosMinSpec = 13,
        androidMinSpec = 14,
    )

}