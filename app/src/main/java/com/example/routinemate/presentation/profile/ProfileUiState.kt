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

    // 화면 로딩 상태
    val isLoading: Boolean = false,

    // 닉네임 저장 중
    val isSavingNickname: Boolean = false,

    // 닉네임 입력 검증 오류
    val nicknameErrorMessage: String? = null,

    // 성공 메시지
    val successMessage: String? = null,

    // 서버 / 네트워크 오류
    val errorMessage: String? = null,

    // 프로필 이미지 주소
    val profileImageUrl: String? = null,

    // 프로필 이미지 저장 중 여부
    val isSavingProfileImage: Boolean = false,
)