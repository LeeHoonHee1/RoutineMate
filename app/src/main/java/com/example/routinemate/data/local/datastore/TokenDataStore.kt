package com.example.routinemate.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// 앱 전체에서 사용할 DataStore
private val Context.tokenDataStore by preferencesDataStore(
    name = "token_preferences"
)

@Singleton
class TokenDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {

        // Access Token 저장 키
        private val ACCESS_TOKEN =
            stringPreferencesKey("access_token")

        // Refresh Token 저장 키
        private val REFRESH_TOKEN =
            stringPreferencesKey("refresh_token")
    }

    // Access Token 읽기
    val accessToken: Flow<String?> =
        context.tokenDataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }

    // Refresh Token 읽기
    val refreshToken: Flow<String?> =
        context.tokenDataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }

    // 로그인 후 토큰 두 개 저장
    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        context.tokenDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    // 새로 발급받은 Access Token만 저장
    suspend fun saveAccessToken(
        accessToken: String
    ) {
        context.tokenDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    // 로그아웃 시 토큰 삭제
    suspend fun clearTokens() {
        context.tokenDataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
        }
    }
}