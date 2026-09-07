package com.example.routinemate.presentation.challenge

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.routinemate.ui.theme.RoutineDimens

@Composable
fun ChallengeScreen(
    viewModel: ChallengeViewModel = hiltViewModel()
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

            // 제목
            item {

                Text(
                    text = "챌린지",
                    style =
                        MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            RoutineDimens.SmallSpacing
                        )
                )

                Text(
                    text =
                        "친구와 함께 목표를 이어가보세요.",
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 로딩
            if (uiState.isLoading) {

                item {

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.Center
                    ) {

                        CircularProgressIndicator()
                    }
                }
            }

            // 받은 초대
            item {

                Spacer(
                    modifier =
                        Modifier.height(
                            RoutineDimens.ContentSpacing
                        )
                )

                Text(
                    text = "받은 초대",
                    style =
                        MaterialTheme.typography.titleLarge
                )
            }

            if (uiState.invitations.isEmpty()) {

                item {

                    EmptyChallengeCard(
                        text =
                            "받은 챌린지 초대가 없어요."
                    )
                }

            } else {

                items(
                    items = uiState.invitations,
                    key = { invitation ->
                        invitation.memberId
                    }
                ) { invitation ->

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            MaterialTheme.shapes.medium
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(
                                    RoutineDimens.CardPadding
                                )
                        ) {

                            Text(
                                text =
                                    invitation.title,
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
                                    "초대한 사람: ${invitation.ownerNickname}",
                                style =
                                    MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        RoutineDimens.ContentSpacing
                                    )
                            )

                            Row(
                                horizontalArrangement =
                                    Arrangement.spacedBy(
                                        RoutineDimens.ItemSpacing
                                    )
                            ) {

                                Button(
                                    onClick = {
                                        viewModel.acceptInvitation(
                                            invitation.memberId
                                        )
                                    }
                                ) {
                                    Text("수락")
                                }

                                OutlinedButton(
                                    onClick = {
                                        viewModel.rejectInvitation(
                                            invitation.memberId
                                        )
                                    }
                                ) {
                                    Text("거절")
                                }
                            }
                        }
                    }
                }
            }

            // 내 챌린지
            item {

                Spacer(
                    modifier =
                        Modifier.height(
                            RoutineDimens.ContentSpacing
                        )
                )

                Text(
                    text = "내 챌린지",
                    style =
                        MaterialTheme.typography.titleLarge
                )
            }

            if (uiState.challenges.isEmpty()) {

                item {

                    EmptyChallengeCard(
                        text =
                            "참여 중인 챌린지가 없어요."
                    )
                }

            } else {

                items(
                    items = uiState.challenges,
                    key = { challenge ->
                        challenge.id
                    }
                ) { challenge ->

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

                            Text(
                                text = challenge.title,
                                style =
                                    MaterialTheme.typography.titleMedium
                            )

                            challenge.description
                                ?.takeIf {
                                    it.isNotBlank()
                                }
                                ?.let { description ->

                                    Spacer(
                                        modifier =
                                            Modifier.height(
                                                RoutineDimens.SmallSpacing
                                            )
                                    )

                                    Text(
                                        text = description,
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
                                        RoutineDimens.ContentSpacing
                                    )
                            )

                            Text(
                                text = "친구 초대",
                                style =
                                    MaterialTheme.typography.labelLarge
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        RoutineDimens.SmallSpacing
                                    )
                            )

                            if (uiState.friends.isEmpty()) {

                                Text(
                                    text =
                                        "초대할 친구가 없어요.",
                                    style =
                                        MaterialTheme.typography.bodyMedium,
                                    color =
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )

                            } else {

                                uiState.friends.forEach { friend ->

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                vertical =
                                                    RoutineDimens.SmallSpacing
                                            ),
                                        verticalAlignment =
                                            Alignment.CenterVertically,
                                        horizontalArrangement =
                                            Arrangement.SpaceBetween
                                    ) {

                                        Text(
                                            text =
                                                friend.nickname,
                                            style =
                                                MaterialTheme.typography.bodyMedium
                                        )

                                        OutlinedButton(
                                            onClick = {

                                                viewModel.inviteFriend(
                                                    challengeId =
                                                        challenge.id,
                                                    friendId =
                                                        friend.id
                                                )
                                            }
                                        ) {

                                            Text("초대")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 챌린지 생성
            item {

                Spacer(
                    modifier =
                        Modifier.height(
                            RoutineDimens.ContentSpacing
                        )
                )

                Text(
                    text = "챌린지 만들기",
                    style =
                        MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            RoutineDimens.ContentSpacing
                        )
                )

                OutlinedTextField(
                    value = uiState.title,
                    onValueChange =
                        viewModel::onTitleChange,
                    modifier =
                        Modifier.fillMaxWidth(),
                    label = {
                        Text("챌린지 이름")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            RoutineDimens.ContentSpacing
                        )
                )

                OutlinedTextField(
                    value =
                        uiState.description,
                    onValueChange =
                        viewModel::onDescriptionChange,
                    modifier =
                        Modifier.fillMaxWidth(),
                    label = {
                        Text("설명")
                    }
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            RoutineDimens.ContentSpacing
                        )
                )

                OutlinedTextField(
                    value =
                        uiState.startDate,
                    onValueChange =
                        viewModel::onStartDateChange,
                    modifier =
                        Modifier.fillMaxWidth(),
                    label = {
                        Text("시작일")
                    },
                    placeholder = {
                        Text("2026-09-07")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            RoutineDimens.ContentSpacing
                        )
                )

                OutlinedTextField(
                    value =
                        uiState.endDate,
                    onValueChange =
                        viewModel::onEndDateChange,
                    modifier =
                        Modifier.fillMaxWidth(),
                    label = {
                        Text("종료일")
                    },
                    placeholder = {
                        Text("2026-09-13")
                    },
                    singleLine = true
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            RoutineDimens.ContentSpacing
                        )
                )

                Button(
                    onClick = {
                        viewModel.createChallenge()
                    },
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("챌린지 만들기")
                }
            }
        }
    }
}

@Composable
private fun EmptyChallengeCard(
    text: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Text(
            text = text,
            modifier = Modifier.padding(
                RoutineDimens.CardPadding
            ),
            style =
                MaterialTheme.typography.bodyMedium,
            color =
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}