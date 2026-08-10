package com.example.routinemate.presentation.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.core.session.SessionManager
import com.example.routinemate.data.local.datastore.TokenDataStore
import com.example.routinemate.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    sessionManager: SessionManager,
    private val tokenDataStore: TokenDataStore,
    private val authRepository: AuthRepository
) : ViewModel() {

    // 세션 만료 상태
    val isSessionExpired: StateFlow<Boolean> =
        sessionManager.isSessionExpired

    // 앱 시작 시 인증 확인 완료 여부
    private val _isAuthChecked =
        MutableStateFlow(false)

    val isAuthChecked: StateFlow<Boolean> =
        _isAuthChecked.asStateFlow()

    // 실제 로그인 가능 상태
    private val _isLoggedIn =
        MutableStateFlow(false)

    val isLoggedIn: StateFlow<Boolean> =
        _isLoggedIn.asStateFlow()

    init {
        checkLoginState()
    }

    // 앱 시작 시 저장된 토큰과 서버 인증 상태 확인
    private fun checkLoginState() {
        viewModelScope.launch {

            val accessToken =
                tokenDataStore.accessToken.first()

            // 저장된 토큰 자체가 없으면 로그인 필요
            if (accessToken.isNullOrBlank()) {
                _isLoggedIn.value = false
                _isAuthChecked.value = true
                return@launch
            }

            try {
                // 실제 보호 API 호출
                // Access Token 만료 시 Authenticator가 자동 Refresh 시도
                authRepository.getMe()

                // 인증 성공
                _isLoggedIn.value = true

            } catch (e: Exception) {

                // Access/Refresh Token 모두 사용할 수 없는 상태
                tokenDataStore.clearTokens()
                _isLoggedIn.value = false
            }

            _isAuthChecked.value = true
        }
    }
}