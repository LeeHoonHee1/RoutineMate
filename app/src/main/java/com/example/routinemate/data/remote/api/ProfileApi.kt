package com.example.routinemate.data.remote.api

import com.example.routinemate.data.remote.dto.profile.ProfileResponse
import com.example.routinemate.data.remote.dto.profile.ProfileSummaryResponse
import com.example.routinemate.data.remote.dto.profile.UpdateProfileImageRequest
import com.example.routinemate.data.remote.dto.profile.UpdateProfileRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface ProfileApi {

    // 내 프로필 조회
    @GET("users/me/profile")
    suspend fun getMyProfile(): ProfileResponse

    // 내 닉네임 수정
    @PATCH("users/me/profile")
    suspend fun updateMyProfile(
        @Body request: UpdateProfileRequest
    ): ProfileResponse

    // 내 활동 요약
    @GET("users/me/profile/summary")
    suspend fun getMyProfileSummary(): ProfileSummaryResponse

    // 실제 프로필 이미지 파일 업로드
    @Multipart
    @POST("users/me/profile/image/upload")
    suspend fun uploadProfileImage(
        @Part file: MultipartBody.Part
    ): ProfileResponse
}