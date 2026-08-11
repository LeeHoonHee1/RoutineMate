package com.example.routinemate.data.remote.datasource

import com.example.routinemate.data.remote.api.HabitApi
import com.example.routinemate.data.remote.dto.habit.CreateHabitRequest
import com.example.routinemate.data.remote.dto.habit.HabitCompletionResponse
import com.example.routinemate.data.remote.dto.habit.HabitResponse
import com.example.routinemate.data.remote.dto.habit.UpdateHabitRequest
import javax.inject.Inject

class HabitRemoteDataSource @Inject constructor(
    private val habitApi: HabitApi
) {

    // 새로운 습관 생성
    suspend fun createHabit(
        request: CreateHabitRequest
    ): HabitResponse {
        return habitApi.createHabit(request)
    }

    // 현재 사용자의 습관 목록 조회
    suspend fun getHabits(): List<HabitResponse> {
        return habitApi.getHabits()
    }

    // 습관 수정
    suspend fun updateHabit(
        habitId: Long,
        request: UpdateHabitRequest
    ): HabitResponse {

        return habitApi.updateHabit(
            habitId = habitId,
            request = request
        )
    }

    // 습관 삭제
    suspend fun deleteHabit(
        habitId: Long
    ) {
        habitApi.deleteHabit(habitId)
    }

    // 오늘 습관 완료
    suspend fun completeHabit(
        habitId: Long
    ): HabitCompletionResponse {

        return habitApi.completeHabit(habitId)
    }

    // 오늘 습관 완료 취소
    suspend fun cancelHabitCompletion(
        habitId: Long
    ) {

        habitApi.cancelHabitCompletion(habitId)
    }
}