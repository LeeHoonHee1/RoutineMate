package com.example.routinemate.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.data.remote.dto.statistics.DailyHabitStatisticsResponse
import com.example.routinemate.data.remote.error.ApiErrorParser
import com.example.routinemate.domain.repository.StatisticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import kotlin.math.roundToInt
import java.time.LocalDate

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository,
    private val apiErrorParser: ApiErrorParser
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            StatisticsUiState()
        )

    val uiState: StateFlow<StatisticsUiState> =
        _uiState.asStateFlow()

    // 기존 7일 통계 조회
    fun loadWeeklyStatistics() {

        loadStatistics(
            period =
                StatisticsPeriod.WEEKLY
        )
    }

    // 최근 30일 통계 조회
    fun loadRecent30DayStatistics() {

        loadStatistics(
            period =
                StatisticsPeriod.THIRTY_DAYS
        )
    }

    // 통계 기간 변경
    fun onPeriodChange(
        period: StatisticsPeriod
    ) {

        // 이미 선택된 기간이면 다시 요청하지 않음
        if (
            uiState.value.selectedPeriod ==
            period
        ) {
            return
        }

        loadStatistics(
            period = period
        )
    }

    // 선택한 기간의 통계 조회
    private fun loadStatistics(
        period: StatisticsPeriod
    ) {

        // 중복 요청 방지
        if (
            uiState.value.isLoading
        ) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    selectedPeriod = period,
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                val statistics =
                    when (period) {

                        StatisticsPeriod.WEEKLY -> {

                            statisticsRepository
                                .getWeeklyStatistics()
                        }

                        StatisticsPeriod.THIRTY_DAYS -> {

                            statisticsRepository
                                .getRecent30DayStatistics()
                        }

                        StatisticsPeriod.MONTHLY -> {

                            val today =
                                LocalDate.now()

                            statisticsRepository
                                .getMonthlyStatistics(
                                    year =
                                        today.year,
                                    month =
                                        today.monthValue
                                )
                        }
                    }

                updateStatistics(
                    statistics = statistics
                )

            } catch (e: Exception) {

                val message =
                    if (
                        e is HttpException
                    ) {

                        apiErrorParser
                            .parseMessage(e)
                            ?: "통계 정보를 불러오지 못했습니다."

                    } else {

                        "통계 정보를 불러오지 못했습니다."
                    }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }
            }
        }
    }

    // 통계 데이터와 요약 정보 계산
    private fun updateStatistics(
        statistics:
        List<DailyHabitStatisticsResponse>
    ) {

        // 평균 달성률
        val averageCompletionRate =
            if (
                statistics.isEmpty()
            ) {

                0

            } else {

                statistics
                    .map {
                        it.completionRate
                    }
                    .average()
                    .roundToInt()
            }

        // 총 완료 횟수
        val totalCompletedCount =
            statistics.sumOf {
                it.completedHabitCount
            }

        // 가장 높은 달성률 날짜
        val bestDay =
            statistics
                .maxByOrNull {
                    it.completionRate
                }
                ?.date

        _uiState.update {
            it.copy(
                statistics = statistics,
                averageCompletionRate =
                    averageCompletionRate,
                totalCompletedCount =
                    totalCompletedCount,
                bestDay = bestDay,
                isLoading = false
            )
        }
    }

    // 이번 달 통계 조회
    fun loadMonthlyStatistics() {

        loadStatistics(
            period =
                StatisticsPeriod.MONTHLY
        )
    }

    // 최근 30일 카테고리별 통계 조회
    fun loadCategoryStatistics() {

        viewModelScope.launch {

            try {

                val categoryStatistics =
                    statisticsRepository
                        .getCategoryStatistics()

                _uiState.update {
                    it.copy(
                        categoryStatistics =
                            categoryStatistics
                    )
                }

            } catch (e: Exception) {

                val message =
                    if (
                        e is HttpException
                    ) {

                        apiErrorParser
                            .parseMessage(e)
                            ?: "카테고리 통계를 불러오지 못했습니다."

                    } else {

                        "카테고리 통계를 불러오지 못했습니다."
                    }

                _uiState.update {
                    it.copy(
                        errorMessage = message
                    )
                }
            }
        }
    }

    // 연속 달성일 조회
    fun loadStreakStatistics() {

        viewModelScope.launch {

            try {

                val streak =
                    statisticsRepository
                        .getStreakStatistics()

                _uiState.update {
                    it.copy(
                        currentStreak =
                            streak.currentStreak,

                        longestStreak =
                            streak.longestStreak
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        errorMessage =
                            "연속 달성 기록을 불러오지 못했습니다."
                    )
                }
            }
        }
    }
}