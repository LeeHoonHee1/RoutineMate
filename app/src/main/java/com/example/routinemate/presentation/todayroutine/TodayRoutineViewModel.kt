package com.example.routinemate.presentation.todayroutine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TodayRoutineViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            TodayRoutineUiState()
        )

    val uiState: StateFlow<TodayRoutineUiState> =
        _uiState.asStateFlow()

    init {
        loadTodayRoutines()
    }

    // 오늘 습관 조회
    fun loadTodayRoutines() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                refreshData()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "오늘의 루틴을 불러오지 못했습니다."
                    )
                }
            }
        }
    }

    // 완료 / 완료 취소
    fun toggleHabitCompletion(
        habitId: Long,
        isCompletedToday: Boolean
    ) {

        viewModelScope.launch {

            try {

                if (isCompletedToday) {

                    // 이미 완료 → 완료 취소
                    habitRepository
                        .cancelHabitCompletion(
                            habitId
                        )

                } else {

                    // 미완료 → 완료 처리
                    habitRepository
                        .completeHabit(
                            habitId
                        )
                }

                refreshData()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        errorMessage =
                            "습관 완료 상태를 변경하지 못했습니다."
                    )
                }
            }
        }
    }

    // 실제 데이터 갱신
    private suspend fun refreshData() {

        val habits =
            habitRepository.getHabits()

        val completedCount =
            habits.count { habit ->
                habit.isCompletedToday
            }

        val completionRate =
            if (habits.isEmpty()) {

                0

            } else {

                (
                        completedCount * 100.0 /
                                habits.size
                        ).roundToInt()
            }

        _uiState.update {
            it.copy(
                habits = habits,
                totalCount = habits.size,
                completedCount = completedCount,
                completionRate = completionRate,
                isLoading = false,
                errorMessage = null
            )
        }
    }
}