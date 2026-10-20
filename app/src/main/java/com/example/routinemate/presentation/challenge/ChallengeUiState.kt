package com.example.routinemate.presentation.challenge

import com.example.routinemate.data.remote.dto.challenge.ChallengeInvitationResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeResponse
import com.example.routinemate.data.remote.dto.friend.FriendUserResponse

data class ChallengeUiState(

    // 내가 참여 중인 챌린지
    val challenges: List<ChallengeResponse> = emptyList(),

    // 내가 받은 챌린지 초대
    val invitations: List<ChallengeInvitationResponse> = emptyList(),

    // 챌린지에 초대할 수 있는 내 친구
    val friends: List<FriendUserResponse> = emptyList(),

    // 챌린지별 초대한 친구 ID
    val invitedFriendIdsByChallenge:
    Map<Long, Set<Long>> = emptyMap(),

    // 챌린지별 현재 참여 중인 친구 ID
    val joinedFriendIdsByChallenge:
    Map<Long, Set<Long>> = emptyMap(),

    // 챌린지 생성 입력값
    val title: String = "",
    val description: String = "",
    val startDate: String = "",
    val endDate: String = "",

    // 화면 데이터 로딩
    val isLoading: Boolean = false,

    // 챌린지 생성 요청 중
    val isSubmitting: Boolean = false,

    // 현재 초대 요청 중인 친구 ID
    val invitingFriendId: Long? = null,

    // 현재 수락/거절 처리 중인 초대 ID
    val processingInvitationId: Long? = null,

    // 입력값 검증 오류
    val inputErrorMessage: String? = null,

    // 성공 메시지
    val successMessage: String? = null,

    // 서버 / 네트워크 오류
    val errorMessage: String? = null,

    // 내가 생성자인 챌린지 ID
    val ownerChallengeIds: Set<Long> = emptySet(),
)