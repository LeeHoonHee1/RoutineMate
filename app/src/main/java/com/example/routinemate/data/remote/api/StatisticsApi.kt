package com.example.routinemate.data.remote.api

import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse
import retrofit2.http.GET

interface StatisticsApi {

    // 최근 7일 습관 통계 조회
    @GET("statistics/weekly")
    suspend fun getWeeklyStatistics(): List<DailyHabitStatisticsResponse>
}