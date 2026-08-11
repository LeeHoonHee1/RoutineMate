package com.example.routinemate.presentation.habit

import com.example.routinemate.data.remote.dto.habit.HabitResponse

data class HabitUiState(

    // 서버에서 받아온 습관 목록
    val habits: List<HabitResponse> = emptyList(),

    // 습관 이름 입력값
    val title: String = "",

    // 습관 설명 입력값
    val description: String = "",

    // 현재 수정 중인 습관 ID
    val editingHabitId: Long? = null,

    // 네트워크 요청 진행 여부
    val isLoading: Boolean = false,

    // 사용자에게 보여줄 오류 메시지
    val errorMessage: String? = null,

    // 습관 생성 성공 여부
    val isCreateSuccess: Boolean = false
)