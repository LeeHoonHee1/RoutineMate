package com.example.routinemate.presentation.challenge.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.domain.repository.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeDetailViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(ChallengeDetailUiState())

    val uiState: StateFlow<ChallengeDetailUiState> =
        _uiState.asStateFlow()

    // 상세 조회
    fun loadChallengeDetail(
        challengeId: Long
    ) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                val detail =
                    challengeRepository.getChallengeDetail(
                        challengeId = challengeId
                    )

                _uiState.update {
                    it.copy(
                        detail = detail,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "챌린지 정보를 불러오지 못했습니다."
                    )
                }
            }
        }
    }

    // 오늘 완료
    fun completeChallenge(
        challengeId: Long
    ) {

        viewModelScope.launch {

            try {

                challengeRepository.completeChallenge(
                    challengeId = challengeId
                )

                _uiState.update {
                    it.copy(
                        successMessage =
                            "오늘 챌린지를 완료했습니다."
                    )
                }

                refreshDetail(
                    challengeId = challengeId
                )

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        errorMessage =
                            "챌린지 완료 처리에 실패했습니다."
                    )
                }
            }
        }
    }

    // 오늘 완료 취소
    fun cancelChallengeCompletion(
        challengeId: Long
    ) {

        viewModelScope.launch {

            try {

                challengeRepository
                    .cancelChallengeCompletion(
                        challengeId = challengeId
                    )

                _uiState.update {
                    it.copy(
                        successMessage =
                            "오늘 완료를 취소했습니다."
                    )
                }

                refreshDetail(
                    challengeId = challengeId
                )

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        errorMessage =
                            "완료 취소에 실패했습니다."
                    )
                }
            }
        }
    }

    // 상세 데이터 새로고침
    private suspend fun refreshDetail(
        challengeId: Long
    ) {

        try {

            val detail =
                challengeRepository.getChallengeDetail(
                    challengeId = challengeId
                )

            _uiState.update {
                it.copy(
                    detail = detail
                )
            }

        } catch (e: Exception) {

            _uiState.update {
                it.copy(
                    errorMessage =
                        "챌린지 정보를 새로고침하지 못했습니다."
                )
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