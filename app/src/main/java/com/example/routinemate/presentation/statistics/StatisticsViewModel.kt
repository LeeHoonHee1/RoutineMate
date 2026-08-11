package com.example.routinemate.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val statisticsRepository: StatisticsRepository,
    private val apiErrorParser: ApiErrorParser
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    // 최근 7일 통계 조회
    fun loadWeeklyStatistics() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                // 서버에서 최근 7일 통계 조회
                val weeklyStatistics =
                    statisticsRepository.getWeeklyStatistics()

                // 최근 7일 평균 달성률
                val averageCompletionRate =
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
                val totalCompletedCount = weeklyStatistics.sumOf { statistics ->
                    statistics.completedHabitCount
                }

                // 가장 높은 달성률을 기록한 날
                val bestDay = weeklyStatistics
                    .maxByOrNull { statistics ->
                        statistics.completionRate
                    }
                    ?.date

                _uiState.update {
                    it.copy(
                        weeklyStatistics = weeklyStatistics,
                        averageCompletionRate = averageCompletionRate,
                        totalCompletedCount = totalCompletedCount,
                        bestDay = bestDay,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                val message = if (e is HttpException) {
                    apiErrorParser.parseMessage(e)
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
}