package com.example.routinemate.data.remote.api

import com.example.routinemate.data.remote.dto.habit.CreateHabitRequest
import com.example.routinemate.data.remote.dto.habit.HabitCompletionResponse
import com.example.routinemate.data.remote.dto.habit.HabitResponse
import com.example.routinemate.data.remote.dto.habit.UpdateHabitRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HabitApi {

    // 새로운 습관 생성
    @POST("habits")
    suspend fun createHabit(
        @Body request: CreateHabitRequest
    ): HabitResponse

    // 현재 사용자의 활성 습관 목록 조회
    @GET("habits")
    suspend fun getHabits(): List<HabitResponse>

    // 습관 수정
    @PUT("habits/{habitId}")
    suspend fun updateHabit(
        @Path("habitId") habitId: Long,
        @Body request: UpdateHabitRequest
    ): HabitResponse

    // 습관 삭제
    @DELETE("habits/{habitId}")
    suspend fun deleteHabit(
        @Path("habitId") habitId: Long
    )

    // 오늘 습관 완료
    @POST("habits/{habitId}/complete")
    suspend fun completeHabit(
        @Path("habitId") habitId: Long
    ): HabitCompletionResponse

    // 오늘 습관 완료 취소
    @DELETE("habits/{habitId}/complete")
    suspend fun cancelHabitCompletion(
        @Path("habitId") habitId: Long
    )
}