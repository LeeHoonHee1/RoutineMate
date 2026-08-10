package com.example.routinemate.data.remote.error

import com.example.routinemate.data.remote.dto.ErrorResponse
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import javax.inject.Inject

class ApiErrorParser @Inject constructor(
    private val json: Json
) {

    // Retrofit HTTP 에러에서 서버 메시지 추출
    fun parseMessage(
        exception: HttpException
    ): String? {

        return try {

            // 서버가 내려준 에러 JSON 문자열
            val errorBody =
                exception.response()
                    ?.errorBody()
                    ?.string()
                    ?: return null

            // JSON을 ErrorResponse 객체로 변환
            json.decodeFromString<ErrorResponse>(
                errorBody
            ).message

        } catch (e: Exception) {

            null
        }
    }
}