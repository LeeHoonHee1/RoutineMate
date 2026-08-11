package com.example.routinemate.data.repository

import com.example.routinemate.data.remote.datasource.StatisticsRemoteDataSource
import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse
import com.example.routinemate.domain.repository.StatisticsRepository
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private val remoteDataSource: StatisticsRemoteDataSource
) : StatisticsRepository {

    // 최근 7일 습관 통계 조회
    override suspend fun getWeeklyStatistics(): List<DailyHabitStatisticsResponse> {

        return remoteDataSource.getWeeklyStatistics()
    }
}