package com.example.routinemate.presentation.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.routinemate.presentation.auth.AuthViewModel
import com.example.routinemate.ui.theme.RoutineDimens

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onFriendClick: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    // 프로필 화면 진입 시 사용자 정보 조회
    LaunchedEffect(Unit) {
        viewModel.loadMe()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(RoutineDimens.ScreenPadding)
    ) {

        // 화면 제목
        Text(
            text = "마이",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.height(
                RoutineDimens.SmallSpacing
            )
        )

        Text(
            text = "내 계정 정보를 확인해보세요.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(
                RoutineDimens.SectionSpacing
            )
        )

        if (uiState.isLoading) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
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
                    text = "프로필 정보를 불러오고 있어요",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        } else {

            uiState.currentUser?.let { user ->

                // 프로필 카드
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
                            text = user.nickname,
                            style = MaterialTheme.typography.headlineSmall,
                            color =
                                MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(
                            modifier = Modifier.height(
                                RoutineDimens.SmallSpacing
                            )
                        )

                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color =
                                MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.SectionSpacing
                    )
                )

                // 활동 영역
                Text(
                    text = "활동",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.ContentSpacing
                    )
                )

                // 친구 화면 진입
                Card(
                    onClick = onFriendClick,
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

                        Text(
                            text = "친구",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(
                            modifier = Modifier.height(
                                RoutineDimens.SmallSpacing
                            )
                        )

                        Text(
                            text = "친구 목록과 요청을 관리해보세요.",
                            style = MaterialTheme.typography.bodyMedium,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.SectionSpacing
                    )
                )

                // 계정 영역
                Text(
                    text = "계정",
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

                        Text(
                            text = "로그인 이메일",
                            style = MaterialTheme.typography.bodySmall,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(
                            modifier = Modifier.height(
                                RoutineDimens.SmallSpacing
                            )
                        )

                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.SectionSpacing
                    )
                )

                // 로그아웃
                TextButton(
                    onClick = {
                        viewModel.logout()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "로그아웃",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        uiState.errorMessage?.let { message ->

            Spacer(
                modifier = Modifier.height(
                    RoutineDimens.ItemSpacing
                )
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}