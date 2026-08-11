package com.example.routinemate.domain.repository

import com.example.routinemate.data.remote.dto.habit.HabitResponse

interface HabitRepository {

    // 새로운 습관 생성
    suspend fun createHabit(
        title: String,
        description: String?
    ): HabitResponse

    // 현재 사용자의 습관 목록 조회
    suspend fun getHabits(): List<HabitResponse>

    // 습관 수정
    suspend fun updateHabit(
        habitId: Long,
        title: String,
        description: String?
    ): HabitResponse

    // 습관 삭제
    suspend fun deleteHabit(
        habitId: Long
    )
}