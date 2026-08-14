package com.example.routinemate.data.remote.dto.friend

import kotlinx.serialization.Serializable

@Serializable
data class FriendRequestResponse(

    // 친구 요청 ID
    val id: Long,

    // 요청을 보낸 사용자 ID
    val requesterId: Long,

    // 요청을 보낸 사용자 닉네임
    val requesterNickname: String,

    // 요청을 받은 사용자 ID
    val receiverId: Long,

    // 요청을 받은 사용자 닉네임
    val receiverNickname: String,

    // PENDING / ACCEPTED / REJECTED
    val status: String,

    // 요청 생성 시간
    val createdAt: String
)