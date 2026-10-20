package com.example.routinemate.data.repository

import com.example.routinemate.data.remote.datasource.ProfileRemoteDataSource
import com.example.routinemate.data.remote.dto.profile.ProfileResponse
import com.example.routinemate.data.remote.dto.profile.ProfileSummaryResponse
import com.example.routinemate.data.remote.dto.profile.UpdateProfileImageRequest
import com.example.routinemate.domain.repository.ProfileRepository
import javax.inject.Inject
import okhttp3.MultipartBody

class ProfileRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {

    override suspend fun getMyProfile():
            ProfileResponse {

        return remoteDataSource.getMyProfile()
    }

    override suspend fun updateMyProfile(
        nickname: String
    ): ProfileResponse {

        return remoteDataSource.updateMyProfile(
            nickname = nickname
        )
    }

    override suspend fun getMyProfileSummary():
            ProfileSummaryResponse {

        return remoteDataSource.getMyProfileSummary()
    }

    // 실제 프로필 이미지 파일 업로드
    override suspend fun uploadProfileImage(
        file: MultipartBody.Part
    ): ProfileResponse {

        return remoteDataSource.uploadProfileImage(
            file = file
        )
    }
}