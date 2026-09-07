package com.example.routinemate.data.remote.dto.challenge

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeResponse(

    val id: Long,

    val title: String,

    val description: String?,

    val startDate: String,

    val endDate: String,

    val ownerId: Long,

    val ownerNickname: String,

    val createdAt: String
)