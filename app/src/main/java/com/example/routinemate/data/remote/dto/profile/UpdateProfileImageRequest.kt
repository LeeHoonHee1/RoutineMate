package com.example.routinemate.data.remote.dto.profile

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileImageRequest(

    // 변경할 프로필 이미지 주소
    val profileImageUrl: String?
)