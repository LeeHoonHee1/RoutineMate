package com.example.routinemate.core.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {

    // 세션 만료 여부
    private val _isSessionExpired =
        MutableStateFlow(false)

    val isSessionExpired: StateFlow<Boolean> =
        _isSessionExpired.asStateFlow()

    // Refresh Token까지 실패했을 때 호출
    fun expireSession() {
        _isSessionExpired.value = true
    }

    // 로그인 성공 후 세션 상태 초기화
    fun resetSession() {
        _isSessionExpired.value = false
    }
}