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

    // 화면 데이터 조회 로딩
    val isLoading: Boolean = false,

    // 생성 / 수정 요청 중
    val isSubmitting: Boolean = false,

    // 서버 / 네트워크 오류
    val errorMessage: String? = null,

    // 입력값 검증 오류
    val inputErrorMessage: String? = null,

    // 기존 값 유지
    val isCreateSuccess: Boolean = false,

    // 생성 / 수정 성공 여부
    val isSaveSuccess: Boolean = false
)