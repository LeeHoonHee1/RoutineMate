package com.example.routinemate.presentation.todayroutine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.routinemate.data.remote.dto.habit.HabitResponse
import com.example.routinemate.ui.theme.RoutineAccentOrange

@Composable
fun TodayRoutineScreen(
    onBackClick: () -> Unit,
    viewModel: TodayRoutineViewModel =
        hiltViewModel()
) {

    val uiState by
    viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFFAFAF7)
            ),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 20.dp,
            bottom = 32.dp
        ),
        verticalArrangement =
            Arrangement.spacedBy(18.dp)
    ) {

        // 상단
        item {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Color(0xFFF0F1EB)
                        )
                        .clickable {
                            onBackClick()
                        },
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = "‹",
                        style =
                            MaterialTheme.typography
                                .headlineMedium,
                        color =
                            Color(0xFF425347)
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(12.dp)
                )

                Column {

                    Text(
                        text = "오늘의 루틴",
                        style =
                            MaterialTheme.typography
                                .headlineMedium,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(0xFF203A30)
                    )

                    Text(
                        text =
                            "오늘 해야 할 습관을 하나씩 완료해보세요.",
                        style =
                            MaterialTheme.typography
                                .bodyMedium,
                        color =
                            Color(0xFF7A827C)
                    )
                }
            }
        }

        if (uiState.isLoading) {

            item {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }

        } else if (
            uiState.errorMessage != null
        ) {

            item {

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(24.dp),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .errorContainer
                        )
                ) {

                    Text(
                        text =
                            uiState.errorMessage
                                ?: "",
                        modifier =
                            Modifier.padding(
                                20.dp
                            ),
                        color =
                            MaterialTheme
                                .colorScheme
                                .onErrorContainer
                    )
                }
            }

        } else {

            // 오늘 진행 요약
            item {

                TodayProgressSummary(
                    completedCount =
                        uiState.completedCount,
                    totalCount =
                        uiState.totalCount,
                    completionRate =
                        uiState.completionRate
                )
            }

            item {

                Text(
                    text = "오늘 할 일",
                    style =
                        MaterialTheme.typography
                            .titleLarge,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(0xFF29372E)
                )
            }

            if (uiState.habits.isEmpty()) {

                item {

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(
                                24.dp
                            ),
                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    Color.White
                            )
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(
                                    24.dp
                                )
                        ) {

                            Text(
                                text =
                                    "오늘 해야 할 루틴이 없어요.",
                                style =
                                    MaterialTheme
                                        .typography
                                        .titleMedium,
                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        6.dp
                                    )
                            )

                            Text(
                                text =
                                    "습관 탭에서 새로운 루틴을 등록해보세요.",
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodyMedium,
                                color =
                                    Color(0xFF808680)
                            )
                        }
                    }
                }

            } else {

                items(
                    items = uiState.habits,
                    key = { habit ->
                        habit.id
                    }
                ) { habit ->

                    TodayRoutineItem(
                        habit = habit,
                        onClick = {

                            viewModel
                                .toggleHabitCompletion(
                                    habitId =
                                        habit.id,
                                    isCompletedToday =
                                        habit
                                            .isCompletedToday
                                )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayProgressSummary(
    completedCount: Int,
    totalCount: Int,
    completionRate: Int
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(28.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFFF1F4EB)
            )
    ) {

        Column(
            modifier =
                Modifier.padding(22.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = "오늘의 진행",
                        style =
                            MaterialTheme.typography
                                .titleMedium,
                        color =
                            Color(0xFF536157)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "$completedCount / $totalCount 완료",
                        style =
                            MaterialTheme.typography
                                .headlineMedium,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(0xFF294536)
                    )
                }

                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )

                Text(
                    text =
                        "$completionRate%",
                    style =
                        MaterialTheme.typography
                            .headlineLarge,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        RoutineAccentOrange
                )
            }

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            LinearProgressIndicator(
                progress = {
                    (
                            completionRate /
                                    100f
                            ).coerceIn(
                            0f,
                            1f
                        )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape),
                color =
                    Color(0xFF80987F),
                trackColor =
                    Color(0xFFDDE2D8)
            )
        }
    }
}

@Composable
private fun TodayRoutineItem(
    habit: HabitResponse,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape =
            RoundedCornerShape(22.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (
                        habit.isCompletedToday
                    ) {
                        Color(0xFFF0F5EC)
                    } else {
                        Color.White
                    }
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 16.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // 완료 원
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (
                            habit.isCompletedToday
                        ) {
                            Color(0xFF829C81)
                        } else {
                            Color(0xFFF0F1ED)
                        }
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        if (
                            habit.isCompletedToday
                        ) {
                            "✓"
                        } else {
                            "○"
                        },
                    style =
                        MaterialTheme.typography
                            .titleLarge,
                    color =
                        if (
                            habit.isCompletedToday
                        ) {
                            Color.White
                        } else {
                            Color(0xFF9A9E99)
                        }
                )
            }

            Spacer(
                modifier =
                    Modifier.width(14.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        habit.title,
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.SemiBold,
                    color =
                        if (
                            habit.isCompletedToday
                        ) {
                            Color(0xFF718075)
                        } else {
                            Color(0xFF28332C)
                        },
                    textDecoration =
                        if (
                            habit.isCompletedToday
                        ) {
                            TextDecoration
                                .LineThrough
                        } else {
                            null
                        }
                )

                if (
                    !habit.description
                        .isNullOrBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            habit.description,
                        style =
                            MaterialTheme.typography
                                .bodyMedium,
                        color =
                            Color(0xFF838983)
                    )
                }
            }

            Text(
                text =
                    if (
                        habit.isCompletedToday
                    ) {
                        "완료"
                    } else {
                        "하기"
                    },
                style =
                    MaterialTheme.typography
                        .labelLarge,
                color =
                    if (
                        habit.isCompletedToday
                    ) {
                        Color(0xFF698169)
                    } else {
                        RoutineAccentOrange
                    }
            )
        }
    }
}