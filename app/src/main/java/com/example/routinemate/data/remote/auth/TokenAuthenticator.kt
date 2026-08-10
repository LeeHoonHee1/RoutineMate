package com.example.routinemate.data.remote.auth

import com.example.routinemate.core.session.SessionManager
import com.example.routinemate.data.local.datastore.TokenDataStore
import com.example.routinemate.data.remote.api.TokenApi
import com.example.routinemate.data.remote.dto.RefreshTokenRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenApi: TokenApi,
    private val tokenDataStore: TokenDataStore,
    private val sessionManager: SessionManager
) : Authenticator {

    // 서버에서 401이 왔을 때 실행
    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {

        // 같은 요청이 계속 실패하는 상황 방지
        if (responseCount(response) >= 2) {
            return null
        }

        return runBlocking {

            // 저장된 Refresh Token 읽기
            val refreshToken =
                tokenDataStore.refreshToken.first()
                    ?: return@runBlocking null

            try {
                // Refresh Token으로 새 Access Token 요청
                val refreshResponse = tokenApi.refresh(
                    RefreshTokenRequest(
                        refreshToken = refreshToken
                    )
                )

                // 새 Access Token 저장
                tokenDataStore.saveAccessToken(
                    refreshResponse.accessToken
                )

                // 새 Access Token으로 원래 요청 재시도
                response.request
                    .newBuilder()
                    .header(
                        "Authorization",
                        "Bearer ${refreshResponse.accessToken}"
                    )
                    .build()

            } catch (e: Exception) {

                // Refresh까지 실패하면 저장된 토큰 삭제
                tokenDataStore.clearTokens()

                // 앱 전체에 세션 만료 알림
                sessionManager.expireSession()

                null
            }
        }
    }

    // 같은 요청이 몇 번 실패했는지 확인
    private fun responseCount(
        response: Response
    ): Int {

        var count = 1
        var previousResponse = response.priorResponse

        while (previousResponse != null) {
            count++
            previousResponse = previousResponse.priorResponse
        }

        return count
    }
}