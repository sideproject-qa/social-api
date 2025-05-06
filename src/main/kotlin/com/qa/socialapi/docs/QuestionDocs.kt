package com.qa.socialapi.docs

const val GET_QUESTION_LIST_RESPONSE = "질문 목록 조회 성공" +
        "\n" +
        "**data 필드 설명**\n" +
        "- `id` : 질문 식별자\n" +
        "- `category` : 질문의 카테고리, ex) 회원가입 & 로그인\n" +
        "- `content` : 질문 내용, ex)앱을 사용할 때 복잡하다고 느낀 부분이 있나요?\n" +
        "- `createdAt` : 질문 생성 일시\n" +
        "- `updatedAt` : 마지막 업데이트 일시"