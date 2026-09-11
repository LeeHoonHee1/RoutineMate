package com.example.routinemate.data.remote.api

import com.example.routinemate.data.remote.dto.profile.ProfileResponse
import com.example.routinemate.data.remote.dto.profile.ProfileSummaryResponse
import com.example.routinemate.data.remote.dto.profile.UpdateProfileRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface ProfileApi {

    // 내 프로필 조회
    @GET("users/me/profile")
    suspend fun getMyProfile(): ProfileResponse

    // 내 프로필 수정
    @PATCH("users/me/profile")
    suspend fun updateMyProfile(
        @Body request: UpdateProfileRequest
    ): ProfileResponse

    // 내 활동 요약
    @GET("users/me/profile/summary")
    suspend fun getMyProfileSummary():
            ProfileSummaryResponse
}