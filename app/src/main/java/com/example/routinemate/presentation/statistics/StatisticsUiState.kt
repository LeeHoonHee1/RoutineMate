package com.example.routinemate.presentation.statistics

import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse
import com.example.routinemate.data.remote.dto.statistics.CategoryHabitStatisticsResponse

data class StatisticsUiState(

    // 현재 선택한 통계 기간
    val selectedPeriod: StatisticsPeriod =
        StatisticsPeriod.WEEKLY,

    // 현재 화면에 표시할 통계 목록
    val statistics:
    List<DailyHabitStatisticsResponse> =
        emptyList(),

    // 선택 기간 평균 달성률
    val averageCompletionRate: Int = 0,

    // 선택 기간 총 완료 횟수
    val totalCompletedCount: Int = 0,

    // 선택 기간 중 가장 높은 달성률 날짜
    val bestDay: String? = null,

    // 현재 연속 달성일
    val currentStreak: Int = 0,

    // 최고 연속 달성일
    val longestStreak: Int = 0,

    // 데이터 로딩 여부
    val isLoading: Boolean = false,

    // 오류 메시지
    val errorMessage: String? = null,

    // 최근 30일 카테고리별 통계
    val categoryStatistics:
    List<CategoryHabitStatisticsResponse> =
        emptyList(),
)