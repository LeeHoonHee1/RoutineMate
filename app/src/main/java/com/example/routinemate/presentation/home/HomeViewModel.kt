package com.example.routinemate.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.domain.repository.ChallengeRepository
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
    private val statisticsRepository: StatisticsRepository,
    private val challengeRepository: ChallengeRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            HomeUiState()
        )

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    // Home 전체 데이터 조회
    fun loadHomeData() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                refreshHomeData()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "홈 정보를 불러오지 못했습니다."
                    )
                }
            }
        }
    }

    // 오늘 습관 완료 상태 변경
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

                    habitRepository
                        .cancelHabitCompletion(
                            habitId
                        )

                } else {

                    habitRepository
                        .completeHabit(
                            habitId
                        )
                }

                refreshHomeData()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "습관 완료 상태를 변경하지 못했습니다."
                    )
                }
            }
        }
    }

    // Home 데이터 실제 조회
    private suspend fun refreshHomeData() {

        // 오늘 습관
        val habits =
            habitRepository.getHabits()

        // 최근 7일 통계
        val weeklyStatistics =
            statisticsRepository
                .getWeeklyStatistics()

        // 내가 참여 중인 진행 챌린지
        val homeChallenges =
            challengeRepository
                .getHomeChallenges()

        // 오늘 완료한 습관 수
        val completedCount =
            habits.count { habit ->
                habit.isCompletedToday
            }

        // 오늘 달성률
        val todayCompletionRate =
            if (habits.isEmpty()) {

                0

            } else {

                (
                        completedCount * 100.0 /
                                habits.size
                        ).roundToInt()
            }

        // 최근 7일 평균
        val weeklyAverageCompletionRate =
            if (
                weeklyStatistics.isEmpty()
            ) {

                0

            } else {

                weeklyStatistics
                    .map { statistics ->
                        statistics.completionRate
                    }
                    .average()
                    .roundToInt()
            }

        // 최근 7일 총 완료
        val weeklyTotalCompletedCount =
            weeklyStatistics
                .sumOf { statistics ->
                    statistics
                        .completedHabitCount
                }

        // 가장 잘한 날
        val bestDay =
            weeklyStatistics
                .filter { statistics ->
                    statistics.completionRate > 0
                }
                .maxByOrNull { statistics ->
                    statistics.completionRate
                }
                ?.date

        // Home 상태 갱신
        _uiState.update {

            it.copy(
                todayHabits = habits,
                homeChallenges =
                    homeChallenges,
                totalHabitCount =
                    habits.size,
                completedHabitCount =
                    completedCount,
                todayCompletionRate =
                    todayCompletionRate,
                weeklyStatistics =
                    weeklyStatistics,
                weeklyAverageCompletionRate =
                    weeklyAverageCompletionRate,
                weeklyTotalCompletedCount =
                    weeklyTotalCompletedCount,
                bestDay = bestDay,
                isLoading = false
            )
        }
    }
}