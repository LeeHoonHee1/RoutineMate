package com.example.routinemate.presentation.habit

import com.example.routinemate.data.remote.dto.habit.HabitResponse

data class HabitUiState(

    val habits: List<HabitResponse> = emptyList(),

    val title: String = "",

    val description: String = "",

    val editingHabitId: Long? = null,

    // 오늘 전체 습관 수
    val totalHabitCount: Int = 0,

    // 오늘 완료한 습관 수
    val completedHabitCount: Int = 0,

    // 오늘 습관 달성률
    val completionRate: Int = 0,

    val isLoading: Boolean = false,

    val errorMessage: String? = null,

    val isCreateSuccess: Boolean = false
)