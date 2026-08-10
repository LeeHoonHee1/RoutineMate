package com.example.routinemate.data.remote.api

import com.example.routinemate.data.remote.dto.LoginRequest
import com.example.routinemate.data.remote.dto.LoginResponse
import com.example.routinemate.data.remote.dto.RegisterRequest
import com.example.routinemate.data.remote.dto.RegisterResponse
import com.example.routinemate.data.remote.dto.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    // 로그인 API 호출
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    // 회원가입 API 호출
    @POST("auth/signup")
    suspend fun signup(
        @Body request: RegisterRequest
    ): RegisterResponse

    // 현재 로그인한 사용자 정보 조회
    @GET("auth/me")
    suspend fun getMe(): UserResponse
}