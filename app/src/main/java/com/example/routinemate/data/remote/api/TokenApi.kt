package com.example.routinemate.data.remote.api

import com.example.routinemate.data.remote.dto.RefreshTokenRequest
import com.example.routinemate.data.remote.dto.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenApi {

    // Refresh Token으로 새 Access Token 발급
    @POST("auth/refresh")
    suspend fun refresh(
        @Body request: RefreshTokenRequest
    ): RefreshTokenResponse
}