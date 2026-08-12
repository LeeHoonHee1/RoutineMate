package com.example.routinemate.presentation.auth

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.routinemate.ui.theme.RoutineDimens

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onSignupSuccess: () -> Unit
) {

    // ViewModel 상태 관찰
    val uiState by viewModel.uiState.collectAsState()

    // 회원가입 성공 시 화면 이동
    LaunchedEffect(uiState.isSignupSuccess) {
        if (uiState.isSignupSuccess) {
            onSignupSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(RoutineDimens.ScreenPadding)
    ) {

        Spacer(
            modifier = Modifier.weight(1f)
        )

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
            text = "나만의 루틴을 만들고 꾸준히 이어가보세요.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(
                RoutineDimens.SectionSpacing
            )
        )

        // 회원가입 입력 카드
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
                    text = "회원가입",
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
                        RoutineDimens.ItemSpacing
                    )
                )

                // 닉네임
                OutlinedTextField(
                    value = uiState.nickname,
                    onValueChange = viewModel::onNicknameChange,
                    label = {
                        Text("닉네임")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.SectionSpacing
                    )
                )

                // 회원가입 버튼
                Button(
                    enabled = !uiState.isLoading,
                    onClick = {
                        viewModel.signup()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    if (uiState.isLoading) {

                        CircularProgressIndicator(
                            color =
                                MaterialTheme.colorScheme.onPrimary
                        )

                    } else {

                        Text("회원가입")
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
            modifier = Modifier.weight(1f)
        )
    }
}