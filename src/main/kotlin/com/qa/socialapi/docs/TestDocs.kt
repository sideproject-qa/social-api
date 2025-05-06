package com.qa.socialapi.docs

const val GET_TEST_LIST_RESPONSE = "테스트 목록 조회 성공: 테스트 목록 반환\n" +
            "\n" +
            "**data 필드 설명**\n" +
            "- `id` : 테스트 고유 식별자 (UUID)\n" +
            "- `estimatedTime` : 예상 소요 시간 (시간)\n" +
            "- `information` : 공지사항 및 유의사항\n" +
            "- `currentAttendees` : 현재 참석자 수\n" +
            "- `maxAttendees` : 최대 참석자 수\n" +
            "- `appStart` : 신청 시작 일시\n" +
            "- `appEnd` : 신청 종료 일시\n" +
            "- `eventStart` : 진행 시작 일시\n" +
            "- `eventEnd` : 진행 종료 일시\n" +
            "- `rewardPoint` : 지급 리워드 포인트\n" +
            "- `status` : 테스트 상태 (NEW, PROGRESS, COMPLETED)\n" +
            "- `app.id` : 앱의 고유 식별자 (UUID)\n" +
            "- `app.name` : 앱 이름\n" +
            "- `app.description` : 관련 앱 설명\n" +
            "- `app.icon` : 앱 아이콘\n" +
            "- `app.createdAt` : 앱 생성 일시\n" +
            "- `app.updatedAt` : 앱 마지막 업데이트 일시\n" +
            "- `iosMinSpec` : iOS 최소 지원 사양 버전\n" +
            "- `androidMinSpec` : Android 최소 지원 사양 버전\n" +
            "- `createdAt` : 생성 일시\n" +
            "- `updatedAt` : 최종 수정 일시"

