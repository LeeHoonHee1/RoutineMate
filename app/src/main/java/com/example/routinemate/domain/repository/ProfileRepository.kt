package com.example.routinemate.domain.repository

import com.example.routinemate.data.remote.dto.profile.ProfileResponse
import com.example.routinemate.data.remote.dto.profile.ProfileSummaryResponse
import okhttp3.MultipartBody

interface ProfileRepository {

    // 내 프로필 조회
    suspend fun getMyProfile():
            ProfileResponse

    // 닉네임 수정
    suspend fun updateMyProfile(
        nickname: String
    ): ProfileResponse

    // 내 활동 요약
    suspend fun getMyProfileSummary():
            ProfileSummaryResponse

    // 실제 프로필 이미지 파일 업로드
    suspend fun uploadProfileImage(
        file: MultipartBody.Part
    ): ProfileResponse
}