package com.example.routinemate.domain.repository

import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse

interface StatisticsRepository {

    // 최근 7일 습관 통계 조회
    suspend fun getWeeklyStatistics(): List<DailyHabitStatisticsResponse>
}