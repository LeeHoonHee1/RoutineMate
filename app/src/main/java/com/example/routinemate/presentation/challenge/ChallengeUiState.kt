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

    // 챌린지 생성 입력값
    val title: String = "",
    val description: String = "",
    val startDate: String = "",
    val endDate: String = "",

    // 로딩 상태
    val isLoading: Boolean = false,

    // 성공 메시지
    val successMessage: String? = null,

    // 에러 메시지
    val errorMessage: String? = null
)