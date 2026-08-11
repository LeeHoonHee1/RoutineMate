package com.example.routinemate.data.remote.dto.habit

import kotlinx.serialization.Serializable

@Serializable
data class UpdateHabitRequest(

    // 수정할 습관 이름
    val title: String,

    // 수정할 습관 설명
    val description: String? = null
)