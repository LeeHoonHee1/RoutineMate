package com.example.routinemate.domain.repository

import com.example.routinemate.data.remote.dto.challenge.ChallengeInvitationResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeMemberResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeResponse
import com.example.routinemate.data.remote.dto.challenge.CreateChallengeRequest

interface ChallengeRepository {

    // 챌린지 생성
    suspend fun createChallenge(
        request: CreateChallengeRequest
    ): ChallengeResponse

    // 내가 참여 중인 챌린지 조회
    suspend fun getMyChallenges():
            List<ChallengeResponse>

    // 친구 초대
    suspend fun inviteFriend(
        challengeId: Long,
        friendId: Long
    ): ChallengeMemberResponse

    // 내가 받은 초대 조회
    suspend fun getMyInvitations():
            List<ChallengeInvitationResponse>

    // 초대 수락
    suspend fun acceptInvitation(
        memberId: Long
    ): ChallengeMemberResponse

    // 초대 거절
    suspend fun rejectInvitation(
        memberId: Long
    ): ChallengeMemberResponse

    // 챌린지 참여자 조회
    suspend fun getChallengeMembers(
        challengeId: Long
    ): List<ChallengeMemberResponse>
}