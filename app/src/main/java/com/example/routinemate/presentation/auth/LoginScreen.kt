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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    // ViewModel의 상태를 Compose에서 관찰
    val uiState by viewModel.uiState.collectAsState()

    // 로그인 성공 상태가 되면 화면 이동 요청
    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoginSuccess()
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
            text = "루틴메이트 로그인"
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

        Button(
            // 로그인 중에는 중복 요청 방지
            enabled = !uiState.isLoading,
            onClick = {
                viewModel.login()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("로그인")
        }

        // 회원가입 화면으로 이동
        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("계정이 없나요? 회원가입")
        }

        if (uiState.isLoading) {
            // 로그인 요청 중 표시
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        uiState.errorMessage?.let { message ->
            // 로그인 실패 메시지
            Text(
                text = message,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}