package com.example.routinemate.presentation.auth

import com.example.routinemate.data.remote.dto.UserResponse

data class AuthUiState(
    val email: String = "",
    val password: String = "",

    // 회원가입에서 사용할 닉네임
    val nickname: String = "",

    // 로그인 또는 회원가입 요청 진행 여부
    val isLoading: Boolean = false,

    // 인증 관련 실패 메시지
    val errorMessage: String? = null,

    // 로그인 성공 여부
    val isLoginSuccess: Boolean = false,

    // 회원가입 성공 여부
    val isSignupSuccess: Boolean = false,

    // 현재 로그인한 사용자 정보
    val currentUser: UserResponse? = null
)