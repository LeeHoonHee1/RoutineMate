package com.example.routinemate.domain.validator

import javax.inject.Inject

class HabitValidator @Inject constructor() {

    // 습관 입력값 검증
    fun validate(
        title: String,
        description: String
    ): String? {

        if (title.isBlank()) {
            return "습관 이름을 입력해주세요."
        }

        if (title.length > 50) {
            return "습관 이름은 50자 이하로 입력해주세요."
        }

        if (description.length > 200) {
            return "습관 설명은 200자 이하로 입력해주세요."
        }

        // 문제가 없으면 null
        return null
    }
}