package com.example.routinemate.data.remote.dto.challenge

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeRecordResponse(

    val id: Long,

    val challengeId: Long,

    val userId: Long,

    val nickname: String,

    val recordDate: String,

    val completedAt: String
)