package com.example.routinemate.domain.repository

import com.example.routinemate.data.remote.dto.friend.FriendRequestResponse
import com.example.routinemate.data.remote.dto.friend.FriendUserResponse

interface FriendRepository {

    // 사용자 검색
    suspend fun searchUsers(
        keyword: String
    ): List<FriendUserResponse>

    // 친구 요청 보내기
    suspend fun sendFriendRequest(
        receiverId: Long
    ): FriendRequestResponse

    // 받은 친구 요청 조회
    suspend fun getReceivedFriendRequests():
            List<FriendRequestResponse>

    // 친구 요청 수락
    suspend fun acceptFriendRequest(
        requestId: Long
    ): FriendRequestResponse

    // 친구 요청 거절
    suspend fun rejectFriendRequest(
        requestId: Long
    ): FriendRequestResponse

    // 친구 목록 조회
    suspend fun getFriends():
            List<FriendUserResponse>
}