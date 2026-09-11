package com.example.routinemate.data.remote.datasource

import com.example.routinemate.data.remote.api.ChallengeApi
import com.example.routinemate.data.remote.dto.challenge.ChallengeDetailResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeInvitationResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeMemberResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeParticipantProgressResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeProgressResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeRecordResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeResponse
import com.example.routinemate.data.remote.dto.challenge.CreateChallengeRequest
import javax.inject.Inject

class ChallengeRemoteDataSource @Inject constructor(
    private val challengeApi: ChallengeApi
) {

    suspend fun createChallenge(
        request: CreateChallengeRequest
    ): ChallengeResponse {

        return challengeApi.createChallenge(
            request = request
        )
    }

    suspend fun getMyChallenges():
            List<ChallengeResponse> {

        return challengeApi.getMyChallenges()
    }

    suspend fun inviteFriend(
        challengeId: Long,
        friendId: Long
    ): ChallengeMemberResponse {

        return challengeApi.inviteFriend(
            challengeId = challengeId,
            friendId = friendId
        )
    }

    suspend fun getMyInvitations():
            List<ChallengeInvitationResponse> {

        return challengeApi.getMyInvitations()
    }

    suspend fun acceptInvitation(
        memberId: Long
    ): ChallengeMemberResponse {

        return challengeApi.acceptInvitation(
            memberId = memberId
        )
    }

    suspend fun rejectInvitation(
        memberId: Long
    ): ChallengeMemberResponse {

        return challengeApi.rejectInvitation(
            memberId = memberId
        )
    }

    suspend fun getChallengeMembers(
        challengeId: Long
    ): List<ChallengeMemberResponse> {

        return challengeApi.getChallengeMembers(
            challengeId = challengeId
        )
    }

    // 내 진행률
    suspend fun getMyProgress(
        challengeId: Long
    ): ChallengeProgressResponse {

        return challengeApi.getMyProgress(
            challengeId = challengeId
        )
    }

    // 참여자 진행 현황
    suspend fun getParticipantProgress(
        challengeId: Long
    ): List<ChallengeParticipantProgressResponse> {

        return challengeApi.getParticipantProgress(
            challengeId = challengeId
        )
    }

    // 챌린지 상세
    suspend fun getChallengeDetail(
        challengeId: Long
    ): ChallengeDetailResponse {

        return challengeApi.getChallengeDetail(
            challengeId = challengeId
        )
    }

    // 오늘 완료
    suspend fun completeChallenge(
        challengeId: Long
    ): ChallengeRecordResponse {

        return challengeApi.completeChallenge(
            challengeId = challengeId
        )
    }

    // 오늘 완료 취소
    suspend fun cancelChallengeCompletion(
        challengeId: Long
    ) {

        challengeApi.cancelChallengeCompletion(
            challengeId = challengeId
        )
    }
}