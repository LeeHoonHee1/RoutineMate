package com.example.routinemate.data.remote.api

import com.example.routinemate.data.remote.dto.challenge.ChallengeDetailResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeInvitationResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeMemberResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeParticipantProgressResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeProgressResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeRecordResponse
import com.example.routinemate.data.remote.dto.challenge.ChallengeResponse
import com.example.routinemate.data.remote.dto.challenge.CreateChallengeRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChallengeApi {

    // 챌린지 생성
    @POST("challenges")
    suspend fun createChallenge(
        @Body request: CreateChallengeRequest
    ): ChallengeResponse

    // 내가 참여 중인 챌린지 목록
    @GET("challenges")
    suspend fun getMyChallenges():
            List<ChallengeResponse>

    // 친구를 챌린지에 초대
    @POST("challenges/{challengeId}/invite/{friendId}")
    suspend fun inviteFriend(
        @Path("challengeId") challengeId: Long,
        @Path("friendId") friendId: Long
    ): ChallengeMemberResponse

    // 내가 받은 챌린지 초대
    @GET("challenges/invitations")
    suspend fun getMyInvitations():
            List<ChallengeInvitationResponse>

    // 챌린지 초대 수락
    @POST("challenges/invitations/{memberId}/accept")
    suspend fun acceptInvitation(
        @Path("memberId") memberId: Long
    ): ChallengeMemberResponse

    // 챌린지 초대 거절
    @POST("challenges/invitations/{memberId}/reject")
    suspend fun rejectInvitation(
        @Path("memberId") memberId: Long
    ): ChallengeMemberResponse

    // 챌린지 참여자 목록
    @GET("challenges/{challengeId}/members")
    suspend fun getChallengeMembers(
        @Path("challengeId") challengeId: Long
    ): List<ChallengeMemberResponse>

    // 내 챌린지 진행률
    @GET("challenges/{challengeId}/progress")
    suspend fun getMyProgress(
        @Path("challengeId") challengeId: Long
    ): ChallengeProgressResponse

    // 참여자별 진행 현황
    @GET("challenges/{challengeId}/participants/progress")
    suspend fun getParticipantProgress(
        @Path("challengeId") challengeId: Long
    ): List<ChallengeParticipantProgressResponse>

    // 챌린지 상세
    @GET("challenges/{challengeId}/detail")
    suspend fun getChallengeDetail(
        @Path("challengeId") challengeId: Long
    ): ChallengeDetailResponse

    // 오늘 챌린지 완료
    @POST("challenges/{challengeId}/complete")
    suspend fun completeChallenge(
        @Path("challengeId") challengeId: Long
    ): ChallengeRecordResponse

    // 오늘 챌린지 완료 취소
    @DELETE("challenges/{challengeId}/complete")
    suspend fun cancelChallengeCompletion(
        @Path("challengeId") challengeId: Long
    )
}