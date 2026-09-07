package com.example.routinemate.data.remote.datasource

import com.example.routinemate.data.remote.api.ChallengeApi
import com.example.routinemate.data.remote.dto.challenge.ChallengeInvitationResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeMemberResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeResponse
import com.example.routinemate.data.remote.dto.challenge.CreateChallengeRequest
import javax.inject.Inject

class ChallengeRemoteDataSource @Inject constructor(
    private val challengeApi: ChallengeApi
) {

    // 챌린지 생성
    suspend fun createChallenge(
        request: CreateChallengeRequest
    ): ChallengeResponse {

        return challengeApi.createChallenge(
            request = request
        )
    }

    // 내가 참여 중인 챌린지 조회
    suspend fun getMyChallenges():
            List<ChallengeResponse> {

        return challengeApi.getMyChallenges()
    }

    // 친구 초대
    suspend fun inviteFriend(
        challengeId: Long,
        friendId: Long
    ): ChallengeMemberResponse {

        return challengeApi.inviteFriend(
            challengeId = challengeId,
            friendId = friendId
        )
    }

    // 내가 받은 챌린지 초대
    suspend fun getMyInvitations():
            List<ChallengeInvitationResponse> {

        return challengeApi.getMyInvitations()
    }

    // 초대 수락
    suspend fun acceptInvitation(
        memberId: Long
    ): ChallengeMemberResponse {

        return challengeApi.acceptInvitation(
            memberId = memberId
        )
    }

    // 초대 거절
    suspend fun rejectInvitation(
        memberId: Long
    ): ChallengeMemberResponse {

        return challengeApi.rejectInvitation(
            memberId = memberId
        )
    }

    // 챌린지 참여자 목록
    suspend fun getChallengeMembers(
        challengeId: Long
    ): List<ChallengeMemberResponse> {

        return challengeApi.getChallengeMembers(
            challengeId = challengeId
        )
    }
}