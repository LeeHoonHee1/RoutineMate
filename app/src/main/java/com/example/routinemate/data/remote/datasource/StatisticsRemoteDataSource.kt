package com.example.routinemate.data.remote.datasource

import com.example.routinemate.data.remote.api.StatisticsApi
import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse
import javax.inject.Inject

class StatisticsRemoteDataSource @Inject constructor(
    private val statisticsApi: StatisticsApi
) {

    // 최근 7일 습관 통계 조회
    suspend fun getWeeklyStatistics(): List<DailyHabitStatisticsResponse> {

        return statisticsApi.getWeeklyStatistics()
    }
}