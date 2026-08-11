package com.example.routinemate.presentation.habit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {

    // ViewModel 상태 관찰
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var deleteHabitId by remember {
        mutableStateOf<Long?>(null)
    }

    // 화면 진입 시 습관 목록 조회
    LaunchedEffect(Unit) {
        viewModel.loadHabits()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "내 습관"
        )

        // 습관 이름 입력
        OutlinedTextField(
            value = uiState.title,
            onValueChange = viewModel::onTitleChange,
            label = {
                Text("습관 이름")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            singleLine = true
        )

        // 습관 설명 입력
        OutlinedTextField(
            value = uiState.description,
            onValueChange = viewModel::onDescriptionChange,
            label = {
                Text("설명")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        // 습관 생성 / 수정 완료 버튼
        Button(
            onClick = {
                if (uiState.editingHabitId == null) {
                    viewModel.createHabit()
                } else {
                    viewModel.updateHabit()
                }
            },
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {

            Text(
                text = if (uiState.editingHabitId == null) {
                    "추가"
                } else {
                    "수정 완료"
                }
            )
        }

        // 수정 중일 때만 취소 버튼 표시
        if (uiState.editingHabitId != null) {

            Button(
                onClick = {
                    viewModel.cancelEditing()
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("수정 취소")
            }
        }

        // 오류 메시지
        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        if (uiState.isLoading) {

            CircularProgressIndicator(
                modifier = Modifier.padding(top = 24.dp)
            )

        } else if (uiState.habits.isEmpty()) {

            Text(
                text = "아직 등록된 습관이 없습니다.",
                modifier = Modifier.padding(top = 24.dp)
            )

        } else {

            LazyColumn(
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = uiState.habits,
                    key = { habit -> habit.id }
                ) { habit ->

                    Column {

                        Text(
                            text = habit.title
                        )

                        if (!habit.description.isNullOrBlank()) {
                            Text(
                                text = habit.description
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.startEditing(habit.id)
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("수정")
                        }

                        Button(
                            onClick = {
                                // 바로 삭제하지 않고 확인 다이얼로그 열기
                                deleteHabitId = habit.id
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("삭제")
                        }
                    }
                }
            }
        }
    }

    if (deleteHabitId != null) {

        AlertDialog(
            onDismissRequest = {
                // 다이얼로그 바깥을 누르면 취소
                deleteHabitId = null
            },

            title = {
                Text("습관 삭제")
            },

            text = {
                Text("이 습관을 삭제하시겠습니까?")
            },

            confirmButton = {
                TextButton(
                    onClick = {

                        // 실제 삭제 실행
                        deleteHabitId?.let { habitId ->
                            viewModel.deleteHabit(habitId)
                        }

                        // 다이얼로그 닫기
                        deleteHabitId = null
                    }
                ) {
                    Text("삭제")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        deleteHabitId = null
                    }
                ) {
                    Text("취소")
                }
            }
        )
    }
}