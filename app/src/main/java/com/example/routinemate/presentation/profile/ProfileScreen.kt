package com.example.routinemate.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun ProfileScreen(
    onHabitClick: () -> Unit,
    onFriendClick: () -> Unit,
    onChallengeClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState =
        remember { SnackbarHostState() }

    // 성공 메시지
    LaunchedEffect(uiState.successMessage) {

        uiState.successMessage?.let { message ->

            snackbarHostState.showSnackbar(
                message = message
            )

            viewModel.clearMessage()
        }
    }

    // 에러 메시지
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

            // 최초 프로필 조회
            uiState.isLoading &&
                    uiState.profile == null -> {

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

            uiState.profile != null -> {

                val profile = uiState.profile!!
                val summary = uiState.summary

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(
                            RoutineDimens.ScreenPadding
                        )
                ) {

                    Text(
                        text = "마이페이지",
                        style =
                            MaterialTheme.typography.headlineSmall
                    )

                    Spacer(
                        modifier = Modifier.height(
                            RoutineDimens.SectionSpacing
                        )
                    )

                    // 프로필 정보
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
                            modifier = Modifier.padding(
                                RoutineDimens.CardPadding
                            )
                        ) {

                            Text(
                                text = profile.nickname,
                                style =
                                    MaterialTheme.typography.headlineSmall,
                                color =
                                    MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Spacer(
                                modifier = Modifier.height(
                                    RoutineDimens.SmallSpacing
                                )
                            )

                            Text(
                                text = profile.email,
                                style =
                                    MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Spacer(
                                modifier = Modifier.height(
                                    RoutineDimens.ContentSpacing
                                )
                            )

                            OutlinedButton(
                                onClick = {
                                    viewModel.startNicknameEdit()
                                }
                            ) {

                                Text("닉네임 수정")
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(
                            RoutineDimens.SectionSpacing
                        )
                    )

                    Text(
                        text = "내 활동",
                        style =
                            MaterialTheme.typography.titleLarge
                    )

                    Spacer(
                        modifier = Modifier.height(
                            RoutineDimens.ItemSpacing
                        )
                    )

                    // 활동 요약
                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(
                                RoutineDimens.ItemSpacing
                            )
                    ) {

                        ProfileSummaryCard(
                            title = "습관",
                            value =
                                summary?.habitCount ?: 0,
                            onClick = onHabitClick,
                            modifier =
                                Modifier.weight(1f)
                        )

                        ProfileSummaryCard(
                            title = "친구",
                            value =
                                summary?.friendCount ?: 0,
                            onClick = onFriendClick,
                            modifier =
                                Modifier.weight(1f)
                        )

                        ProfileSummaryCard(
                            title = "챌린지",
                            value =
                                summary?.challengeCount ?: 0,
                            onClick = onChallengeClick,
                            modifier =
                                Modifier.weight(1f)
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(
                            RoutineDimens.SectionSpacing
                        )
                    )

                    // 친구 관리
                    Card(
                        onClick = onFriendClick,
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            MaterialTheme.shapes.medium
                    ) {

                        Column(
                            modifier = Modifier.padding(
                                RoutineDimens.CardPadding
                            )
                        ) {

                            Text(
                                text = "친구 관리",
                                style =
                                    MaterialTheme.typography.titleMedium
                            )

                            Spacer(
                                modifier = Modifier.height(
                                    RoutineDimens.SmallSpacing
                                )
                            )

                            Text(
                                text =
                                    "친구 검색과 친구 요청을 관리할 수 있어요.",
                                style =
                                    MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    // 로그아웃
                    OutlinedButton(
                        onClick = onLogout,
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text("로그아웃")
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
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Text(
                        text =
                            "프로필 정보를 불러올 수 없습니다.",
                        style =
                            MaterialTheme.typography.bodyLarge
                    )

                    Spacer(
                        modifier = Modifier.height(
                            RoutineDimens.ItemSpacing
                        )
                    )

                    Button(
                        onClick = {
                            viewModel.loadProfile()
                        }
                    ) {

                        Text("다시 시도")
                    }
                }
            }
        }
    }

    // 닉네임 수정 창
    if (uiState.isEditingNickname) {

        AlertDialog(
            onDismissRequest = {
                viewModel.cancelNicknameEdit()
            },
            title = {
                Text("닉네임 수정")
            },
            text = {

                OutlinedTextField(
                    value =
                        uiState.nicknameInput,
                    onValueChange = {
                        viewModel.updateNicknameInput(it)
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    label = {
                        Text("닉네임")
                    },
                    singleLine = true
                )
            },
            confirmButton = {

                TextButton(
                    onClick = {
                        viewModel.saveNickname()
                    }
                ) {

                    Text("저장")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        viewModel.cancelNicknameEdit()
                    }
                ) {

                    Text("취소")
                }
            }
        )
    }
}

@Composable
private fun ProfileSummaryCard(
    title: String,
    value: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        onClick = onClick,
        modifier = modifier,
        shape =
            MaterialTheme.shapes.medium,
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surfaceVariant
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

            Text(
                text = value.toString(),
                style =
                    MaterialTheme.typography.headlineMedium,
                color =
                    RoutineAccentOrange
            )

            Spacer(
                modifier = Modifier.height(
                    RoutineDimens.SmallSpacing
                )
            )

            Text(
                text = title,
                style =
                    MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}