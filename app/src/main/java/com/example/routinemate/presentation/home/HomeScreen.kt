package com.example.routinemate.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Home 화면 진입 시 필요한 데이터 조회
    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "홈"
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (uiState.isLoading) {

            // Home 데이터 로딩 중
            CircularProgressIndicator()

        } else if (uiState.errorMessage != null) {

            // Home 데이터 조회 실패
            Column {

                Text(
                    text = "홈 정보를 불러오지 못했습니다."
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = uiState.errorMessage ?: ""
                )
            }

        } else {

            // 오늘 진행 상황
            Text(
                text = "오늘의 진행"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "${uiState.completedHabitCount} / " +
                        "${uiState.totalHabitCount} 완료"
            )

            Text(
                text = "오늘 달성률 ${uiState.todayCompletionRate}%"
            )

            LinearProgressIndicator(
                progress = {
                    uiState.todayCompletionRate / 100f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // 오늘의 루틴 제목
            Text(
                text = "오늘의 루틴"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // 오늘 Habit이 없을 때
            if (uiState.todayHabits.isEmpty()) {

                Column {

                    Text(
                        text = "아직 등록된 습관이 없습니다."
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "습관 탭에서 첫 루틴을 만들어보세요."
                    )
                }

            } else {

                // 오늘 Habit 목록 표시
                LazyColumn {

                    items(uiState.todayHabits) { habit ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {

                            Checkbox(
                                checked = habit.isCompletedToday,
                                onCheckedChange = {
                                    viewModel.toggleHabitCompletion(
                                        habitId = habit.id,
                                        isCompletedToday = habit.isCompletedToday
                                    )
                                },
                                enabled = !uiState.isLoading
                            )

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            Column {

                                Text(
                                    text = habit.title
                                )

                                if (!habit.description.isNullOrBlank()) {
                                    Text(
                                        text = habit.description
                                    )
                                }
                            }
                        }
                    }

                    // 최근 7일 성과
                    item {

                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        Text(
                            text = "최근 성과"
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Text(
                            text = "7일 평균 달성률 " +
                                    "${uiState.weeklyAverageCompletionRate}%"
                        )

                        Text(
                            text = "총 완료 횟수 " +
                                    "${uiState.weeklyTotalCompletedCount}회"
                        )

                        val bestDay = uiState.bestDay

                        Text(
                            text = if (bestDay != null) {
                                "가장 잘한 날 ${formatHomeDate(bestDay)}"
                            } else {
                                "가장 잘한 날 없음"
                            }
                        )
                    }
                }
            }
        }
    }
}

// 서버 날짜를 "8/11 (화)" 형식으로 변환
private fun formatHomeDate(
    dateString: String
): String {

    val date = LocalDate.parse(dateString)

    val dayOfWeek = date.dayOfWeek.getDisplayName(
        TextStyle.SHORT,
        Locale.KOREAN
    )

    return "${date.monthValue}/${date.dayOfMonth} ($dayOfWeek)"
}