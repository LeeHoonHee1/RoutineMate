package com.example.routinemate.data.remote.dto.challenge

import kotlinx.serialization.Serializable

@Serializable
data class CreateChallengeRequest(

    val title: String,

    val description: String?,

    val startDate: String,

    val endDate: String
)