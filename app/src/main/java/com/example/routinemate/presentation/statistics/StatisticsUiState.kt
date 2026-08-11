package com.example.routinemate.presentation.statistics

import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse

data class StatisticsUiState(

    // 최근 7일 통계 목록
    val weeklyStatistics: List<DailyHabitStatisticsResponse> = emptyList(),

    // 최근 7일 평균 달성률
    val averageCompletionRate: Int = 0,

    // 최근 7일 총 완료 횟수
    val totalCompletedCount: Int = 0,

    // 최근 7일 중 가장 높은 달성률을 기록한 날짜
    val bestDay: String? = null,

    // 데이터 로딩 여부
    val isLoading: Boolean = false,

    // 오류 메시지
    val errorMessage: String? = null
)