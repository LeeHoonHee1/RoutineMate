package com.example.routinemate.data.repository

import com.example.routinemate.data.remote.datasource.AuthRemoteDataSource
import com.example.routinemate.data.remote.dto.LoginRequest
import com.example.routinemate.data.remote.dto.LoginResponse
import com.example.routinemate.data.remote.dto.RefreshTokenRequest
import com.example.routinemate.data.remote.dto.RefreshTokenResponse
import com.example.routinemate.data.remote.dto.RegisterRequest
import com.example.routinemate.data.remote.dto.RegisterResponse
import com.example.routinemate.data.remote.dto.UserResponse
import com.example.routinemate.domain.repository.AuthRepository
import javax.inject.Inject
import com.example.routinemate.data.local.datastore.TokenDataStore
import kotlinx.coroutines.flow.first

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val tokenDataStore: TokenDataStore
) : AuthRepository {

    // 로그인 정보를 서버 요청용 DTO로 변환
    override suspend fun login(
        email: String,
        password: String
    ): LoginResponse {

        val request = LoginRequest(
            email = email,
            password = password
        )

        return remoteDataSource.login(request)
    }

    // 회원가입 정보를 서버 요청용 DTO로 변환
    override suspend fun signup(
        email: String,
        password: String,
        nickname: String
    ): RegisterResponse {

        val request = RegisterRequest(
            email = email,
            password = password,
            nickname = nickname
        )

        return remoteDataSource.signup(request)
    }

    // Refresh Token을 서버 요청용 DTO로 변환
    override suspend fun refreshToken(
        refreshToken: String
    ): RefreshTokenResponse {

        val request = RefreshTokenRequest(
            refreshToken = refreshToken
        )

        return remoteDataSource.refresh(request)
    }

    // 현재 로그인한 사용자 조회
    override suspend fun getMe(): UserResponse {
        return remoteDataSource.getMe()
    }

    // 저장된 Refresh Token으로 새 Access Token 발급
    override suspend fun refreshAccessToken(): Boolean {

        // DataStore에서 Refresh Token 읽기
        val refreshToken = tokenDataStore.refreshToken.first()
            ?: return false

        return try {

            // 서버에 Access Token 재발급 요청
            val response = remoteDataSource.refresh(
                RefreshTokenRequest(
                    refreshToken = refreshToken
                )
            )

            // 새 Access Token 저장
            tokenDataStore.saveAccessToken(
                response.accessToken
            )

            true

        } catch (e: Exception) {

            false
        }
    }
}