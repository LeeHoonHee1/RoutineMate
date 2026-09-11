package com.example.routinemate.data.repository

import com.example.routinemate.data.remote.datasource.ChallengeRemoteDataSource
import com.example.routinemate.data.remote.dto.challenge.ChallengeDetailResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeInvitationResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeMemberResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeParticipantProgressResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeProgressResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeRecordResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeResponse
import com.example.routinemate.data.remote.dto.challenge.CreateChallengeRequest
import com.example.routinemate.domain.repository.ChallengeRepository
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(
    private val remoteDataSource: ChallengeRemoteDataSource
) : ChallengeRepository {

    override suspend fun createChallenge(
        request: CreateChallengeRequest
    ): ChallengeResponse {
        return remoteDataSource.createChallenge(request)
    }

    override suspend fun getMyChallenges():
            List<ChallengeResponse> {
        return remoteDataSource.getMyChallenges()
    }

    override suspend fun inviteFriend(
        challengeId: Long,
        friendId: Long
    ): ChallengeMemberResponse {
        return remoteDataSource.inviteFriend(
            challengeId,
            friendId
        )
    }

    override suspend fun getMyInvitations():
            List<ChallengeInvitationResponse> {
        return remoteDataSource.getMyInvitations()
    }

    override suspend fun acceptInvitation(
        memberId: Long
    ): ChallengeMemberResponse {
        return remoteDataSource.acceptInvitation(
            memberId
        )
    }

    override suspend fun rejectInvitation(
        memberId: Long
    ): ChallengeMemberResponse {
        return remoteDataSource.rejectInvitation(
            memberId
        )
    }

    override suspend fun getChallengeMembers(
        challengeId: Long
    ): List<ChallengeMemberResponse> {
        return remoteDataSource.getChallengeMembers(
            challengeId
        )
    }

    override suspend fun getMyProgress(
        challengeId: Long
    ): ChallengeProgressResponse {
        return remoteDataSource.getMyProgress(
            challengeId
        )
    }

    override suspend fun getParticipantProgress(
        challengeId: Long
    ): List<ChallengeParticipantProgressResponse> {
        return remoteDataSource.getParticipantProgress(
            challengeId
        )
    }

    override suspend fun getChallengeDetail(
        challengeId: Long
    ): ChallengeDetailResponse {
        return remoteDataSource.getChallengeDetail(
            challengeId
        )
    }

    override suspend fun completeChallenge(
        challengeId: Long
    ): ChallengeRecordResponse {
        return remoteDataSource.completeChallenge(
            challengeId
        )
    }

    override suspend fun cancelChallengeCompletion(
        challengeId: Long
    ) {
        remoteDataSource.cancelChallengeCompletion(
            challengeId
        )
    }
}