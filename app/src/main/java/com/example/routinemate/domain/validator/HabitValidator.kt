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

        if (title.length > TITLE_MAX_LENGTH) {
            return "습관 이름은 ${TITLE_MAX_LENGTH}자 이하로 입력해주세요."
        }

        if (description.length > DESCRIPTION_MAX_LENGTH) {
            return "습관 설명은 ${DESCRIPTION_MAX_LENGTH}자 이하로 입력해주세요."
        }

        // 문제가 없으면 null
        return null
    }

    companion object {

        const val TITLE_MAX_LENGTH = 50

        const val DESCRIPTION_MAX_LENGTH = 200
    }
}