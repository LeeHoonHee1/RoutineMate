package com.example.routinemate.data.remote.dto.statistics

import kotlinx.serialization.Serializable

@Serializable
data class StreakResponse(

    // 현재 연속 달성일
    val currentStreak: Int,

    // 최고 연속 달성일
    val longestStreak: Int
)