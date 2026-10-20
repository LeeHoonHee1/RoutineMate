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

                // 챌린지별 초대 중인 친구
                val invitedMap =
                    mutableMapOf<Long, Set<Long>>()

                // 챌린지별 참여 중인 친구
                val joinedMap =
                    mutableMapOf<Long, Set<Long>>()

                // 내가 생성자인 챌린지
                val ownerIds =
                    mutableSetOf<Long>()

                challenges.forEach { challenge ->

                    /*
                     * OWNER인 경우에만 member-statuses 조회가 성공한다.
                     * 성공하면 INVITED + JOINED 상태를 모두 복원한다.
                     */
                    val memberStatuses =
                        runCatching {

                            challengeRepository
                                .getChallengeMemberStatuses(
                                    challengeId =
                                        challenge.id
                                )
                        }
                            .getOrNull()

                    if (
                        memberStatuses != null
                    ) {

                        // 조회 성공 = 내가 OWNER
                        ownerIds.add(
                            challenge.id
                        )

                        invitedMap[
                            challenge.id
                        ] =
                            memberStatuses
                                .filter { member ->
                                    member.status ==
                                            "INVITED"
                                }
                                .map { member ->
                                    member.userId
                                }
                                .toSet()

                        joinedMap[
                            challenge.id
                        ] =
                            memberStatuses
                                .filter { member ->
                                    member.status ==
                                            "JOINED"
                                }
                                .map { member ->
                                    member.userId
                                }
                                .toSet()

                    } else {

                        /*
                         * 내가 MEMBER인 챌린지는
                         * 기존 참여자 조회만 사용한다.
                         */
                        val members =
                            challengeRepository
                                .getChallengeMembers(
                                    challengeId =
                                        challenge.id
                                )

                        joinedMap[
                            challenge.id
                        ] =
                            members
                                .filter { member ->
                                    member.status ==
                                            "JOINED"
                                }
                                .map { member ->
                                    member.userId
                                }
                                .toSet()
                    }
                }

                _uiState.update {
                    it.copy(
                        challenges =
                            challenges,

                        invitations =
                            invitations,

                        friends =
                            friends,

                        invitedFriendIdsByChallenge =
                            invitedMap,

                        joinedFriendIdsByChallenge =
                            joinedMap,

                        ownerChallengeIds =
                            ownerIds,

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

        val state =
            _uiState.value

        // 현재 이 친구에게 요청 중이면 중복 클릭 방지
        if (
            state.invitingFriendId ==
            friendId
        ) {
            return
        }

        // 이미 해당 챌린지에 참여 중인 친구
        val joinedFriendIds =
            state.joinedFriendIdsByChallenge[
                challengeId
            ] ?: emptySet()

        if (
            friendId in joinedFriendIds
        ) {

            _uiState.update {
                it.copy(
                    errorMessage =
                        "이미 참여 중인 친구입니다."
                )
            }

            return
        }

        // 이미 초대한 친구
        val invitedFriendIds =
            state.invitedFriendIdsByChallenge[
                challengeId
            ] ?: emptySet()

        if (
            friendId in invitedFriendIds
        ) {

            _uiState.update {
                it.copy(
                    errorMessage =
                        "이미 초대한 친구입니다."
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    invitingFriendId =
                        friendId,
                    errorMessage =
                        null
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

                _uiState.update { currentState ->

                    val currentInvitedIds =
                        currentState
                            .invitedFriendIdsByChallenge[
                            challengeId
                        ]
                            ?: emptySet()

                    currentState.copy(
                        // 초대한 친구를 기억
                        invitedFriendIdsByChallenge =
                            currentState
                                .invitedFriendIdsByChallenge +
                                    (
                                            challengeId to
                                                    (
                                                            currentInvitedIds +
                                                                    friendId
                                                            )
                                            ),

                        invitingFriendId =
                            null,

                        successMessage =
                            "친구에게 챌린지 초대를 보냈습니다."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        invitingFriendId =
                            null,
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

        // 같은 초대를 처리 중이면 막기
        if (
            _uiState.value
                .processingInvitationId ==
            memberId
        ) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    processingInvitationId =
                        memberId,
                    errorMessage =
                        null
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

        // 같은 초대를 처리 중이면 막기
        if (
            _uiState.value
                .processingInvitationId ==
            memberId
        ) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    processingInvitationId =
                        memberId,
                    errorMessage =
                        null
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

            val invitedMap =
                mutableMapOf<Long, Set<Long>>()

            val joinedMap =
                mutableMapOf<Long, Set<Long>>()

            val ownerIds =
                mutableSetOf<Long>()

            challenges.forEach { challenge ->

                val memberStatuses =
                    runCatching {

                        challengeRepository
                            .getChallengeMemberStatuses(
                                challengeId =
                                    challenge.id
                            )
                    }
                        .getOrNull()

                if (
                    memberStatuses != null
                ) {

                    ownerIds.add(
                        challenge.id
                    )

                    invitedMap[
                        challenge.id
                    ] =
                        memberStatuses
                            .filter { member ->
                                member.status ==
                                        "INVITED"
                            }
                            .map { member ->
                                member.userId
                            }
                            .toSet()

                    joinedMap[
                        challenge.id
                    ] =
                        memberStatuses
                            .filter { member ->
                                member.status ==
                                        "JOINED"
                            }
                            .map { member ->
                                member.userId
                            }
                            .toSet()

                } else {

                    val members =
                        challengeRepository
                            .getChallengeMembers(
                                challengeId =
                                    challenge.id
                            )

                    joinedMap[
                        challenge.id
                    ] =
                        members
                            .filter { member ->
                                member.status ==
                                        "JOINED"
                            }
                            .map { member ->
                                member.userId
                            }
                            .toSet()
                }
            }

            _uiState.update {
                it.copy(
                    challenges =
                        challenges,

                    invitedFriendIdsByChallenge =
                        invitedMap,

                    joinedFriendIdsByChallenge =
                        joinedMap,

                    ownerChallengeIds =
                        ownerIds
                )
            }

        } catch (e: Exception) {

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