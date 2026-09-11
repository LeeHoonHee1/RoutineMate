package com.example.routinemate.data.remote.dto.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val nickname: String
)