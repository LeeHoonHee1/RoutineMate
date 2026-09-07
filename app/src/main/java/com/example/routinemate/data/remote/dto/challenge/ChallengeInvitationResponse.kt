package com.example.routinemate.data.remote.dto.challenge

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeInvitationResponse(

    val memberId: Long,

    val challengeId: Long,

    val title: String,

    val description: String?,

    val startDate: String,

    val endDate: String,

    val ownerId: Long,

    val ownerNickname: String,

    val createdAt: String
)