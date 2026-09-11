package com.example.routinemate.data.repository

import com.example.routinemate.data.remote.datasource.ProfileRemoteDataSource
import com.example.routinemate.data.remote.dto.profile.ProfileResponse
import com.example.routinemate.data.remote.dto.profile.ProfileSummaryResponse
import com.example.routinemate.domain.repository.ProfileRepository
import javax.inject.Inject

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
}