package com.example.routinemate.data.remote.dto.friend

import kotlinx.serialization.Serializable

@Serializable
data class FriendUserResponse(

    // 사용자 ID
    val id: Long,

    // 사용자 이메일
    val email: String,

    // 사용자 닉네임
    val nickname: String
)