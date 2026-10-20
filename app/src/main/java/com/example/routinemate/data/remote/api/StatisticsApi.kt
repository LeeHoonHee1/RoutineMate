package com.example.routinemate.data.remote.api

import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse
import com.example.routinemate.data.remote.dto.statistics.CategoryHabitStatisticsResponse
import com.example.routinemate.data.remote.dto.statistics.StreakResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StatisticsApi {

    // 최근 7일 습관 통계 조회
    @GET("statistics/weekly")
    suspend fun getWeeklyStatistics(): List<DailyHabitStatisticsResponse>

    // 최근 30일 습관 통계 조회
    @GET("statistics/30-days")
    suspend fun getRecent30DayStatistics(): List<DailyHabitStatisticsResponse>

    // 특정 월 습관 통계 조회
    @GET("statistics/monthly")
    suspend fun getMonthlyStatistics(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): List<DailyHabitStatisticsResponse>

    // 최근 30일 카테고리별 통계 조회
    @GET("statistics/categories")
    suspend fun getCategoryStatistics(): List<CategoryHabitStatisticsResponse>

    // 연속 달성일 조회
    @GET("statistics/streak")
    suspend fun getStreakStatistics(): StreakResponse
}