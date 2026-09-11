package com.example.routinemate.presentation.profile

import com.example.routinemate.data.remote.dto.profile.ProfileResponse
import com.example.routinemate.data.remote.dto.profile.ProfileSummaryResponse

data class ProfileUiState(

    // 내 프로필 정보
    val profile: ProfileResponse? = null,

    // 활동 요약
    val summary: ProfileSummaryResponse? = null,

    // 수정 중인 닉네임
    val nicknameInput: String = "",

    // 프로필 수정 모드
    val isEditingNickname: Boolean = false,

    // 로딩 상태
    val isLoading: Boolean = false,

    // 성공 메시지
    val successMessage: String? = null,

    // 에러 메시지
    val errorMessage: String? = null
)