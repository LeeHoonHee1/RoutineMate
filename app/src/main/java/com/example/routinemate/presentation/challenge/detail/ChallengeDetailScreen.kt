package com.example.routinemate.presentation.challenge.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.routinemate.ui.theme.RoutineAccentOrange
import com.example.routinemate.ui.theme.RoutineDimens

@Composable
fun ChallengeDetailScreen(
    challengeId: Long,
    viewModel: ChallengeDetailViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState =
        remember { SnackbarHostState() }

    // 상세 데이터 조회
    LaunchedEffect(challengeId) {
        viewModel.loadChallengeDetail(
            challengeId = challengeId
        )
    }

    // 성공 메시지
    LaunchedEffect(uiState.successMessage) {

        uiState.successMessage?.let { message ->

            snackbarHostState.showSnackbar(
                message = message
            )

            viewModel.clearMessage()
        }
    }

    // 실패 메시지
    LaunchedEffect(uiState.errorMessage) {

        uiState.errorMessage?.let { message ->

            snackbarHostState.showSnackbar(
                message = message
            )

            viewModel.clearMessage()
        }
    }

    Scaffold(
        containerColor =
            MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { innerPadding ->

        when {

            // 최초 로딩
            uiState.isLoading &&
                    uiState.detail == null -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {

                    CircularProgressIndicator()
                }
            }

            uiState.detail != null -> {

                val detail = uiState.detail!!
                val challenge = detail.challenge
                val myProgress = detail.myProgress

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(
                            RoutineDimens.ScreenPadding
                        ),
                    verticalArrangement =
                        Arrangement.spacedBy(
                            RoutineDimens.ItemSpacing
                        )
                ) {

                    // 챌린지 기본 정보
                    item {

                        Text(
                            text = challenge.title,
                            style =
                                MaterialTheme.typography.headlineSmall
                        )

                        if (
                            !challenge.description
                                .isNullOrBlank()
                        ) {

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        RoutineDimens.SmallSpacing
                                    )
                            )

                            Text(
                                text =
                                    challenge.description,
                                style =
                                    MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(
                                    RoutineDimens.SmallSpacing
                                )
                        )

                        Text(
                            text =
                                "${challenge.startDate} ~ ${challenge.endDate}",
                            style =
                                MaterialTheme.typography.bodySmall,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    RoutineDimens.SmallSpacing
                                )
                        )

                        Text(
                            text =
                                "생성자: ${challenge.ownerNickname}",
                            style =
                                MaterialTheme.typography.bodySmall,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // 내 진행률
                    item {

                        Card(
                            modifier =
                                Modifier.fillMaxWidth(),
                            shape =
                                MaterialTheme.shapes.large,
                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        MaterialTheme.colorScheme.primaryContainer
                                )
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(
                                        RoutineDimens.CardPadding
                                    )
                            ) {

                                Text(
                                    text = "내 진행률",
                                    style =
                                        MaterialTheme.typography.titleMedium,
                                    color =
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            RoutineDimens.ContentSpacing
                                        )
                                )

                                Text(
                                    text =
                                        "${myProgress.progressRate}%",
                                    style =
                                        MaterialTheme.typography.headlineLarge,
                                    color =
                                        RoutineAccentOrange
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            RoutineDimens.ContentSpacing
                                        )
                                )

                                LinearProgressIndicator(
                                    progress = {
                                        myProgress.progressRate / 100f
                                    },
                                    modifier =
                                        Modifier.fillMaxWidth(),
                                    color =
                                        MaterialTheme.colorScheme.primary,
                                    trackColor =
                                        MaterialTheme.colorScheme.surface
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            RoutineDimens.ContentSpacing
                                        )
                                )

                                Text(
                                    text =
                                        "${myProgress.totalDays}일 중 " +
                                                "${myProgress.completedDays}일 완료",
                                    style =
                                        MaterialTheme.typography.bodyMedium,
                                    color =
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    // 오늘 챌린지
                    item {

                        Text(
                            text = "오늘의 챌린지",
                            style =
                                MaterialTheme.typography.titleLarge
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    RoutineDimens.ContentSpacing
                                )
                        )

                        if (
                            myProgress.isCompletedToday
                        ) {

                            OutlinedButton(
                                onClick = {
                                    viewModel
                                        .cancelChallengeCompletion(
                                            challengeId =
                                                challengeId
                                        )
                                },
                                modifier =
                                    Modifier.fillMaxWidth()
                            ) {

                                Text(
                                    "오늘 완료 취소"
                                )
                            }

                        } else {

                            Button(
                                onClick = {
                                    viewModel.completeChallenge(
                                        challengeId =
                                            challengeId
                                    )
                                },
                                modifier =
                                    Modifier.fillMaxWidth()
                            ) {

                                Text(
                                    "오늘 완료하기"
                                )
                            }
                        }
                    }

                    // 참여자 진행 현황
                    item {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    RoutineDimens.ContentSpacing
                                )
                        )

                        Text(
                            text = "참여자 진행 현황",
                            style =
                                MaterialTheme.typography.titleLarge
                        )
                    }

                    items(
                        items = detail.participants,
                        key = { participant ->
                            participant.userId
                        }
                    ) { participant ->

                        Card(
                            modifier =
                                Modifier.fillMaxWidth(),
                            shape =
                                MaterialTheme.shapes.medium,
                            colors =
                                CardDefaults.cardColors(
                                    containerColor =
                                        MaterialTheme.colorScheme.surfaceVariant
                                )
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(
                                        RoutineDimens.CardPadding
                                    )
                            ) {

                                Row(
                                    modifier =
                                        Modifier.fillMaxWidth(),
                                    horizontalArrangement =
                                        Arrangement.SpaceBetween,
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Column {

                                        Text(
                                            text =
                                                participant.nickname,
                                            style =
                                                MaterialTheme.typography.titleMedium
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.height(
                                                    RoutineDimens.SmallSpacing
                                                )
                                        )

                                        Text(
                                            text =
                                                if (
                                                    participant.role ==
                                                    "OWNER"
                                                ) {
                                                    "생성자"
                                                } else {
                                                    "참여자"
                                                },
                                            style =
                                                MaterialTheme.typography.bodySmall,
                                            color =
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Text(
                                        text =
                                            "${participant.progressRate}%",
                                        style =
                                            MaterialTheme.typography.titleLarge,
                                        color =
                                            RoutineAccentOrange
                                    )
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            RoutineDimens.ContentSpacing
                                        )
                                )

                                LinearProgressIndicator(
                                    progress = {
                                        participant.progressRate /
                                                100f
                                    },
                                    modifier =
                                        Modifier.fillMaxWidth()
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            RoutineDimens.SmallSpacing
                                        )
                                )

                                Text(
                                    text =
                                        "${participant.completedDays}/${participant.totalDays}일 완료" +
                                                if (
                                                    participant.isCompletedToday
                                                ) {
                                                    " · 오늘 완료"
                                                } else {
                                                    ""
                                                },
                                    style =
                                        MaterialTheme.typography.bodyMedium,
                                    color =
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            else -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(
                            RoutineDimens.ScreenPadding
                        ),
                    verticalArrangement =
                        Arrangement.Center,
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text =
                            "챌린지 정보를 불러올 수 없습니다.",
                        style =
                            MaterialTheme.typography.bodyLarge,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}