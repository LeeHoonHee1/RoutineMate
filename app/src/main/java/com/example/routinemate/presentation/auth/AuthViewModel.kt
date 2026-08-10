package com.example.routinemate.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.core.session.SessionManager
import com.example.routinemate.data.local.datastore.TokenDataStore
import com.example.routinemate.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.routinemate.data.remote.error.ApiErrorParser
import retrofit2.HttpException
import com.example.routinemate.domain.validator.AuthValidator

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenDataStore: TokenDataStore,
    private val sessionManager: SessionManager,
    private val apiErrorParser: ApiErrorParser,
    private val authValidator: AuthValidator
) : ViewModel() {

    // 인증 화면 상태
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // 이메일 입력값 변경
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email
        )
    }

    // 비밀번호 입력값 변경
    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password
        )
    }

    // 닉네임 입력값 변경
    fun onNicknameChange(nickname: String) {
        _uiState.value = _uiState.value.copy(
            nickname = nickname
        )
    }

    // 서버에 로그인 요청
    fun login() {

        val email = _uiState.value.email.trim()
        val password = _uiState.value.password

        // 로그인 입력값 검증
        val validationMessage =
            authValidator.validateLogin(
                email = email,
                password = password
            )

        if (validationMessage != null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = validationMessage
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                isLoginSuccess = false
            )

            try {
                val response = authRepository.login(
                    email = email,
                    password = password
                )

                // 서버에서 받은 토큰 저장
                tokenDataStore.saveTokens(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken
                )

                // 이전 세션 만료 상태 초기화
                sessionManager.resetSession()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoginSuccess = true
                )

            } catch (e: Exception) {

                // 서버 에러 메시지가 있으면 우선 사용
                val message =
                    if (e is HttpException) {
                        apiErrorParser.parseMessage(e)
                            ?: "로그인에 실패했습니다."
                    } else {
                        "로그인에 실패했습니다."
                    }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isLoginSuccess = false,
                    errorMessage = message
                )
            }
        }
    }

    // 서버에 회원가입 요청
    fun signup() {

        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val nickname = _uiState.value.nickname.trim()

        // 회원가입 입력값 검증
        val validationMessage =
            authValidator.validateSignup(
                email = email,
                password = password,
                nickname = nickname
            )

        if (validationMessage != null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = validationMessage
            )
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                isSignupSuccess = false
            )

            try {
                authRepository.signup(
                    email = email,
                    password = password,
                    nickname = nickname
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSignupSuccess = true
                )

            } catch (e: Exception) {

                // 서버 에러 메시지가 있으면 우선 사용
                val message =
                    if (e is HttpException) {
                        apiErrorParser.parseMessage(e)
                            ?: "회원가입에 실패했습니다."
                    } else {
                        "회원가입에 실패했습니다."
                    }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSignupSuccess = false,
                    errorMessage = message
                )
            }
        }
    }

    // 현재 로그인한 사용자 정보 조회
    fun loadMe() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                // 저장된 Access Token이 Interceptor를 통해 자동으로 전달됨
                val user = authRepository.getMe()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentUser = user
                )

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentUser = null,
                    errorMessage = "사용자 정보를 불러오지 못했습니다."
                )
            }
        }
    }

    // 로그아웃
    fun logout() {
        viewModelScope.launch {

            // 저장된 토큰 삭제
            tokenDataStore.clearTokens()

            // 앱 전체에 세션 종료 알림
            sessionManager.expireSession()
        }
    }
}