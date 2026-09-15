package com.example.routinemate.data.remote.dto.challenge

import kotlinx.serialization.Serializable

@Serializable
data class HomeChallengeResponse(

    val id: Long,

    val title: String,

    val startDate: String,

    val endDate: String,

    // 종료일까지 남은 날짜
    val remainingDays: Long,

    // 전체 참여 인원
    val totalMemberCount: Long,

    // 오늘 완료한 인원
    val todayCompletedMemberCount: Long,

    // 내가 오늘 완료했는지
    val isCompletedToday: Boolean
)