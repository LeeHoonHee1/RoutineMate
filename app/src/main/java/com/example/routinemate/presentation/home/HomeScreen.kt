package com.example.routinemate.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.routinemate.ui.theme.RoutineAccentOrange
import com.example.routinemate.ui.theme.RoutineDimens
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import java.time.LocalTime
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Home 화면 진입 시 필요한 데이터 조회
    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
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

        // 상단 인사말
        item {

            Column {

                Text(
                    text = getGreetingMessage(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.SmallSpacing
                    )
                )

                Text(
                    text = "오늘도 루틴을 이어가 볼까요?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 로딩 상태
        if (uiState.isLoading) {

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
                            modifier = Modifier.size(32.dp),
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier = Modifier.height(
                                RoutineDimens.ContentSpacing
                            )
                        )

                        Text(
                            text = "오늘의 루틴을 불러오고 있어요",
                            style = MaterialTheme.typography.bodyMedium,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

        } else if (uiState.errorMessage != null) {

            // 오류 상태
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
                            text = "홈 정보를 불러오지 못했어요",
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

            // 오늘의 달성률
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
                            text = "오늘의 달성률",
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
                            text = "${uiState.todayCompletionRate}%",
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
                                uiState.todayCompletionRate / 100f
                            },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surface
                        )

                        Spacer(
                            modifier = Modifier.height(
                                RoutineDimens.ContentSpacing
                            )
                        )

                        Text(
                            text =
                                "${uiState.totalHabitCount}개 중 " +
                                        "${uiState.completedHabitCount}개 완료",
                            style = MaterialTheme.typography.bodyMedium,
                            color =
                                MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // 오늘의 루틴 제목
            item {

                Text(
                    text = "오늘의 루틴",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Habit이 없을 때
            if (uiState.todayHabits.isEmpty()) {

                item {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                text = "아직 등록된 루틴이 없어요",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(
                                modifier = Modifier.height(
                                    RoutineDimens.SmallSpacing
                                )
                            )

                            Text(
                                text = "습관 탭에서 첫 루틴을 만들어보세요.",
                                style = MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

            } else {

                // 오늘 Habit 목록
                items(
                    items = uiState.todayHabits,
                    key = { habit ->
                        habit.id
                    }
                ) { habit ->

                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.elevatedCardColors(
                            containerColor =
                                if (habit.isCompletedToday) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    RoutineDimens.CardPadding
                                ),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Checkbox(
                                checked = habit.isCompletedToday,
                                onCheckedChange = {
                                    viewModel.toggleHabitCompletion(
                                        habitId = habit.id,
                                        isCompletedToday =
                                            habit.isCompletedToday
                                    )
                                },
                                enabled = !uiState.isLoading
                            )

                            Spacer(
                                modifier = Modifier.width(
                                    RoutineDimens.ItemSpacing
                                )
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = habit.title,
                                    style =
                                        MaterialTheme.typography.titleMedium,
                                    color =
                                        if (habit.isCompletedToday) {
                                            MaterialTheme.colorScheme
                                                .onPrimaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        }
                                )

                                if (!habit.description.isNullOrBlank()) {

                                    Spacer(
                                        modifier = Modifier.height(
                                            RoutineDimens.SmallSpacing
                                        )
                                    )

                                    Text(
                                        text = habit.description,
                                        style =
                                            MaterialTheme.typography.bodyMedium,
                                        color =
                                            if (habit.isCompletedToday) {
                                                MaterialTheme.colorScheme
                                                    .onPrimaryContainer
                                            } else {
                                                MaterialTheme.colorScheme
                                                    .onSurfaceVariant
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 최근 성과 제목
            item {

                Text(
                    text = "최근 성과",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // 최근 성과 카드
            item {

                val bestDay = uiState.bestDay

                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        RoutineDimens.ItemSpacing
                    )
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            RoutineDimens.ItemSpacing
                        )
                    ) {

                        // 7일 평균
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
                                    text = "7일 평균",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color =
                                        MaterialTheme.colorScheme.onSecondaryContainer
                                )

                                Spacer(
                                    modifier = Modifier.height(
                                        RoutineDimens.SmallSpacing
                                    )
                                )

                                Text(
                                    text =
                                        "${uiState.weeklyAverageCompletionRate}%",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = RoutineAccentOrange
                                )
                            }
                        }

                        // 총 완료
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
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(
                                    modifier = Modifier.height(
                                        RoutineDimens.SmallSpacing
                                    )
                                )

                                Text(
                                    text =
                                        "${uiState.weeklyTotalCompletedCount}회",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    // Best Day
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    RoutineDimens.CardPadding
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {

                                Text(
                                    text = "가장 잘한 날",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color =
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                )

                                Spacer(
                                    modifier = Modifier.height(
                                        RoutineDimens.SmallSpacing
                                    )
                                )

                                Text(
                                    text = "최근 7일 기준",
                                    style = MaterialTheme.typography.bodySmall,
                                    color =
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Text(
                                text = if (bestDay != null) {
                                    formatHomeDate(bestDay)
                                } else {
                                    "-"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                color =
                                    MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
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

// 현재 시간대에 맞는 인사말 반환
private fun getGreetingMessage(): String {

    val currentHour = LocalTime.now().hour

    return when (currentHour) {

        // 오전 5시 ~ 오전 11시 59분
        in 5..11 -> "좋은 아침이에요 👋"

        // 오후 12시 ~ 오후 5시 59분
        in 12..17 -> "좋은 오후예요 👋"

        // 오후 6시 ~ 다음 날 오전 4시 59분
        else -> "좋은 저녁이에요 👋"
    }
}