package com.example.routinemate.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(ProfileUiState())

    val uiState: StateFlow<ProfileUiState> =
        _uiState.asStateFlow()

    init {
        loadProfile()
    }

    // 프로필 + 활동 요약 조회
    fun loadProfile() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                // 두 API를 동시에 요청
                val profileDeferred =
                    async {
                        profileRepository
                            .getMyProfile()
                    }

                val summaryDeferred =
                    async {
                        profileRepository
                            .getMyProfileSummary()
                    }

                val profile =
                    profileDeferred.await()

                val summary =
                    summaryDeferred.await()

                _uiState.update {
                    it.copy(
                        profile = profile,
                        summary = summary,
                        nicknameInput =
                            profile.nickname,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "프로필 정보를 불러오지 못했습니다."
                    )
                }
            }
        }
    }

    // 닉네임 입력 변경
    fun updateNicknameInput(
        nickname: String
    ) {

        _uiState.update {
            it.copy(
                // 최대 20자까지만 입력
                nicknameInput =
                    nickname.take(20),

                // 다시 입력하면 기존 검증 오류 제거
                nicknameErrorMessage =
                    null
            )
        }
    }

    // 닉네임 수정 모드 시작
    fun startNicknameEdit() {

        val currentNickname =
            _uiState.value.profile?.nickname
                ?: return

        _uiState.update {
            it.copy(
                nicknameInput =
                    currentNickname,
                isEditingNickname =
                    true,
                nicknameErrorMessage =
                    null
            )
        }
    }

    // 닉네임 수정 취소
    fun cancelNicknameEdit() {

        // 저장 중에는 취소 방지
        if (
            _uiState.value.isSavingNickname
        ) {
            return
        }

        val currentNickname =
            _uiState.value.profile?.nickname
                ?: ""

        _uiState.update {
            it.copy(
                nicknameInput =
                    currentNickname,
                isEditingNickname =
                    false,
                nicknameErrorMessage =
                    null
            )
        }
    }

    // 닉네임 저장
    fun saveNickname() {

        // 저장 버튼 연타 방지
        if (
            _uiState.value.isSavingNickname
        ) {
            return
        }

        val nickname =
            _uiState.value
                .nicknameInput
                .trim()

        // 공백 검사
        if (
            nickname.isBlank()
        ) {

            _uiState.update {
                it.copy(
                    nicknameErrorMessage =
                        "닉네임을 입력해주세요."
                )
            }

            return
        }

        // 길이 검사
        if (
            nickname.length > 20
        ) {

            _uiState.update {
                it.copy(
                    nicknameErrorMessage =
                        "닉네임은 20자 이하로 입력해주세요."
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSavingNickname =
                        true,
                    nicknameErrorMessage =
                        null,
                    errorMessage =
                        null
                )
            }

            try {

                val updatedProfile =
                    profileRepository
                        .updateMyProfile(
                            nickname =
                                nickname
                        )

                _uiState.update {
                    it.copy(
                        profile =
                            updatedProfile,

                        nicknameInput =
                            updatedProfile.nickname,

                        isEditingNickname =
                            false,

                        isSavingNickname =
                            false,

                        successMessage =
                            "닉네임을 수정했습니다."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isSavingNickname =
                            false,

                        errorMessage =
                            "닉네임 수정에 실패했습니다."
                    )
                }
            }
        }
    }

    // 실제 프로필 이미지 파일 업로드
    fun uploadProfileImage(
        file: MultipartBody.Part
    ) {

        // 업로드 중 연타 방지
        if (
            _uiState.value.isSavingProfileImage
        ) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSavingProfileImage =
                        true,
                    errorMessage =
                        null
                )
            }

            try {

                val updatedProfile =
                    profileRepository
                        .uploadProfileImage(
                            file = file
                        )

                _uiState.update {
                    it.copy(
                        profile =
                            updatedProfile,

                        isSavingProfileImage =
                            false,

                        successMessage =
                            "프로필 이미지를 변경했습니다."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isSavingProfileImage =
                            false,

                        errorMessage =
                            "프로필 이미지 업로드에 실패했습니다."
                    )
                }
            }
        }
    }

    // 메시지 초기화
    fun clearMessage() {

        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null
            )
        }
    }
}