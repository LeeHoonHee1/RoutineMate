package com.example.routinemate.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.domain.repository.ChallengeRepository
import com.example.routinemate.domain.repository.HabitRepository
import com.example.routinemate.domain.repository.StatisticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

                    habitErrorMessage = null,
                    statisticsErrorMessage = null,
                    challengeErrorMessage = null,
                    actionErrorMessage = null
                )
            }

            refreshHomeData()

            _uiState.update {
                it.copy(
                    isLoading = false
                )
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
                    actionErrorMessage = null
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

                // 습관 관련 데이터만 다시 조회
                refreshHabitData()

                // 완료 결과가 통계에도 영향을 주므로
                // 통계도 다시 조회
                refreshStatisticsData()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        actionErrorMessage =
                            "습관 완료 상태를 변경하지 못했습니다."
                    )
                }
            }
        }
    }

    // 홈 전체 조회
    private suspend fun refreshHomeData() {

        /*
         * 서로 독립적인 데이터이므로
         * 각각 실패해도 다른 영역은 계속 조회한다.
         */
        coroutineScope {

            val habitJob =
                async {
                    refreshHabitData()
                }

            val statisticsJob =
                async {
                    refreshStatisticsData()
                }

            val challengeJob =
                async {
                    refreshChallengeData()
                }

            habitJob.await()
            statisticsJob.await()
            challengeJob.await()
        }
    }

    // 습관 영역 조회
    private suspend fun refreshHabitData() {

        try {

            val habits =
                habitRepository.getHabits()

            val completedCount =
                habits.count { habit ->
                    habit.isCompletedToday
                }

            val todayCompletionRate =
                if (habits.isEmpty()) {

                    0

                } else {

                    (
                            completedCount *
                                    100.0 /
                                    habits.size
                            ).roundToInt()
                }

            _uiState.update {

                it.copy(
                    todayHabits =
                        habits,

                    totalHabitCount =
                        habits.size,

                    completedHabitCount =
                        completedCount,

                    todayCompletionRate =
                        todayCompletionRate,

                    habitErrorMessage =
                        null
                )
            }

        } catch (e: Exception) {

            _uiState.update {

                it.copy(
                    habitErrorMessage =
                        "오늘의 습관을 불러오지 못했습니다."
                )
            }
        }
    }

    // 통계 영역 조회
    private suspend fun refreshStatisticsData() {

        try {

            val weeklyStatistics =
                statisticsRepository
                    .getWeeklyStatistics()

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

            val weeklyTotalCompletedCount =
                weeklyStatistics
                    .sumOf { statistics ->
                        statistics
                            .completedHabitCount
                    }

            val bestDay =
                weeklyStatistics
                    .filter { statistics ->
                        statistics.completionRate > 0
                    }
                    .maxByOrNull { statistics ->
                        statistics.completionRate
                    }
                    ?.date

            _uiState.update {

                it.copy(
                    weeklyStatistics =
                        weeklyStatistics,

                    weeklyAverageCompletionRate =
                        weeklyAverageCompletionRate,

                    weeklyTotalCompletedCount =
                        weeklyTotalCompletedCount,

                    bestDay =
                        bestDay,

                    statisticsErrorMessage =
                        null
                )
            }

        } catch (e: Exception) {

            _uiState.update {

                it.copy(
                    statisticsErrorMessage =
                        "이번 주 통계를 불러오지 못했습니다."
                )
            }
        }
    }

    // 챌린지 영역 조회
    private suspend fun refreshChallengeData() {

        try {

            val homeChallenges =
                challengeRepository
                    .getHomeChallenges()

            _uiState.update {

                it.copy(
                    homeChallenges =
                        homeChallenges,

                    challengeErrorMessage =
                        null
                )
            }

        } catch (e: Exception) {

            _uiState.update {

                it.copy(
                    challengeErrorMessage =
                        "챌린지 정보를 불러오지 못했습니다."
                )
            }
        }
    }

    // 습관 영역 다시 시도
    fun retryHabitData() {

        viewModelScope.launch {
            refreshHabitData()
        }
    }

    // 통계 영역 다시 시도
    fun retryStatisticsData() {

        viewModelScope.launch {
            refreshStatisticsData()
        }
    }

    // 챌린지 영역 다시 시도
    fun retryChallengeData() {

        viewModelScope.launch {
            refreshChallengeData()
        }
    }

    // 액션 오류 메시지 제거
    fun clearActionError() {

        _uiState.update {
            it.copy(
                actionErrorMessage = null
            )
        }
    }
}