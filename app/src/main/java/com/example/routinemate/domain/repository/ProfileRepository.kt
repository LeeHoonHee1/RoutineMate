package com.example.routinemate.domain.repository

import com.example.routinemate.data.remote.dto.profile.ProfileResponse
import com.example.routinemate.data.remote.dto.profile.ProfileSummaryResponse

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
}