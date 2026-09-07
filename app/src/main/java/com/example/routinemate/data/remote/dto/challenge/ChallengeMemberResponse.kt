package com.example.routinemate.data.remote.dto.challenge

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeMemberResponse(

    val id: Long,

    val challengeId: Long,

    val userId: Long,

    val nickname: String,

    val role: String,

    val status: String,

    val createdAt: String
)