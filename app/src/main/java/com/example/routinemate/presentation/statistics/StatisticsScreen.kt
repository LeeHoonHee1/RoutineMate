package com.example.routinemate.presentation.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 화면에 처음 진입했을 때 최근 7일 통계 조회
    LaunchedEffect(Unit) {
        viewModel.loadWeeklyStatistics()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "최근 7일 통계"
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // 최근 7일 평균 달성률
        Text(
            text = "평균 달성률 ${uiState.averageCompletionRate}%"
        )

        LinearProgressIndicator(
            progress = {
                uiState.averageCompletionRate / 100f
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // 최근 7일 동안 완료한 총 횟수
        Text(
            text = "총 완료 횟수 ${uiState.totalCompletedCount}회"
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        // delegated state의 nullable 값을 로컬 변수로 고정
        val bestDay = uiState.bestDay

        Text(
            text = if (bestDay != null) {
                "가장 잘한 날 ${formatStatisticsDate(bestDay)}"
            } else {
                "가장 잘한 날 없음"
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (uiState.isLoading) {

            // 통계 데이터 로딩 중
            CircularProgressIndicator()

        } else if (uiState.errorMessage != null) {

            // 서버 통계 조회 실패
            Text(
                text = uiState.errorMessage ?: ""
            )

        } else {

            // 최근 7일 날짜별 통계 표시
            LazyColumn {

                items(uiState.weeklyStatistics) { statistics ->

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {

                        // 날짜를 "8/11 (화)" 형식으로 표시
                        Text(
                            text = formatStatisticsDate(statistics.date)
                        )

                        Text(
                            text = "${statistics.completedHabitCount} / " +
                                    "${statistics.totalHabitCount} 완료"
                        )

                        Text(
                            text = "달성률 ${statistics.completionRate}%"
                        )

                        LinearProgressIndicator(
                            progress = {
                                statistics.completionRate / 100f
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// 서버 날짜를 "8/11 (화)" 형식으로 변환
private fun formatStatisticsDate(
    dateString: String
): String {

    val date = LocalDate.parse(dateString)

    val dayOfWeek = date.dayOfWeek.getDisplayName(
        TextStyle.SHORT,
        Locale.KOREAN
    )

    return "${date.monthValue}/${date.dayOfMonth} ($dayOfWeek)"
}