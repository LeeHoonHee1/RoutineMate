package com.example.routinemate.data.remote.dto.challenge

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeParticipantProgressResponse(

    val userId: Long,

    val nickname: String,

    val role: String,

    val completedDays: Long,

    val totalDays: Long,

    val progressRate: Int,

    val isCompletedToday: Boolean
)