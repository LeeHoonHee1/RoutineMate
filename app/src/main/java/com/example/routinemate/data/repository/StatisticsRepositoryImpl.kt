package com.example.routinemate.data.repository

import com.example.routinemate.data.remote.datasource.StatisticsRemoteDataSource
import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse
import com.example.routinemate.domain.repository.StatisticsRepository
import com.example.routinemate.data.remote.dto.statistics.CategoryHabitStatisticsResponse
import com.example.routinemate.data.remote.dto.statistics.StreakResponse
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private val remoteDataSource: StatisticsRemoteDataSource
) : StatisticsRepository {

    // 최근 7일 습관 통계 조회
    override suspend fun getWeeklyStatistics(): List<DailyHabitStatisticsResponse> {

        return remoteDataSource.getWeeklyStatistics()
    }

    // 최근 30일 습관 통계 조회
    override suspend fun getRecent30DayStatistics(): List<DailyHabitStatisticsResponse> {

        return remoteDataSource.getRecent30DayStatistics()
    }

    // 특정 월 습관 통계 조회
    override suspend fun getMonthlyStatistics(
        year: Int,
        month: Int
    ): List<DailyHabitStatisticsResponse> {

        return remoteDataSource.getMonthlyStatistics(
            year = year,
            month = month
        )
    }

    // 최근 30일 카테고리별 통계 조회
    override suspend fun getCategoryStatistics(): List<CategoryHabitStatisticsResponse> {

        return remoteDataSource.getCategoryStatistics()
    }

    // 연속 달성일 조회
    override suspend fun getStreakStatistics(): StreakResponse {

        return remoteDataSource
            .getStreakStatistics()
    }
}