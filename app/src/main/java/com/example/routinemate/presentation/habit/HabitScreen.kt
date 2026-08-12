package com.example.routinemate.presentation.habit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.routinemate.ui.theme.RoutineAccentOrange
import com.example.routinemate.ui.theme.RoutineDimens

@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var deleteHabitId by remember {
        mutableStateOf<Long?>(null)
    }

    var showHabitDialog by remember {
        mutableStateOf(false)
    }

    // 현재 열려 있는 카드 메뉴의 Habit ID
    var expandedMenuHabitId by remember {
        mutableStateOf<Long?>(null)
    }

    // 화면 진입 시 습관 목록 조회
    LaunchedEffect(Unit) {
        viewModel.loadHabits()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(RoutineDimens.ScreenPadding)
    ) {

        // 화면 제목
        Text(
            text = "내 습관",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.height(
                RoutineDimens.SmallSpacing
            )
        )

        Text(
            text = "오늘을 만드는 작은 루틴을 관리해보세요.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.height(
                RoutineDimens.SectionSpacing
            )
        )

        // 오늘 진행 상황
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
                    text = "오늘의 진행",
                    style = MaterialTheme.typography.titleMedium,
                    color =
                        MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.ContentSpacing
                    )
                )

                Text(
                    text = "${uiState.completionRate}%",
                    style = MaterialTheme.typography.headlineLarge,
                    color = RoutineAccentOrange
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.ContentSpacing
                    )
                )

                LinearProgressIndicator(
                    progress = {
                        uiState.completionRate / 100f
                    },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surface
                )

                Spacer(
                    modifier = Modifier.height(
                        RoutineDimens.ContentSpacing
                    )
                )

                Text(
                    text =
                        "${uiState.totalHabitCount}개 중 " +
                                "${uiState.completedHabitCount}개 완료",
                    style = MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(
            modifier = Modifier.height(
                RoutineDimens.ItemSpacing
            )
        )

        // 새 습관 생성 버튼
        Button(
            onClick = {
                viewModel.cancelEditing()
                showHabitDialog = true
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ 새 습관 만들기")
        }

        // 오류 메시지
        if (uiState.errorMessage != null) {

            Spacer(
                modifier = Modifier.height(
                    RoutineDimens.ItemSpacing
                )
            )

            Text(
                text = uiState.errorMessage!!,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(
            modifier = Modifier.height(
                RoutineDimens.SectionSpacing
            )
        )

        Text(
            text = "등록된 습관",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = Modifier.height(
                RoutineDimens.ContentSpacing
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
                    text = "습관을 불러오고 있어요",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

        } else if (uiState.habits.isEmpty()) {

            // Empty 상태
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {

                Column(
                    modifier = Modifier.padding(
                        RoutineDimens.CardPadding
                    )
                ) {

                    Text(
                        text = "아직 등록된 습관이 없어요",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(
                        modifier = Modifier.height(
                            RoutineDimens.SmallSpacing
                        )
                    )

                    Text(
                        text = "위의 버튼으로 첫 습관을 만들어보세요.",
                        style = MaterialTheme.typography.bodyMedium,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

        } else {

            LazyColumn(
                contentPadding = PaddingValues(
                    bottom = RoutineDimens.ScreenPadding
                ),
                verticalArrangement = Arrangement.spacedBy(
                    RoutineDimens.ItemSpacing
                )
            ) {

                items(
                    items = uiState.habits,
                    key = { habit ->
                        habit.id
                    }
                ) { habit ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (habit.isCompletedToday) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    RoutineDimens.CardPadding
                                ),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            // 오늘 완료 여부 체크
                            Checkbox(
                                checked = habit.isCompletedToday,
                                onCheckedChange = {
                                    viewModel.toggleHabitCompletion(
                                        habitId = habit.id,
                                        isCompletedToday =
                                            habit.isCompletedToday
                                    )
                                },
                                enabled = !uiState.isLoading
                            )

                            Spacer(
                                modifier = Modifier.width(
                                    RoutineDimens.ItemSpacing
                                )
                            )

                            // Habit 정보
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = habit.title,
                                    style =
                                        MaterialTheme.typography.titleMedium,
                                    color =
                                        if (habit.isCompletedToday) {
                                            MaterialTheme.colorScheme
                                                .onPrimaryContainer
                                        } else {
                                            MaterialTheme.colorScheme
                                                .onSurface
                                        }
                                )

                                if (!habit.description.isNullOrBlank()) {

                                    Spacer(
                                        modifier = Modifier.height(
                                            RoutineDimens.SmallSpacing
                                        )
                                    )

                                    Text(
                                        text = habit.description,
                                        style =
                                            MaterialTheme.typography.bodyMedium,
                                        color =
                                            MaterialTheme.colorScheme
                                                .onSurfaceVariant
                                    )
                                }
                            }

                            // 수정 / 삭제 메뉴
                            Column {

                                IconButton(
                                    onClick = {
                                        expandedMenuHabitId =
                                            if (
                                                expandedMenuHabitId ==
                                                habit.id
                                            ) {
                                                null
                                            } else {
                                                habit.id
                                            }
                                    },
                                    enabled = !uiState.isLoading
                                ) {

                                    Text(
                                        text = "⋮",
                                        style =
                                            MaterialTheme.typography.titleLarge,
                                        color =
                                            MaterialTheme.colorScheme
                                                .onSurfaceVariant
                                    )
                                }

                                DropdownMenu(
                                    expanded =
                                        expandedMenuHabitId == habit.id,
                                    onDismissRequest = {
                                        expandedMenuHabitId = null
                                    }
                                ) {

                                    // 수정
                                    DropdownMenuItem(
                                        text = {
                                            Text("수정")
                                        },
                                        onClick = {

                                            expandedMenuHabitId = null

                                            viewModel.startEditing(
                                                habit.id
                                            )

                                            showHabitDialog = true
                                        }
                                    )

                                    // 삭제
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "삭제",
                                                color =
                                                    MaterialTheme.colorScheme
                                                        .error
                                            )
                                        },
                                        onClick = {

                                            expandedMenuHabitId = null

                                            deleteHabitId = habit.id
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // 생성 / 수정 다이얼로그
    if (showHabitDialog) {

        AlertDialog(
            onDismissRequest = {

                showHabitDialog = false

                viewModel.cancelEditing()
            },

            title = {

                Text(
                    text = if (uiState.editingHabitId == null) {
                        "새 습관 만들기"
                    } else {
                        "습관 수정하기"
                    }
                )
            },

            text = {

                Column {

                    // 습관 이름
                    OutlinedTextField(
                        value = uiState.title,
                        onValueChange = viewModel::onTitleChange,
                        label = {
                            Text("습관 이름")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(
                        modifier = Modifier.height(
                            RoutineDimens.ItemSpacing
                        )
                    )

                    // 습관 설명
                    OutlinedTextField(
                        value = uiState.description,
                        onValueChange = viewModel::onDescriptionChange,
                        label = {
                            Text("설명")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        if (uiState.editingHabitId == null) {

                            viewModel.createHabit()

                        } else {

                            viewModel.updateHabit()
                        }

                        showHabitDialog = false
                    },
                    enabled = !uiState.isLoading
                ) {

                    Text(
                        text = if (uiState.editingHabitId == null) {
                            "추가"
                        } else {
                            "수정 완료"
                        }
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {

                        showHabitDialog = false

                        viewModel.cancelEditing()
                    }
                ) {
                    Text("취소")
                }
            }
        )
    }

    // 삭제 확인 다이얼로그
    if (deleteHabitId != null) {

        AlertDialog(
            onDismissRequest = {
                deleteHabitId = null
            },

            title = {
                Text("습관 삭제")
            },

            text = {
                Text(
                    "이 습관을 삭제하시겠습니까?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        deleteHabitId?.let { habitId ->
                            viewModel.deleteHabit(habitId)
                        }

                        deleteHabitId = null
                    }
                ) {

                    Text(
                        text = "삭제",
                        color =
                            MaterialTheme.colorScheme.error
                    )
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