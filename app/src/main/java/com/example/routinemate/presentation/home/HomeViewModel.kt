package com.example.routinemate.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.domain.repository.HabitRepository
import com.example.routinemate.domain.repository.StatisticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val statisticsRepository: StatisticsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    // Home 화면에 필요한 전체 데이터 조회
    fun loadHomeData() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                // 실제 Home 데이터 조회
                refreshHomeData()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "홈 정보를 불러오지 못했습니다."
                    )
                }
            }
        }
    }

    // Home에서 오늘 습관 완료 상태 토글
    fun toggleHabitCompletion(
        habitId: Long,
        isCompletedToday: Boolean
    ) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                if (isCompletedToday) {
                    // 이미 완료된 Habit이면 완료 취소
                    habitRepository.cancelHabitCompletion(habitId)
                } else {
                    // 아직 완료하지 않은 Habit이면 완료 처리
                    habitRepository.completeHabit(habitId)
                }

                // 완료 상태 변경 후 Home 데이터 갱신
                refreshHomeData()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "습관 완료 상태를 변경하지 못했습니다."
                    )
                }
            }
        }
    }

    // Home 데이터 실제 조회 및 상태 갱신
    private suspend fun refreshHomeData() {

        // 오늘 Habit 목록 조회
        val habits = habitRepository.getHabits()

        // 최근 7일 통계 조회
        val weeklyStatistics =
            statisticsRepository.getWeeklyStatistics()

        // 오늘 완료한 Habit 개수
        val completedCount = habits.count { habit ->
            habit.isCompletedToday
        }

        // 오늘 달성률 계산
        val todayCompletionRate =
            if (habits.isEmpty()) {
                0
            } else {
                (
                        completedCount * 100.0 /
                                habits.size
                        ).roundToInt()
            }

        // 최근 7일 평균 달성률
        val weeklyAverageCompletionRate =
            if (weeklyStatistics.isEmpty()) {
                0
            } else {
                weeklyStatistics
                    .map { statistics ->
                        statistics.completionRate
                    }
                    .average()
                    .roundToInt()
            }

        // 최근 7일 총 완료 횟수
        val weeklyTotalCompletedCount =
            weeklyStatistics.sumOf { statistics ->
                statistics.completedHabitCount
            }

        // 실제 달성 기록이 있는 날 중 가장 높은 달성률을 기록한 날짜
        val bestDay =
            weeklyStatistics
                .filter { statistics ->
                    statistics.completionRate > 0
                }
                .maxByOrNull { statistics ->
                    statistics.completionRate
                }
                ?.date

        // Home 화면 상태 갱신
        _uiState.update {
            it.copy(
                todayHabits = habits,
                totalHabitCount = habits.size,
                completedHabitCount = completedCount,
                todayCompletionRate = todayCompletionRate,
                weeklyStatistics = weeklyStatistics,
                weeklyAverageCompletionRate = weeklyAverageCompletionRate,
                weeklyTotalCompletedCount = weeklyTotalCompletedCount,
                bestDay = bestDay,
                isLoading = false
            )
        }
    }
}