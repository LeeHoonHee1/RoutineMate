package com.example.routinemate.data.remote.dto.habit

import kotlinx.serialization.Serializable

@Serializable
data class CreateHabitRequest(

    // 생성할 습관 이름
    val title: String,

    // 습관 설명은 선택
    val description: String? = null,

    // 습관 카테고리
    val category: HabitCategory,

    // 반복 요일
    val repeatDays: Set<HabitRepeatDay>
)