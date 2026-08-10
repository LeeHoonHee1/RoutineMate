package com.example.routinemate.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserResponse
)

@Serializable
data class UserResponse(
    val id: Long,
    val email: String,
    val nickname: String
)