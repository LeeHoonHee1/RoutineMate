package com.example.routinemate.data.repository

import com.example.routinemate.data.remote.datasource.FriendRemoteDataSource
import com.example.routinemate.data.remote.dto.friend.FriendRequestResponse
import com.example.routinemate.data.remote.dto.friend.FriendUserResponse
import com.example.routinemate.domain.repository.FriendRepository
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val remoteDataSource: FriendRemoteDataSource
) : FriendRepository {

    // 사용자 검색
    override suspend fun searchUsers(
        keyword: String
    ): List<FriendUserResponse> {

        return remoteDataSource.searchUsers(
            keyword = keyword
        )
    }

    // 친구 요청 보내기
    override suspend fun sendFriendRequest(
        receiverId: Long
    ): FriendRequestResponse {

        return remoteDataSource.sendFriendRequest(
            receiverId = receiverId
        )
    }

    // 받은 친구 요청 조회
    override suspend fun getReceivedFriendRequests():
            List<FriendRequestResponse> {

        return remoteDataSource
            .getReceivedFriendRequests()
    }

    // 친구 요청 수락
    override suspend fun acceptFriendRequest(
        requestId: Long
    ): FriendRequestResponse {

        return remoteDataSource.acceptFriendRequest(
            requestId = requestId
        )
    }

    // 친구 요청 거절
    override suspend fun rejectFriendRequest(
        requestId: Long
    ): FriendRequestResponse {

        return remoteDataSource.rejectFriendRequest(
            requestId = requestId
        )
    }

    // 친구 목록 조회
    override suspend fun getFriends():
            List<FriendUserResponse> {

        return remoteDataSource.getFriends()
    }
}