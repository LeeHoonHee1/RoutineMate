package com.example.routinemate.presentation.challenge.overview

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.routinemate.data.remote.dto.challenge.HomeChallengeResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ChallengeOverviewScreen(
    onBackClick: () -> Unit,
    onChallengeClick: (Long) -> Unit,
    viewModel: ChallengeOverviewViewModel = hiltViewModel()
) {

    val uiState by
    viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadChallenges()
    }

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
                        text = "내 챌린지",
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
                            "참여 중인 챌린지를 한눈에 확인해보세요.",
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

        } else if (
            uiState.challenges.isEmpty()
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
                                "참여 중인 챌린지가 없어요.",
                            style =
                                MaterialTheme.typography
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
                                "홈에서 새로운 챌린지를 만들어보세요.",
                            style =
                                MaterialTheme.typography
                                    .bodyMedium,
                            color =
                                Color(0xFF808680)
                        )
                    }
                }
            }

        } else {

            item {

                Text(
                    text =
                        "진행 중 ${uiState.challenges.size}",
                    style =
                        MaterialTheme.typography
                            .titleLarge,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(0xFF29372E)
                )
            }

            items(
                items =
                    uiState.challenges,
                key = { challenge ->
                    challenge.id
                }
            ) { challenge ->

                ChallengeOverviewCard(
                    challenge =
                        challenge,
                    onClick = {
                        onChallengeClick(
                            challenge.id
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ChallengeOverviewCard(
    challenge:
    HomeChallengeResponse,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape =
            RoundedCornerShape(26.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    20.dp
                )
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Color(0xFFF1F4EB)
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = "👥",
                        style =
                            MaterialTheme.typography
                                .titleLarge
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(12.dp)
                )

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            challenge.title,
                        style =
                            MaterialTheme.typography
                                .titleLarge,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(0xFF2B362F),
                        maxLines = 1,
                        overflow =
                            TextOverflow.Ellipsis
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                4.dp
                            )
                    )

                    Text(
                        text =
                            "${formatChallengeDate(challenge.startDate)} ~ " +
                                    formatChallengeDate(
                                        challenge.endDate
                                    ),
                        style =
                            MaterialTheme.typography
                                .bodyMedium,
                        color =
                            Color(0xFF7D837E)
                    )
                }

                Surface(
                    shape =
                        RoundedCornerShape(
                            50.dp
                        ),
                    color =
                        Color(0xFFFFE4CD)
                ) {

                    Text(
                        text =
                            formatDday(
                                challenge
                                    .remainingDays
                            ),
                        modifier =
                            Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                        style =
                            MaterialTheme.typography
                                .labelLarge,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(0xFFB76527)
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )

            Text(
                text =
                    "오늘 성공 현황",
                style =
                    MaterialTheme.typography
                        .bodyMedium,
                color =
                    Color(0xFF69736B)
            )

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            val successRate =
                if (
                    challenge.totalMemberCount == 0L
                ) {
                    0f
                } else {
                    challenge
                        .todayCompletedMemberCount
                        .toFloat() /
                            challenge
                                .totalMemberCount
                                .toFloat()
                }

            LinearProgressIndicator(
                progress = {
                    successRate
                        .coerceIn(
                            0f,
                            1f
                        )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .clip(CircleShape),
                color =
                    Color(0xFF849D84),
                trackColor =
                    Color(0xFFE7EAE4)
            )

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "오늘 ${challenge.todayCompletedMemberCount} / " +
                                "${challenge.totalMemberCount}명 성공",
                    style =
                        MaterialTheme.typography
                            .bodyMedium,
                    color =
                        Color(0xFF5F6861)
                )

                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )

                Text(
                    text =
                        if (
                            challenge
                                .isCompletedToday
                        ) {
                            "✓ 오늘 완료"
                        } else {
                            "○ 오늘 미완료"
                        },
                    style =
                        MaterialTheme.typography
                            .labelLarge,
                    fontWeight =
                        FontWeight.Medium,
                    color =
                        if (
                            challenge
                                .isCompletedToday
                        ) {
                            Color(0xFF617B62)
                        } else {
                            Color(0xFFAA7552)
                        }
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.End
            ) {

                Text(
                    text = "상세보기  ›",
                    style =
                        MaterialTheme.typography
                            .labelLarge,
                    color =
                        Color(0xFF687369)
                )
            }
        }
    }
}

private fun formatDday(
    remainingDays: Long
): String {

    return when {

        remainingDays == 0L ->
            "D-Day"

        remainingDays > 0L ->
            "D-$remainingDays"

        else ->
            "종료"
    }
}

private fun formatChallengeDate(
    dateString: String
): String {

    return try {

        val date =
            LocalDate.parse(
                dateString,
                DateTimeFormatter.ISO_LOCAL_DATE
            )

        "${date.monthValue}/${date.dayOfMonth}"

    } catch (e: Exception) {

        dateString
    }
}