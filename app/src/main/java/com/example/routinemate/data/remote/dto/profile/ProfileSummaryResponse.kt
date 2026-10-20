package com.example.routinemate.data.remote.dto.profile

import kotlinx.serialization.Serializable

@Serializable
data class ProfileSummaryResponse(

    // 현재 사용 중인 습관 수
    val habitCount: Long,

    // 친구 수
    val friendCount: Long,

    // 현재 참여 중인 챌린지 수
    val challengeCount: Long,

    // 가입 후 활동일 수
    val activeDaysSinceSignup: Long,

    // 가장 많이 완료한 습관 카테고리
    val topHabitCategory: String?,

    // 누적 챌린지 참여 수
    val totalChallengeParticipationCount: Long
)