package com.example.routinemate.data.remote.dto.challenge

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeProgressResponse(

    val challengeId: Long,

    val totalDays: Long,

    val completedDays: Long,

    val progressRate: Int,

    val isCompletedToday: Boolean
)