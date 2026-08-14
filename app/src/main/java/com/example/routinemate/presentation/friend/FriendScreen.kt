package com.example.routinemate.presentation.friend

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
fun FriendScreen(
    viewModel: FriendViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState =
        remember { SnackbarHostState() }

    // 성공 메시지 표시
    LaunchedEffect(uiState.successMessage) {

        val message = uiState.successMessage

        if (message != null) {

            snackbarHostState.showSnackbar(
                message = message
            )

            viewModel.clearMessage()
        }
    }

    // 실패 메시지 표시
    LaunchedEffect(uiState.errorMessage) {

        val message = uiState.errorMessage

        if (message != null) {

            snackbarHostState.showSnackbar(
                message = message
            )

            viewModel.clearMessage()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
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
                    horizontal = RoutineDimens.ScreenPadding,
                    vertical = RoutineDimens.ScreenPadding
                ),
            verticalArrangement =
                Arrangement.spacedBy(
                    RoutineDimens.ItemSpacing
                )
        ) {

            // 화면 제목
            item {

                Text(
                    text = "친구",
                    style =
                        MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.SmallSpacing
                    )
                )

                Text(
                    text =
                        "함께 루틴을 이어갈 친구를 관리해보세요.",
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.ContentSpacing
                    )
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

            // 받은 친구 요청
            item {

                Text(
                    text = "받은 친구 요청",
                    style =
                        MaterialTheme.typography.titleLarge
                )
            }

            if (uiState.receivedRequests.isEmpty()) {

                item {

                    EmptyStateCard(
                        text = "받은 친구 요청이 없어요."
                    )
                }

            } else {

                items(
                    items =
                        uiState.receivedRequests,
                    key = { request ->
                        request.id
                    }
                ) { request ->

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            MaterialTheme.shapes.medium,
                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    MaterialTheme.colorScheme.surface
                            )
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(
                                    RoutineDimens.CardPadding
                                )
                        ) {

                            Text(
                                text =
                                    request.requesterNickname,
                                style =
                                    MaterialTheme.typography.titleMedium
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

                                        viewModel
                                            .acceptFriendRequest(
                                                requestId =
                                                    request.id
                                            )
                                    }
                                ) {

                                    Text("수락")
                                }

                                OutlinedButton(
                                    onClick = {

                                        viewModel
                                            .rejectFriendRequest(
                                                requestId =
                                                    request.id
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

            // 내 친구
            item {

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.ContentSpacing
                    )
                )

                Text(
                    text = "내 친구",
                    style =
                        MaterialTheme.typography.titleLarge
                )
            }

            if (uiState.friends.isEmpty()) {

                item {

                    EmptyStateCard(
                        text = "아직 친구가 없어요."
                    )
                }

            } else {

                items(
                    items = uiState.friends,
                    key = { friend ->
                        friend.id
                    }
                ) { friend ->

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
                                text = friend.nickname,
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
                                text = friend.email,
                                style =
                                    MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 친구 찾기
            item {

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.ContentSpacing
                    )
                )

                Text(
                    text = "친구 찾기",
                    style =
                        MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.ContentSpacing
                    )
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically,
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            RoutineDimens.ItemSpacing
                        )
                ) {

                    OutlinedTextField(
                        value =
                            uiState.searchKeyword,
                        onValueChange =
                            viewModel::onSearchKeywordChange,
                        modifier =
                            Modifier.weight(1f),
                        singleLine = true,
                        shape =
                            MaterialTheme.shapes.medium,
                        placeholder = {
                            Text(
                                "닉네임 또는 이메일"
                            )
                        }
                    )

                    Button(
                        onClick = {
                            viewModel.searchUsers()
                        }
                    ) {

                        Text("검색")
                    }
                }
            }

            // 검색 결과
            items(
                items = uiState.searchResults,
                key = { user ->
                    user.id
                }
            ) { user ->

                Card(
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        MaterialTheme.shapes.medium,
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surface
                        )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                RoutineDimens.CardPadding
                            ),
                        verticalAlignment =
                            Alignment.CenterVertically,
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text = user.nickname,
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
                                text = user.email,
                                style =
                                    MaterialTheme.typography.bodyMedium,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = {

                                viewModel
                                    .sendFriendRequest(
                                        receiverId =
                                            user.id
                                    )
                            }
                        ) {

                            Text("친구 요청")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStateCard(
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