package com.example.routinemate.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val accessToken: String
)