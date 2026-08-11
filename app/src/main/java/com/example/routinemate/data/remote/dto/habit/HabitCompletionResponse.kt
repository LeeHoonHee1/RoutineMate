package com.example.routinemate.data.remote.dto.habit

import kotlinx.serialization.Serializable

@Serializable
data class HabitCompletionResponse(

    // 완료 기록 ID
    val id: Long,

    // 완료된 Habit ID
    val habitId: Long,

    // 완료 날짜
    val completedDate: String,

    // 완료 기록 생성 시간
    val createdAt: String
)