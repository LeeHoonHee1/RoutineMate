package com.example.routinemate.data.remote.dto.habit

import kotlinx.serialization.Serializable

@Serializable
data class HabitResponse(

    // 서버에서 생성된 습관 ID
    val id: Long,

    // 습관 이름
    val title: String,

    // 습관 설명
    val description: String?,

    // 활성화 여부
    val isActive: Boolean,

    // 서버에서 받은 생성 시간
    val createdAt: String
)