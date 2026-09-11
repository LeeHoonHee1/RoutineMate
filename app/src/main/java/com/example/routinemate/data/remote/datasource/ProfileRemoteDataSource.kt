package com.example.routinemate.data.remote.datasource

import com.example.routinemate.data.remote.api.ProfileApi
import com.example.routinemate.data.remote.dto.profile.ProfileResponse
import com.example.routinemate.data.remote.dto.profile.ProfileSummaryResponse
import com.example.routinemate.data.remote.dto.profile.UpdateProfileRequest
import javax.inject.Inject

class ProfileRemoteDataSource @Inject constructor(
    private val profileApi: ProfileApi
) {

    // 내 프로필 조회
    suspend fun getMyProfile():
            ProfileResponse {

        return profileApi.getMyProfile()
    }

    // 내 프로필 수정
    suspend fun updateMyProfile(
        nickname: String
    ): ProfileResponse {

        return profileApi.updateMyProfile(
            request = UpdateProfileRequest(
                nickname = nickname
            )
        )
    }

    // 내 활동 요약
    suspend fun getMyProfileSummary():
            ProfileSummaryResponse {

        return profileApi.getMyProfileSummary()
    }
}