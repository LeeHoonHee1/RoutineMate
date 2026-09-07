package com.example.routinemate.presentation.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.data.remote.dto.challenge.CreateChallengeRequest
import com.example.routinemate.domain.repository.ChallengeRepository
import com.example.routinemate.domain.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val friendRepository: FriendRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(ChallengeUiState())

    val uiState: StateFlow<ChallengeUiState> =
        _uiState.asStateFlow()

    init {
        loadChallengeData()
    }

    // 챌린지 화면에 필요한 데이터 조회
    fun loadChallengeData() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                val challenges =
                    challengeRepository.getMyChallenges()

                val invitations =
                    challengeRepository.getMyInvitations()

                val friends =
                    friendRepository.getFriends()

                _uiState.update {
                    it.copy(
                        challenges = challenges,
                        invitations = invitations,
                        friends = friends,
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

    // 제목 변경
    fun onTitleChange(
        value: String
    ) {

        _uiState.update {
            it.copy(
                title = value
            )
        }
    }

    // 설명 변경
    fun onDescriptionChange(
        value: String
    ) {

        _uiState.update {
            it.copy(
                description = value
            )
        }
    }

    // 시작일 변경
    fun onStartDateChange(
        value: String
    ) {

        _uiState.update {
            it.copy(
                startDate = value
            )
        }
    }

    // 종료일 변경
    fun onEndDateChange(
        value: String
    ) {

        _uiState.update {
            it.copy(
                endDate = value
            )
        }
    }

    // 챌린지 생성
    fun createChallenge() {

        val state = _uiState.value

        if (state.title.isBlank()) {

            _uiState.update {
                it.copy(
                    errorMessage =
                        "챌린지 이름을 입력해주세요."
                )
            }

            return
        }

        if (
            state.startDate.isBlank() ||
            state.endDate.isBlank()
        ) {

            _uiState.update {
                it.copy(
                    errorMessage =
                        "시작일과 종료일을 입력해주세요."
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                challengeRepository.createChallenge(
                    CreateChallengeRequest(
                        title = state.title.trim(),
                        description =
                            state.description
                                .trim()
                                .takeIf {
                                    it.isNotBlank()
                                },
                        startDate =
                            state.startDate.trim(),
                        endDate =
                            state.endDate.trim()
                    )
                )

                _uiState.update {
                    it.copy(
                        title = "",
                        description = "",
                        startDate = "",
                        endDate = "",
                        successMessage =
                            "챌린지를 만들었습니다.",
                        isLoading = false
                    )
                }

                refreshChallenges()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "챌린지 생성에 실패했습니다."
                    )
                }
            }
        }
    }

    // 친구를 챌린지에 초대
    fun inviteFriend(
        challengeId: Long,
        friendId: Long
    ) {

        viewModelScope.launch {

            try {

                challengeRepository.inviteFriend(
                    challengeId = challengeId,
                    friendId = friendId
                )

                _uiState.update {
                    it.copy(
                        successMessage =
                            "친구에게 챌린지 초대를 보냈습니다."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        errorMessage =
                            "친구를 초대하지 못했습니다."
                    )
                }
            }
        }
    }

    // 챌린지 초대 수락
    fun acceptInvitation(
        memberId: Long
    ) {

        viewModelScope.launch {

            try {

                challengeRepository.acceptInvitation(
                    memberId = memberId
                )

                _uiState.update { state ->
                    state.copy(
                        invitations =
                            state.invitations.filter {
                                it.memberId != memberId
                            },
                        successMessage =
                            "챌린지 초대를 수락했습니다."
                    )
                }

                refreshChallenges()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        errorMessage =
                            "챌린지 초대를 수락하지 못했습니다."
                    )
                }
            }
        }
    }

    // 챌린지 초대 거절
    fun rejectInvitation(
        memberId: Long
    ) {

        viewModelScope.launch {

            try {

                challengeRepository.rejectInvitation(
                    memberId = memberId
                )

                _uiState.update { state ->
                    state.copy(
                        invitations =
                            state.invitations.filter {
                                it.memberId != memberId
                            },
                        successMessage =
                            "챌린지 초대를 거절했습니다."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        errorMessage =
                            "챌린지 초대를 거절하지 못했습니다."
                    )
                }
            }
        }
    }

    // 챌린지 목록 새로고침
    private suspend fun refreshChallenges() {

        try {

            val challenges =
                challengeRepository.getMyChallenges()

            _uiState.update {
                it.copy(
                    challenges = challenges
                )
            }

        } catch (e: Exception) {

            _uiState.update {
                it.copy(
                    errorMessage =
                        "챌린지 목록을 새로고침하지 못했습니다."
                )
            }
        }
    }

    // Snackbar 메시지 초기화
    fun clearMessage() {

        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null
            )
        }
    }
}