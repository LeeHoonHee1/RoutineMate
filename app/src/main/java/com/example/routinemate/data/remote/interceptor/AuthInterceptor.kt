package com.example.routinemate.data.remote.interceptor

import com.example.routinemate.data.local.datastore.TokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : Interceptor {

    // Retrofit 요청이 나가기 전에 실행
    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val originalRequest = chain.request()

        // DataStore에서 현재 Access Token 읽기
        val accessToken = runBlocking {
            tokenDataStore.accessToken.first()
        }

        // 토큰이 있으면 Authorization 헤더 추가
        val request = if (accessToken != null) {

            originalRequest.newBuilder()
                .header(
                    "Authorization",
                    "Bearer $accessToken"
                )
                .build()

        } else {

            originalRequest
        }

        // 실제 서버 요청 진행
        return chain.proceed(request)
    }
}