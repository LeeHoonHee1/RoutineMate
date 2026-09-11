package com.example.routinemate.data.remote.dto.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileSummaryResponse(
    val habitCount: Long,
    val friendCount: Long,
    val challengeCount: Long
)