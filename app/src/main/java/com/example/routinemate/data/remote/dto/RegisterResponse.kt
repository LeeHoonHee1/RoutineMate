package com.example.routinemate.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val id: Long,
    val email: String,
    val nickname: String
)