package com.example.routinemate.data.repository

import com.example.routinemate.data.remote.datasource.ChallengeRemoteDataSource
import com.example.routinemate.data.remote.dto.challenge.ChallengeInvitationResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeMemberResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeResponse
import com.example.routinemate.data.remote.dto.challenge.CreateChallengeRequest
import com.example.routinemate.domain.repository.ChallengeRepository
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(
    private val remoteDataSource: ChallengeRemoteDataSource
) : ChallengeRepository {

    // 챌린지 생성
    override suspend fun createChallenge(
        request: CreateChallengeRequest
    ): ChallengeResponse {

        return remoteDataSource.createChallenge(
            request = request
        )
    }

    // 내가 참여 중인 챌린지 조회
    override suspend fun getMyChallenges():
            List<ChallengeResponse> {

        return remoteDataSource.getMyChallenges()
    }

    // 친구 초대
    override suspend fun inviteFriend(
        challengeId: Long,
        friendId: Long
    ): ChallengeMemberResponse {

        return remoteDataSource.inviteFriend(
            challengeId = challengeId,
            friendId = friendId
        )
    }

    // 내가 받은 초대 조회
    override suspend fun getMyInvitations():
            List<ChallengeInvitationResponse> {

        return remoteDataSource.getMyInvitations()
    }

    // 초대 수락
    override suspend fun acceptInvitation(
        memberId: Long
    ): ChallengeMemberResponse {

        return remoteDataSource.acceptInvitation(
            memberId = memberId
        )
    }

    // 초대 거절
    override suspend fun rejectInvitation(
        memberId: Long
    ): ChallengeMemberResponse {

        return remoteDataSource.rejectInvitation(
            memberId = memberId
        )
    }

    // 챌린지 참여자 조회
    override suspend fun getChallengeMembers(
        challengeId: Long
    ): List<ChallengeMemberResponse> {

        return remoteDataSource.getChallengeMembers(
            challengeId = challengeId
        )
    }
}