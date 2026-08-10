package com.example.routinemate.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.routinemate.presentation.auth.AuthViewModel
import androidx.compose.material3.Button

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel
) {
    // ViewModel 상태 관찰
    val uiState by viewModel.uiState.collectAsState()

    // 프로필 화면 진입 시 사용자 정보 조회
    LaunchedEffect(Unit) {
        viewModel.loadMe()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "마이페이지"
        )

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        uiState.currentUser?.let { user ->

            Text(
                text = "닉네임: ${user.nickname}",
                modifier = Modifier.padding(top = 24.dp)
            )

            Text(
                text = "이메일: ${user.email}",
                modifier = Modifier.padding(top = 8.dp)
            )

            Button(
                onClick = {
                    viewModel.logout()
                },
                modifier = Modifier.padding(top = 24.dp)
            ) {
                Text("로그아웃")
            }
        }

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}