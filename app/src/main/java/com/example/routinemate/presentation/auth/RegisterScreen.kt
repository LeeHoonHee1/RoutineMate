package com.example.routinemate.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onSignupSuccess: () -> Unit
) {
    // ViewModel의 상태를 Compose에서 관찰
    val uiState by viewModel.uiState.collectAsState()

    // 회원가입 성공 시 외부에 화면 이동 요청
    LaunchedEffect(uiState.isSignupSuccess) {
        if (uiState.isSignupSuccess) {
            onSignupSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "루틴메이트 회원가입"
        )

        OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
            label = {
                Text("이메일")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )

        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = {
                Text("비밀번호")
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        OutlinedTextField(
            value = uiState.nickname,
            onValueChange = viewModel::onNicknameChange,
            label = {
                Text("닉네임")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Button(
            // 요청 중에는 중복 회원가입 방지
            enabled = !uiState.isLoading,
            onClick = {
                viewModel.signup()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("회원가입")
        }

        if (uiState.isLoading) {
            // 서버 응답 대기 중 표시
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        uiState.errorMessage?.let { message ->
            // 회원가입 실패 메시지 표시
            Text(
                text = message,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}