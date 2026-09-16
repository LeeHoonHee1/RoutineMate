package com.example.routinemate.presentation.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.data.remote.dto.challenge.CreateChallengeRequest
import com.example.routinemate.domain.repository.ChallengeRepository
import com.example.routinemate.domain.repository.FriendRepository
import com.example.routinemate.domain.validator.ChallengeValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val friendRepository: FriendRepository,
    private val challengeValidator: ChallengeValidator
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            ChallengeUiState()
        )

    val uiState: StateFlow<ChallengeUiState> =
        _uiState.asStateFlow()

    init {
        loadChallengeData()
    }

    // 챌린지 화면 데이터 조회
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
                    challengeRepository
                        .getMyChallenges()

                val invitations =
                    challengeRepository
                        .getMyInvitations()

                val friends =
                    friendRepository
                        .getFriends()

                _uiState.update {
                    it.copy(
                        challenges =
                            challenges,
                        invitations =
                            invitations,
                        friends =
                            friends,
                        isLoading =
                            false
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
                title =
                    value.take(
                        ChallengeValidator
                            .TITLE_MAX_LENGTH
                    ),
                inputErrorMessage =
                    null
            )
        }
    }

    // 설명 변경
    fun onDescriptionChange(
        value: String
    ) {

        _uiState.update {
            it.copy(
                description =
                    value.take(
                        ChallengeValidator
                            .DESCRIPTION_MAX_LENGTH
                    ),
                inputErrorMessage =
                    null
            )
        }
    }

    // 시작일 변경
    fun onStartDateChange(
        value: String
    ) {

        _uiState.update {
            it.copy(
                startDate =
                    value.take(
                        ChallengeValidator
                            .DATE_MAX_LENGTH
                    ),
                inputErrorMessage =
                    null
            )
        }
    }

    // 종료일 변경
    fun onEndDateChange(
        value: String
    ) {

        _uiState.update {
            it.copy(
                endDate =
                    value.take(
                        ChallengeValidator
                            .DATE_MAX_LENGTH
                    ),
                inputErrorMessage =
                    null
            )
        }
    }

    // 챌린지 생성
    fun createChallenge() {

        // 저장 버튼 연타 방지
        if (
            _uiState.value.isSubmitting
        ) {
            return
        }

        val state =
            _uiState.value

        val title =
            state.title.trim()

        val description =
            state.description.trim()

        val startDate =
            state.startDate.trim()

        val endDate =
            state.endDate.trim()

        val validationMessage =
            challengeValidator.validate(
                title = title,
                description = description,
                startDate = startDate,
                endDate = endDate
            )

        // 입력값 검증 실패
        if (
            validationMessage != null
        ) {

            _uiState.update {
                it.copy(
                    inputErrorMessage =
                        validationMessage
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    inputErrorMessage = null,
                    errorMessage = null
                )
            }

            try {

                challengeRepository
                    .createChallenge(
                        CreateChallengeRequest(
                            title = title,

                            description =
                                description.takeIf {
                                    it.isNotBlank()
                                },

                            startDate =
                                startDate,

                            endDate =
                                endDate
                        )
                    )

                /*
                 * 생성 자체는 성공했으므로
                 * 입력값을 먼저 초기화한다.
                 */
                _uiState.update {
                    it.copy(
                        title = "",
                        description = "",
                        startDate = "",
                        endDate = "",
                        isSubmitting = false,
                        successMessage =
                            "챌린지를 만들었습니다."
                    )
                }

                // 최신 목록 조회
                refreshChallenges()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
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

        // 같은 친구에게 초대 요청 중이면 중복 방지
        if (
            _uiState.value.invitingFriendId ==
            friendId
        ) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    invitingFriendId = friendId,
                    errorMessage = null
                )
            }

            try {

                challengeRepository
                    .inviteFriend(
                        challengeId =
                            challengeId,
                        friendId =
                            friendId
                    )

                _uiState.update {
                    it.copy(
                        invitingFriendId = null,
                        successMessage =
                            "친구에게 챌린지 초대를 보냈습니다."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        invitingFriendId = null,
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

        // 이미 처리 중인 초대면 중복 방지
        if (
            _uiState.value.processingInvitationId ==
            memberId
        ) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    processingInvitationId =
                        memberId,
                    errorMessage = null
                )
            }

            try {

                challengeRepository
                    .acceptInvitation(
                        memberId =
                            memberId
                    )

                _uiState.update { state ->

                    state.copy(
                        invitations =
                            state.invitations
                                .filter {
                                    it.memberId !=
                                            memberId
                                },
                        processingInvitationId =
                            null,
                        successMessage =
                            "챌린지 초대를 수락했습니다."
                    )
                }

                refreshChallenges()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        processingInvitationId =
                            null,
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

        // 이미 처리 중인 초대면 중복 방지
        if (
            _uiState.value.processingInvitationId ==
            memberId
        ) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    processingInvitationId =
                        memberId,
                    errorMessage = null
                )
            }

            try {

                challengeRepository
                    .rejectInvitation(
                        memberId =
                            memberId
                    )

                _uiState.update { state ->

                    state.copy(
                        invitations =
                            state.invitations
                                .filter {
                                    it.memberId !=
                                            memberId
                                },
                        processingInvitationId =
                            null,
                        successMessage =
                            "챌린지 초대를 거절했습니다."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        processingInvitationId =
                            null,
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
                challengeRepository
                    .getMyChallenges()

            _uiState.update {
                it.copy(
                    challenges =
                        challenges
                )
            }

        } catch (e: Exception) {

            /*
             * 챌린지 생성 자체는 이미 성공했을 수 있으므로
             * 생성 실패라고 표시하면 안 된다.
             */
            _uiState.update {
                it.copy(
                    errorMessage =
                        "변경사항은 저장됐지만 챌린지 목록을 새로고침하지 못했습니다."
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