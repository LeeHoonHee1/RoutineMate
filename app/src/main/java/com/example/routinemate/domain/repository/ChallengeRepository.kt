package com.example.routinemate.domain.repository

import com.example.routinemate.data.remote.dto.challenge.ChallengeDetailResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeInvitationResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeMemberResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeParticipantProgressResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeProgressResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeRecordResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeResponse
import com.example.routinemate.data.remote.dto.challenge.CreateChallengeRequest
import com.example.routinemate.data.remote.dto.challenge.HomeChallengeResponse

interface ChallengeRepository {

    suspend fun createChallenge(
        request: CreateChallengeRequest
    ): ChallengeResponse

    suspend fun getMyChallenges():
            List<ChallengeResponse>

    // 홈용 챌린지
    suspend fun getHomeChallenges():
            List<HomeChallengeResponse>

    suspend fun inviteFriend(
        challengeId: Long,
        friendId: Long
    ): ChallengeMemberResponse

    suspend fun getMyInvitations():
            List<ChallengeInvitationResponse>

    suspend fun acceptInvitation(
        memberId: Long
    ): ChallengeMemberResponse

    suspend fun rejectInvitation(
        memberId: Long
    ): ChallengeMemberResponse

    suspend fun getChallengeMembers(
        challengeId: Long
    ): List<ChallengeMemberResponse>

    suspend fun getMyProgress(
        challengeId: Long
    ): ChallengeProgressResponse

    suspend fun getParticipantProgress(
        challengeId: Long
    ): List<ChallengeParticipantProgressResponse>

    suspend fun getChallengeDetail(
        challengeId: Long
    ): ChallengeDetailResponse

    suspend fun completeChallenge(
        challengeId: Long
    ): ChallengeRecordResponse

    suspend fun cancelChallengeCompletion(
        challengeId: Long
    )
}