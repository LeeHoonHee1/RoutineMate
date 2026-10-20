package com.example.routinemate.data.remote.dto.statistics

import com.example.routinemate.data.remote.dto.habit.HabitCategory
import kotlinx.serialization.Serializable

@Serializable
data class CategoryHabitStatisticsResponse(

    // 습관 카테고리
    val category: HabitCategory,

    // 최근 30일 예정 횟수
    val totalScheduledCount: Int,

    // 최근 30일 완료 횟수
    val completedCount: Int,

    // 최근 30일 달성률
    val completionRate: Int
)