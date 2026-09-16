package com.example.routinemate.domain.validator

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

class ChallengeValidator @Inject constructor() {

    fun validate(
        title: String,
        description: String,
        startDate: String,
        endDate: String
    ): String? {

        if (title.isBlank()) {
            return "챌린지 이름을 입력해주세요."
        }

        if (title.length > TITLE_MAX_LENGTH) {
            return "챌린지 이름은 ${TITLE_MAX_LENGTH}자 이하로 입력해주세요."
        }

        if (description.length > DESCRIPTION_MAX_LENGTH) {
            return "챌린지 설명은 ${DESCRIPTION_MAX_LENGTH}자 이하로 입력해주세요."
        }

        if (
            startDate.isBlank() ||
            endDate.isBlank()
        ) {
            return "시작일과 종료일을 입력해주세요."
        }

        val start =
            parseDate(startDate)
                ?: return "시작일을 YYYY-MM-DD 형식으로 입력해주세요."

        val end =
            parseDate(endDate)
                ?: return "종료일을 YYYY-MM-DD 형식으로 입력해주세요."

        if (end.isBefore(start)) {
            return "종료일은 시작일보다 빠를 수 없습니다."
        }

        return null
    }

    private fun parseDate(
        value: String
    ): LocalDate? {

        return try {

            LocalDate.parse(
                value,
                DateTimeFormatter.ISO_LOCAL_DATE
            )

        } catch (
            e: DateTimeParseException
        ) {

            null
        }
    }

    companion object {

        const val TITLE_MAX_LENGTH = 50

        const val DESCRIPTION_MAX_LENGTH = 200

        // 2026-09-15 = 10자
        const val DATE_MAX_LENGTH = 10
    }
}