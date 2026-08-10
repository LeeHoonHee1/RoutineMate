package com.example.routinemate.data.remote.datasource

import com.example.routinemate.data.remote.api.AuthApi
import com.example.routinemate.data.remote.api.TokenApi
import com.example.routinemate.data.remote.dto.LoginRequest
import com.example.routinemate.data.remote.dto.LoginResponse
import com.example.routinemate.data.remote.dto.RefreshTokenRequest
import com.example.routinemate.data.remote.dto.RefreshTokenResponse
import com.example.routinemate.data.remote.dto.RegisterRequest
import com.example.routinemate.data.remote.dto.RegisterResponse
import com.example.routinemate.data.remote.dto.UserResponse
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApi: AuthApi,
    private val tokenApi: TokenApi
) {

    // 로그인 요청
    suspend fun login(
        request: LoginRequest
    ): LoginResponse {
        return authApi.login(request)
    }

    // 회원가입 요청
    suspend fun signup(
        request: RegisterRequest
    ): RegisterResponse {
        return authApi.signup(request)
    }

    // Refresh Token으로 새 Access Token 요청
    suspend fun refresh(
        request: RefreshTokenRequest
    ): RefreshTokenResponse {
        return tokenApi.refresh(request)
    }

    // 현재 로그인한 사용자 조회
    suspend fun getMe(): UserResponse {
        return authApi.getMe()
    }
}