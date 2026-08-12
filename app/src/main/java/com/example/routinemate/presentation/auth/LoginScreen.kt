package com.example.routinemate.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.routinemate.ui.theme.RoutineDimens

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {

    // ViewModel 상태 관찰
    val uiState by viewModel.uiState.collectAsState()

    // 로그인 성공 시 Home 이동
    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(RoutineDimens.ScreenPadding),
        verticalArrangement = Arrangement.Center
    ) {

        // 앱 이름
        Text(
            text = "RoutineMate",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(
                RoutineDimens.SmallSpacing
            )
        )

        Text(
            text = "오늘의 작은 습관을 시작해보세요.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(
                RoutineDimens.SectionSpacing
            )
        )

        // 로그인 입력 카드
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {

            Column(
                modifier = Modifier.padding(
                    RoutineDimens.CardPadding
                )
            ) {

                Text(
                    text = "로그인",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.ItemSpacing
                    )
                )

                // 이메일
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChange,
                    label = {
                        Text("이메일")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.ItemSpacing
                    )
                )

                // 비밀번호
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = {
                        Text("비밀번호")
                    },
                    visualTransformation =
                        PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.SectionSpacing
                    )
                )

                // 로그인 버튼
                Button(
                    enabled = !uiState.isLoading,
                    onClick = {
                        viewModel.login()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    if (uiState.isLoading) {

                        CircularProgressIndicator(
                            color =
                                MaterialTheme.colorScheme.onPrimary
                        )

                    } else {

                        Text("로그인")
                    }
                }

                // 오류 메시지
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

        Spacer(
            modifier = Modifier.height(
                RoutineDimens.ItemSpacing
            )
        )

        // 회원가입 이동
        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            )
        ) {

            Text(
                text = "계정이 없나요? 회원가입",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}