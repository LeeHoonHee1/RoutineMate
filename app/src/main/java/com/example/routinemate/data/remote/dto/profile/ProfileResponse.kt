package com.example.routinemate.data.remote.dto.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    // 프로필 이미지 주소
    val profileImageUrl: String?
)