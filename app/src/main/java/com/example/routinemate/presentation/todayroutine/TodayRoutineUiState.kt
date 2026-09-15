package com.example.routinemate.presentation.todayroutine

import com.example.routinemate.data.remote.dto.habit.HabitResponse

data class TodayRoutineUiState(

    // 오늘 해야 하는 습관
    val habits: List<HabitResponse> = emptyList(),

    // 오늘 전체 습관 수
    val totalCount: Int = 0,

    // 오늘 완료한 습관 수
    val completedCount: Int = 0,

    // 오늘 달성률
    val completionRate: Int = 0,

    // 로딩 여부
    val isLoading: Boolean = false,

    // 오류 메시지
    val errorMessage: String? = null
)