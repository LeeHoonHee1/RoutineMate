package com.example.routinemate.domain.repository

import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse
import com.example.routinemate.data.remote.dto.statistics.CategoryHabitStatisticsResponse
import com.example.routinemate.data.remote.dto.statistics.StreakResponse

interface StatisticsRepository {

    // 최근 7일 습관 통계 조회
    suspend fun getWeeklyStatistics(): List<DailyHabitStatisticsResponse>

    // 최근 30일 습관 통계 조회
    suspend fun getRecent30DayStatistics(): List<DailyHabitStatisticsResponse>

    // 특정 월 습관 통계 조회
    suspend fun getMonthlyStatistics(
        year: Int,
        month: Int
    ): List<DailyHabitStatisticsResponse>

    // 최근 30일 카테고리별 통계 조회
    suspend fun getCategoryStatistics(): List<CategoryHabitStatisticsResponse>

    // 연속 달성일 조회
    suspend fun getStreakStatistics(): StreakResponse
}