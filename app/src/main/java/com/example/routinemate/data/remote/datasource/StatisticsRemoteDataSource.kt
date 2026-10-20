package com.example.routinemate.data.remote.datasource

import com.example.routinemate.data.remote.api.StatisticsApi
import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse
import com.example.routinemate.data.remote.dto.statistics.CategoryHabitStatisticsResponse
import com.example.routinemate.data.remote.dto.statistics.StreakResponse
import javax.inject.Inject

class StatisticsRemoteDataSource @Inject constructor(
    private val statisticsApi: StatisticsApi
) {

    // 최근 7일 습관 통계 조회
    suspend fun getWeeklyStatistics(): List<DailyHabitStatisticsResponse> {

        return statisticsApi.getWeeklyStatistics()
    }

    // 최근 30일 습관 통계 조회
    suspend fun getRecent30DayStatistics(): List<DailyHabitStatisticsResponse> {

        return statisticsApi.getRecent30DayStatistics()
    }

    // 특정 월 습관 통계 조회
    suspend fun getMonthlyStatistics(
        year: Int,
        month: Int
    ): List<DailyHabitStatisticsResponse> {

        return statisticsApi.getMonthlyStatistics(
            year = year,
            month = month
        )
    }

    // 최근 30일 카테고리별 통계 조회
    suspend fun getCategoryStatistics(): List<CategoryHabitStatisticsResponse> {

        return statisticsApi.getCategoryStatistics()
    }

    // 연속 달성일 조회
    suspend fun getStreakStatistics(): StreakResponse {

        return statisticsApi
            .getStreakStatistics()
    }
}