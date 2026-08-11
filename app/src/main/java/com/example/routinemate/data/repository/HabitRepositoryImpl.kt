package com.example.routinemate.data.repository

import com.example.routinemate.data.remote.datasource.HabitRemoteDataSource
import com.example.routinemate.data.remote.dto.habit.CreateHabitRequest
import com.example.routinemate.data.remote.dto.habit.HabitResponse
import com.example.routinemate.data.remote.dto.habit.UpdateHabitRequest
import com.example.routinemate.domain.repository.HabitRepository
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val remoteDataSource: HabitRemoteDataSource
) : HabitRepository {

    // 새로운 습관 생성
    override suspend fun createHabit(
        title: String,
        description: String?
    ): HabitResponse {

        val request = CreateHabitRequest(
            title = title,
            description = description
        )

        return remoteDataSource.createHabit(request)
    }

    // 현재 사용자의 습관 목록 조회
    override suspend fun getHabits(): List<HabitResponse> {
        return remoteDataSource.getHabits()
    }

    // 습관 수정
    override suspend fun updateHabit(
        habitId: Long,
        title: String,
        description: String?
    ): HabitResponse {

        val request = UpdateHabitRequest(
            title = title,
            description = description
        )

        return remoteDataSource.updateHabit(
            habitId = habitId,
            request = request
        )
    }

    // 습관 삭제
    override suspend fun deleteHabit(
        habitId: Long
    ) {
        remoteDataSource.deleteHabit(habitId)
    }
}