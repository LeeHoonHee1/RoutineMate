package com.example.routinemate.presentation.challenge.detail

import com.example.routinemate.data.remote.dto.challenge.ChallengeDetailResponse

data class ChallengeDetailUiState(

    // 상세 데이터
    val detail: ChallengeDetailResponse? = null,

    // 로딩 상태
    val isLoading: Boolean = false,

    // 성공 메시지
    val successMessage: String? = null,

    // 에러 메시지
    val errorMessage: String? = null,

    // 나가기/삭제 처리 중 여부
    val isManagingChallenge: Boolean = false,

    // 나가기 또는 삭제 후 화면 종료 여부
    val shouldCloseScreen: Boolean = false,
)