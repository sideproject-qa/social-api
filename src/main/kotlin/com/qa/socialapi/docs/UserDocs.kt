package com.qa.socialapi.docs

const val GET_USER_RESPONSE = "조회 성공: 사용자 정보 반환\n" +
        "\n" +
        "**data 필드 설명**\n" +
        "- `id` : 사용자 고유 식별자 (UUID)\n" +
        "- `platformId` : 플랫폼에서 제공한 식별자\n" +
        "- `platform` : 소셜 플랫폼 구분 (KAKAO, GOOGLE, APPLE)\n" +
        "- `name` : 실명 (선택)\n" +
        "- `email` : 이메일 주소 (선택)\n" +
        "- `nickname` : 별명 (선택)\n" +
        "- `ticket` : 보유중인 티켓 수\n" +
        "- `goalPoint` : 목표 포인트\n" +
        "- `currentPoint` : 현재 보유 포인트\n" +
        "- `createdAt` : 계정 생성 일시\n" +
        "- `updatedAt` : 마지막 업데이트 일시"

const val UPDATE_USER_RESPONSE = "정보 수정 성공: 수정된 사용자 정보 반환\n" +
        "\n" +
        "**data 필드 설명**\n" +
        "- `id` : 사용자 고유 식별자 (UUID)\n" +
        "- `platformId` : 플랫폼에서 제공한 식별자\n" +
        "- `platform` : 소셜 플랫폼 구분 (KAKAO, GOOGLE, APPLE)\n" +
        "- `name` : 실명 (선택)\n" +
        "- `email` : 이메일 주소 (선택)\n" +
        "- `nickname` : 별명 (선택)\n" +
        "- `ticket` : 보유중인 티켓 수\n" +
        "- `goalPoint` : 목표 포인트\n" +
        "- `currentPoint` : 현재 보유 포인트\n" +
        "- `createdAt` : 계정 생성 일시\n" +
        "- `updatedAt` : 마지막 업데이트 일시"