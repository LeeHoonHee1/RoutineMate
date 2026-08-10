package com.example.routinemate.domain.repository

import com.example.routinemate.data.remote.dto.LoginResponse
import com.example.routinemate.data.remote.dto.RefreshTokenResponse
import com.example.routinemate.data.remote.dto.RegisterResponse
import com.example.routinemate.data.remote.dto.UserResponse

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse

    suspend fun signup(
        email: String,
        password: String,
        nickname: String
    ): RegisterResponse

    suspend fun refreshToken(
        refreshToken: String
    ): RefreshTokenResponse

    // 저장된 Refresh Token으로 Access Token 재발급
    suspend fun refreshAccessToken(): Boolean

    suspend fun getMe(): UserResponse
}