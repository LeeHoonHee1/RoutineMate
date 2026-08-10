package com.example.routinemate.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String
)