package com.example.routinemate.presentation.home

import com.example.routinemate.data.remote.dto.habit.HabitResponse
import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse

data class HomeUiState(

    // 오늘의 습관 목록
    val todayHabits: List<HabitResponse> = emptyList(),

    // 오늘 전체 습관 수
    val totalHabitCount: Int = 0,

    // 오늘 완료한 습관 수
    val completedHabitCount: Int = 0,

    // 오늘 달성률
    val todayCompletionRate: Int = 0,

    // 최근 7일 통계
    val weeklyStatistics: List<DailyHabitStatisticsResponse> = emptyList(),

    // 최근 7일 평균 달성률
    val weeklyAverageCompletionRate: Int = 0,

    // 최근 7일 총 완료 횟수
    val weeklyTotalCompletedCount: Int = 0,

    // 최근 7일 중 가장 잘한 날
    val bestDay: String? = null,

    // 데이터 로딩 여부
    val isLoading: Boolean = false,

    // 오류 메시지
    val errorMessage: String? = null
)