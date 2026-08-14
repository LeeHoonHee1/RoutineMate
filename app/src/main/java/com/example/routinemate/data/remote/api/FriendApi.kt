package com.example.routinemate.data.remote.api

import com.example.routinemate.data.remote.dto.friend.FriendRequestResponse
import com.example.routinemate.data.remote.dto.friend.FriendUserResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FriendApi {

    // 닉네임 또는 이메일로 사용자 검색
    @GET("friends/search")
    suspend fun searchUsers(
        @Query("keyword") keyword: String
    ): List<FriendUserResponse>

    // 친구 요청 보내기
    @POST("friends/requests/{receiverId}")
    suspend fun sendFriendRequest(
        @Path("receiverId") receiverId: Long
    ): FriendRequestResponse

    // 내가 받은 대기 중 친구 요청 조회
    @GET("friends/requests/received")
    suspend fun getReceivedFriendRequests():
            List<FriendRequestResponse>

    // 친구 요청 수락
    @POST("friends/requests/{requestId}/accept")
    suspend fun acceptFriendRequest(
        @Path("requestId") requestId: Long
    ): FriendRequestResponse

    // 친구 요청 거절
    @POST("friends/requests/{requestId}/reject")
    suspend fun rejectFriendRequest(
        @Path("requestId") requestId: Long
    ): FriendRequestResponse

    // 현재 사용자의 친구 목록 조회
    @GET("friends")
    suspend fun getFriends():
            List<FriendUserResponse>
}