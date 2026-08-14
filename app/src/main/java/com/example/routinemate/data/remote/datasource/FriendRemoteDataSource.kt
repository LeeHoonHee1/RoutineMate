package com.example.routinemate.data.remote.datasource

import com.example.routinemate.data.remote.api.FriendApi
import com.example.routinemate.data.remote.dto.friend.FriendRequestResponse
import com.example.routinemate.data.remote.dto.friend.FriendUserResponse
import javax.inject.Inject

class FriendRemoteDataSource @Inject constructor(
    private val friendApi: FriendApi
) {

    // 사용자 검색
    suspend fun searchUsers(
        keyword: String
    ): List<FriendUserResponse> {

        return friendApi.searchUsers(
            keyword = keyword
        )
    }

    // 친구 요청 보내기
    suspend fun sendFriendRequest(
        receiverId: Long
    ): FriendRequestResponse {

        return friendApi.sendFriendRequest(
            receiverId = receiverId
        )
    }

    // 내가 받은 친구 요청 조회
    suspend fun getReceivedFriendRequests():
            List<FriendRequestResponse> {

        return friendApi
            .getReceivedFriendRequests()
    }

    // 친구 요청 수락
    suspend fun acceptFriendRequest(
        requestId: Long
    ): FriendRequestResponse {

        return friendApi.acceptFriendRequest(
            requestId = requestId
        )
    }

    // 친구 요청 거절
    suspend fun rejectFriendRequest(
        requestId: Long
    ): FriendRequestResponse {

        return friendApi.rejectFriendRequest(
            requestId = requestId
        )
    }

    // 친구 목록 조회
    suspend fun getFriends():
            List<FriendUserResponse> {

        return friendApi.getFriends()
    }
}