package com.example.routinemate.data.remote.dto.challenge

import kotlinx.serialization.Serializable

@Serializable
data class ChallengeDetailResponse(

    val challenge: ChallengeResponse,

    val myProgress: ChallengeProgressResponse,

    val participants: List<ChallengeParticipantProgressResponse>
)