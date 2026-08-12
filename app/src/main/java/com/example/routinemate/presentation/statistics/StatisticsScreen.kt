package com.example.routinemate.presentation.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.routinemate.ui.theme.RoutineAccentOrange
import com.example.routinemate.ui.theme.RoutineDimens
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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            RoutineDimens.ScreenPadding
        ),
        verticalArrangement = Arrangement.spacedBy(
            RoutineDimens.SectionSpacing
        )
    ) {

        // 화면 제목
        item {

            Column {

                Text(
                    text = "통계",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.SmallSpacing
                    )
                )

                Text(
                    text = "최근 7일 동안의 루틴 기록을 확인해보세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (uiState.isLoading) {

            // 통계 데이터 로딩 상태
            item {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                RoutineDimens.CardPadding
                            ),
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(
                                RoutineDimens.ContentSpacing
                            )
                        )

                        Text(
                            text = "통계를 불러오고 있어요",
                            style = MaterialTheme.typography.bodyMedium,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

        } else if (uiState.errorMessage != null) {

            // 통계 조회 실패
            item {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(
                            RoutineDimens.CardPadding
                        )
                    ) {

                        Text(
                            text = "통계를 불러오지 못했어요",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(
                            modifier = Modifier.height(
                                RoutineDimens.ContentSpacing
                            )
                        )

                        Text(
                            text = uiState.errorMessage ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

        } else {

            // 평균 달성률 메인 카드
            item {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.primaryContainer
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(
                            RoutineDimens.CardPadding
                        )
                    ) {

                        Text(
                            text = "7일 평균 달성률",
                            style = MaterialTheme.typography.titleMedium,
                            color =
                                MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(
                            modifier = Modifier.height(
                                RoutineDimens.ContentSpacing
                            )
                        )

                        Text(
                            text = "${uiState.averageCompletionRate}%",
                            style = MaterialTheme.typography.headlineLarge,
                            color = RoutineAccentOrange
                        )

                        Spacer(
                            modifier = Modifier.height(
                                RoutineDimens.ContentSpacing
                            )
                        )

                        LinearProgressIndicator(
                            progress = {
                                uiState.averageCompletionRate / 100f
                            },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }

            // 요약 통계
            item {

                val bestDay = uiState.bestDay

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        RoutineDimens.ItemSpacing
                    )
                ) {

                    // 총 완료 횟수
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(
                                RoutineDimens.CardPadding
                            )
                        ) {

                            Text(
                                text = "총 완료",
                                style = MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme
                                        .onSurfaceVariant
                            )

                            Spacer(
                                modifier = Modifier.height(
                                    RoutineDimens.SmallSpacing
                                )
                            )

                            Text(
                                text =
                                    "${uiState.totalCompletedCount}회",
                                style =
                                    MaterialTheme.typography.headlineSmall,
                                color =
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // 가장 잘한 날
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(
                                RoutineDimens.CardPadding
                            )
                        ) {

                            Text(
                                text = "Best Day",
                                style = MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme
                                        .onSecondaryContainer
                            )

                            Spacer(
                                modifier = Modifier.height(
                                    RoutineDimens.SmallSpacing
                                )
                            )

                            Text(
                                text = if (bestDay != null) {
                                    formatStatisticsDate(bestDay)
                                } else {
                                    "-"
                                },
                                style =
                                    MaterialTheme.typography.titleMedium,
                                color =
                                    MaterialTheme.colorScheme
                                        .onSecondaryContainer
                            )
                        }
                    }
                }
            }

            // 최근 7일 그래프
            if (uiState.weeklyStatistics.isNotEmpty()) {

                item {

                    Column {

                        Text(
                            text = "최근 7일 흐름",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(
                            modifier = Modifier.height(
                                RoutineDimens.ContentSpacing
                            )
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor =
                                    MaterialTheme.colorScheme.surface
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(
                                    RoutineDimens.CardPadding
                                )
                            ) {

                                Text(
                                    text = "일별 달성률",
                                    style =
                                        MaterialTheme.typography.titleMedium,
                                    color =
                                        MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(
                                    modifier = Modifier.height(
                                        RoutineDimens.SectionSpacing
                                    )
                                )

                                // 7일 막대그래프
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement =
                                        Arrangement.spacedBy(
                                            RoutineDimens.ContentSpacing
                                        ),
                                    verticalAlignment =
                                        Alignment.Bottom
                                ) {

                                    uiState.weeklyStatistics.forEach {
                                            statistics ->

                                        Column(
                                            modifier =
                                                Modifier.weight(1f),
                                            horizontalAlignment =
                                                Alignment.CenterHorizontally
                                        ) {

                                            // 달성률 숫자
                                            Text(
                                                text =
                                                    "${statistics.completionRate}%",
                                                style =
                                                    MaterialTheme
                                                        .typography
                                                        .bodySmall,
                                                color =
                                                    if (
                                                        statistics
                                                            .completionRate > 0
                                                    ) {
                                                        RoutineAccentOrange
                                                    } else {
                                                        MaterialTheme
                                                            .colorScheme
                                                            .onSurfaceVariant
                                                    }
                                            )

                                            Spacer(
                                                modifier =
                                                    Modifier.height(
                                                        RoutineDimens
                                                            .SmallSpacing
                                                    )
                                            )

                                            // 그래프 영역
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(120.dp)
                                                    .background(
                                                        color =
                                                            MaterialTheme
                                                                .colorScheme
                                                                .surfaceVariant,
                                                        shape =
                                                            MaterialTheme
                                                                .shapes
                                                                .small
                                                    ),
                                                contentAlignment =
                                                    Alignment.BottomCenter
                                            ) {

                                                // 달성률 막대
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth(
                                                            0.55f
                                                        )
                                                        .height(
                                                            getBarHeight(
                                                                statistics
                                                                    .completionRate
                                                            )
                                                        )
                                                        .background(
                                                            color =
                                                                MaterialTheme
                                                                    .colorScheme
                                                                    .primary,
                                                            shape =
                                                                MaterialTheme
                                                                    .shapes
                                                                    .small
                                                        )
                                                )
                                            }

                                            Spacer(
                                                modifier =
                                                    Modifier.height(
                                                        RoutineDimens
                                                            .ContentSpacing
                                                    )
                                            )

                                            // 요일
                                            Text(
                                                text =
                                                    formatStatisticsDay(
                                                        statistics.date
                                                    ),
                                                style =
                                                    MaterialTheme
                                                        .typography
                                                        .bodyMedium,
                                                color =
                                                    MaterialTheme
                                                        .colorScheme
                                                        .onSurface
                                            )

                                            Spacer(
                                                modifier =
                                                    Modifier.height(
                                                        RoutineDimens
                                                            .SmallSpacing
                                                    )
                                            )

                                            // 날짜
                                            Text(
                                                text =
                                                    formatStatisticsShortDate(
                                                        statistics.date
                                                    ),
                                                style =
                                                    MaterialTheme
                                                        .typography
                                                        .bodySmall,
                                                color =
                                                    MaterialTheme
                                                        .colorScheme
                                                        .onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 날짜별 기록 제목
            item {

                Text(
                    text = "날짜별 기록",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // 최근 7일 날짜별 통계
            items(
                items = uiState.weeklyStatistics,
                key = { statistics ->
                    statistics.date
                }
            ) { statistics ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(
                            RoutineDimens.CardPadding
                        )
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.SpaceBetween,
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Column {

                                // 날짜
                                Text(
                                    text =
                                        formatStatisticsDate(
                                            statistics.date
                                        ),
                                    style =
                                        MaterialTheme.typography.titleMedium,
                                    color =
                                        MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(
                                    modifier = Modifier.height(
                                        RoutineDimens.SmallSpacing
                                    )
                                )

                                Text(
                                    text =
                                        "${statistics.completedHabitCount} / " +
                                                "${statistics.totalHabitCount} 완료",
                                    style =
                                        MaterialTheme.typography.bodyMedium,
                                    color =
                                        MaterialTheme.colorScheme
                                            .onSurfaceVariant
                                )
                            }

                            // 달성률
                            Text(
                                text =
                                    "${statistics.completionRate}%",
                                style =
                                    MaterialTheme.typography.titleLarge,
                                color =
                                    if (statistics.completionRate > 0) {
                                        RoutineAccentOrange
                                    } else {
                                        MaterialTheme.colorScheme
                                            .onSurfaceVariant
                                    }
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(
                                RoutineDimens.ContentSpacing
                            )
                        )

                        LinearProgressIndicator(
                            progress = {
                                statistics.completionRate / 100f
                            },
                            modifier = Modifier.fillMaxWidth(),
                            color =
                                MaterialTheme.colorScheme.primary,
                            trackColor =
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// 달성률에 맞는 그래프 높이 계산
private fun getBarHeight(
    completionRate: Int
) = when {

    completionRate <= 0 -> 4.dp

    else -> {
        (completionRate.coerceIn(0, 100) * 1.2f).dp
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

// 날짜에서 요일만 반환
private fun formatStatisticsDay(
    dateString: String
): String {

    val date = LocalDate.parse(dateString)

    return date.dayOfWeek.getDisplayName(
        TextStyle.SHORT,
        Locale.KOREAN
    )
}

// 날짜를 "8/11" 형식으로 변환
private fun formatStatisticsShortDate(
    dateString: String
): String {

    val date = LocalDate.parse(dateString)

    return "${date.monthValue}/${date.dayOfMonth}"
}