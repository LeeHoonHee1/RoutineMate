package com.example.routinemate.data.remote.dto.statistics

import kotlinx.serialization.Serializable

@Serializable
data class DailyHabitStatisticsResponse(

    // 통계 날짜
    val date: String,

    // 해당 날짜에 완료한 습관 수
    val completedHabitCount: Int,

    // 해당 날짜의 전체 습관 수
    val totalHabitCount: Int,

    // 해당 날짜의 달성률 (0 ~ 100)
    val completionRate: Int
)