package com.example.routinemate.presentation.habit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.routinemate.data.remote.dto.habit.HabitResponse
import com.example.routinemate.data.remote.dto.habit.HabitCategory
import com.example.routinemate.data.remote.dto.habit.HabitRepeatDay

@Composable
fun HabitScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {

    val uiState by
    viewModel.uiState.collectAsStateWithLifecycle()

    var deleteHabitId by remember {
        mutableStateOf<Long?>(null)
    }

    var showHabitDialog by remember {
        mutableStateOf(false)
    }

    // 화면 진입 시 습관 목록 조회
    LaunchedEffect(Unit) {
        viewModel.loadHabits()
    }

    // 생성 / 수정 성공했을 때만 다이얼로그 닫기
    LaunchedEffect(
        uiState.isSaveSuccess
    ) {

        if (
            uiState.isSaveSuccess
        ) {

            showHabitDialog = false

            viewModel.consumeSaveSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFFAF9F5)
            )
    ) {

        LazyColumn(
            modifier =
                Modifier.fillMaxSize(),
            contentPadding =
                PaddingValues(
                    bottom = 32.dp
                ),
            verticalArrangement =
                Arrangement.spacedBy(
                    24.dp
                )
        ) {

            // 상단 영역
            item {

                HabitHeader()
            }

            // 오류
            if (
                uiState.errorMessage != null
            ) {

                item {

                    Text(
                        text =
                            uiState.errorMessage
                                ?: "",
                        modifier =
                            Modifier.padding(
                                horizontal = 20.dp
                            ),
                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }
            }

            // 로딩
            if (uiState.isLoading) {

                item {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        CircularProgressIndicator()
                    }
                }

            } else {

                // 오늘 진행 카드
                item {

                    Box(
                        modifier =
                            Modifier.padding(
                                horizontal = 20.dp
                            )
                    ) {

                        HabitProgressCard(
                            completedCount =
                                uiState
                                    .completedHabitCount,
                            totalCount =
                                uiState
                                    .totalHabitCount,
                            completionRate =
                                uiState
                                    .completionRate
                        )
                    }
                }

                // 오늘의 루틴
                item {

                    Column {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal =
                                        20.dp
                                ),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                text =
                                    "오늘의 루틴",
                                style =
                                    MaterialTheme
                                        .typography
                                        .titleLarge,
                                fontWeight =
                                    FontWeight.Bold,
                                color =
                                    Color(
                                        0xFF123D33
                                    )
                            )

                            Spacer(
                                modifier =
                                    Modifier.weight(
                                        1f
                                    )
                            )

                            Text(
                                text =
                                    "${uiState.totalHabitCount}개의 루틴",
                                style =
                                    MaterialTheme
                                        .typography
                                        .bodyMedium,
                                color =
                                    Color(
                                        0xFF7B827E
                                    )
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )

                        if (
                            uiState.todayHabits
                                .isEmpty()
                        ) {

                            EmptyTodayHabitCard()

                        } else {

                            TodayRoutineCard(
                                habits =
                                    uiState.todayHabits,
                                onToggle = {
                                        habit ->

                                    viewModel
                                        .toggleHabitCompletion(
                                            habitId =
                                                habit.id,
                                            isCompletedToday =
                                                habit.isCompletedToday
                                        )
                                }
                            )
                        }
                    }
                }

                // 내 습관
                if (
                    uiState.habits
                        .isNotEmpty()
                ) {

                    item {

                        Column {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal =
                                            20.dp
                                    ),
                                verticalAlignment =
                                    Alignment
                                        .CenterVertically
                            ) {

                                Text(
                                    text =
                                        "내 습관",
                                    style =
                                        MaterialTheme
                                            .typography
                                            .titleLarge,
                                    fontWeight =
                                        FontWeight.Bold,
                                    color =
                                        Color(
                                            0xFF123D33
                                        )
                                )

                                Spacer(
                                    modifier =
                                        Modifier.weight(
                                            1f
                                        )
                                )

                                Text(
                                    text =
                                        "모두 보기 ›",
                                    style =
                                        MaterialTheme
                                            .typography
                                            .bodyMedium,
                                    color =
                                        Color(
                                            0xFF777E79
                                        )
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        12.dp
                                    )
                            )

                            HabitManagementRow(
                                habits =
                                    uiState.habits,
                                onEdit = {
                                        habitId ->

                                    viewModel
                                        .startEditing(
                                            habitId
                                        )

                                    showHabitDialog =
                                        true
                                },
                                onDelete = {
                                        habitId ->

                                    deleteHabitId =
                                        habitId
                                }
                            )
                        }
                    }
                }

                // 습관 만들기
                item {

                    Button(
                        onClick = {

                            viewModel
                                .cancelEditing()

                            showHabitDialog =
                                true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal =
                                    20.dp
                            )
                            .height(58.dp),
                        shape =
                            RoundedCornerShape(
                                22.dp
                            ),
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        Color(
                                            0xFFEF8A3A
                                        ),
                                    contentColor =
                                        Color.White
                                )
                    ) {

                        Text(
                            text =
                                "＋  습관 만들기",
                            style =
                                MaterialTheme
                                    .typography
                                    .titleMedium,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // 생성 / 수정 다이얼로그
    if (showHabitDialog) {

        HabitEditorDialog(
            isEditing =
                uiState.editingHabitId != null,

            selectedCategory =
                uiState.selectedCategory,

            selectedRepeatDays =
                uiState.selectedRepeatDays,

            title =
                uiState.title,

            description =
                uiState.description,

            isSubmitting =
                uiState.isSubmitting,

            inputErrorMessage =
                uiState.inputErrorMessage,

            operationErrorMessage =
                uiState.errorMessage,

            onEveryDaySelected = {
                viewModel.onEveryDayToggle()
            },

            onCategorySelected = { category ->

                // 카테고리 변경
                viewModel.onCategoryChange(
                    category
                )

                // 카테고리에 맞는 기본 이름 자동 입력
                viewModel.onTitleChange(
                    categoryDefaultTitle(
                        category
                    )
                )
            },

            onRepeatDaySelected = { repeatDay ->

                viewModel.onRepeatDayToggle(
                    repeatDay
                )
            },


            onTitleChange =
                viewModel::onTitleChange,

            onDescriptionChange =
                viewModel::onDescriptionChange,

            onDismiss = {

                if (
                    !uiState.isSubmitting
                ) {

                    showHabitDialog =
                        false

                    viewModel.cancelEditing()
                }
            },

            onConfirm = {

                /*
                 * 여기서 다이얼로그를 닫지 않는다.
                 *
                 * 검증 성공 + 서버 저장 성공 후
                 * isSaveSuccess가 true가 되면 닫힌다.
                 */
                if (
                    uiState.editingHabitId == null
                ) {

                    viewModel.createHabit()

                } else {

                    viewModel.updateHabit()
                }
            }
        )
    }

    // 삭제 확인
    if (
        deleteHabitId != null
    ) {

        AlertDialog(
            onDismissRequest = {
                deleteHabitId =
                    null
            },

            title = {
                Text(
                    "습관 삭제"
                )
            },

            text = {
                Text(
                    "이 습관을 삭제하시겠습니까?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        deleteHabitId
                            ?.let {
                                    habitId ->

                                viewModel
                                    .deleteHabit(
                                        habitId
                                    )
                            }

                        deleteHabitId =
                            null
                    }
                ) {

                    Text(
                        text =
                            "삭제",
                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        deleteHabitId =
                            null
                    }
                ) {

                    Text(
                        "취소"
                    )
                }
            }
        )
    }
}

@Composable
private fun HabitHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                Color(0xFFF7F4EA)
            )
    ) {

        // 오른쪽 산 / 해
        HabitLandscape(
            modifier = Modifier
                .align(
                    Alignment.BottomEnd
                )
                .width(230.dp)
                .height(140.dp)
        )

        Column(
            modifier = Modifier
                .align(
                    Alignment.CenterStart
                )
                .padding(
                    start = 20.dp,
                    end = 20.dp
                )
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text = "습관",
                    style =
                        MaterialTheme
                            .typography
                            .displaySmall,
                    fontWeight =
                        FontWeight.ExtraBold,
                    color =
                        Color(
                            0xFF073D32
                        )
                )

                Spacer(
                    modifier =
                        Modifier.width(
                            8.dp
                        )
                )

                Text(
                    text = "🌱",
                    style =
                        MaterialTheme
                            .typography
                            .headlineMedium
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )

            Text(
                text =
                    "오늘의 루틴을 관리해보세요",
                style =
                    MaterialTheme
                        .typography
                        .bodyLarge,
                color =
                    Color(
                        0xFF75827A
                    )
            )
        }
    }
}

@Composable
private fun HabitLandscape(
    modifier: Modifier =
        Modifier
) {

    Canvas(
        modifier =
            modifier
    ) {

        // 해
        drawCircle(
            color =
                Color(
                    0xFFF5B46F
                ),
            radius =
                size.width *
                        0.12f,
            center =
                Offset(
                    size.width *
                            0.72f,
                    size.height *
                            0.33f
                )
        )

        // 뒤쪽 산
        val back =
            Path().apply {

                moveTo(
                    0f,
                    size.height
                )

                lineTo(
                    0f,
                    size.height *
                            0.73f
                )

                cubicTo(
                    size.width *
                            0.22f,
                    size.height *
                            0.54f,
                    size.width *
                            0.35f,
                    size.height *
                            0.48f,
                    size.width *
                            0.50f,
                    size.height *
                            0.62f
                )

                cubicTo(
                    size.width *
                            0.67f,
                    size.height *
                            0.78f,
                    size.width *
                            0.81f,
                    size.height *
                            0.48f,
                    size.width,
                    size.height *
                            0.56f
                )

                lineTo(
                    size.width,
                    size.height
                )

                close()
            }

        drawPath(
            path =
                back,
            color =
                Color(
                    0xFFD8E2D3
                )
        )

        // 앞쪽 산
        val front =
            Path().apply {

                moveTo(
                    0f,
                    size.height
                )

                lineTo(
                    0f,
                    size.height *
                            0.86f
                )

                cubicTo(
                    size.width *
                            0.20f,
                    size.height *
                            0.67f,
                    size.width *
                            0.40f,
                    size.height *
                            0.66f,
                    size.width *
                            0.57f,
                    size.height *
                            0.82f
                )

                cubicTo(
                    size.width *
                            0.72f,
                    size.height *
                            0.95f,
                    size.width *
                            0.89f,
                    size.height *
                            0.70f,
                    size.width,
                    size.height *
                            0.72f
                )

                lineTo(
                    size.width,
                    size.height
                )

                close()
            }

        drawPath(
            path =
                front,
            color =
                Color(
                    0xFFACC1A5
                )
        )
    }
}

@Composable
private fun HabitProgressCard(
    completedCount: Int,
    totalCount: Int,
    completionRate: Int
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(
                28.dp
            ),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults
                .cardElevation(
                    defaultElevation =
                        2.dp
                )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            HabitProgressCircle(
                completedCount =
                    completedCount,
                totalCount =
                    totalCount,
                completionRate =
                    completionRate
            )

            Spacer(
                modifier =
                    Modifier.width(
                        22.dp
                    )
            )

            Column(
                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {

                Text(
                    text =
                        if (
                            completionRate ==
                            100
                        ) {
                            "완벽해요!"
                        } else {
                            "좋아요!"
                        },
                    style =
                        MaterialTheme
                            .typography
                            .titleLarge,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF153E34
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            4.dp
                        )
                )

                Text(
                    text =
                        if (
                            totalCount == 0
                        ) {
                            "첫 습관을 만들어보세요."
                        } else {
                            "오늘도 조금씩 나아가고 있어요."
                        },
                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,
                    color =
                        Color(
                            0xFF607269
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    LinearProgressIndicator(
                        progress = {
                            (
                                    completionRate /
                                            100f
                                    ).coerceIn(
                                    0f,
                                    1f
                                )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(
                                CircleShape
                            ),
                        color =
                            Color(
                                0xFF819A80
                            ),
                        trackColor =
                            Color(
                                0xFFECEDE8
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.width(
                                12.dp
                            )
                    )

                    Text(
                        text =
                            "$completionRate%",
                        color =
                            Color(
                                0xFF617067
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun HabitProgressCircle(
    completedCount: Int,
    totalCount: Int,
    completionRate: Int
) {

    Box(
        modifier =
            Modifier.size(
                105.dp
            ),
        contentAlignment =
            Alignment.Center
    ) {

        CircularProgressIndicator(
            progress = {
                (
                        completionRate /
                                100f
                        ).coerceIn(
                        0f,
                        1f
                    )
            },
            modifier =
                Modifier.fillMaxSize(),
            strokeWidth =
                12.dp,
            color =
                Color(
                    0xFF789174
                ),
            trackColor =
                Color(
                    0xFFEAECE6
                )
        )

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = "🌱"
            )

            Text(
                text =
                    "오늘",
                style =
                    MaterialTheme
                        .typography
                        .bodySmall,
                color =
                    Color(
                        0xFF66746B
                    )
            )

            Text(
                text =
                    "$completedCount / $totalCount",
                style =
                    MaterialTheme
                        .typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(
                        0xFF234A3C
                    )
            )

            Text(
                text =
                    "완료",
                style =
                    MaterialTheme
                        .typography
                        .bodySmall,
                color =
                    Color(
                        0xFF66746B
                    )
            )
        }
    }
}

@Composable
private fun TodayRoutineCard(
    habits:
    List<HabitResponse>,
    onToggle:
        (HabitResponse) ->
    Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal =
                    20.dp
            ),
        shape =
            RoundedCornerShape(
                22.dp
            ),
        colors =
            CardDefaults
                .cardColors(
                    containerColor =
                        Color.White
                )
    ) {

        Column {

            habits.forEachIndexed {
                    index,
                    habit ->

                TodayRoutineRow(
                    habit =
                        habit,
                    onClick = {
                        onToggle(
                            habit
                        )
                    }
                )

                if (
                    index <
                    habits.lastIndex
                ) {

                    HorizontalDivider(
                        modifier =
                            Modifier.padding(
                                horizontal =
                                    18.dp
                            ),
                        color =
                            Color(
                                0xFFF0F0ED
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayRoutineRow(
    habit: HabitResponse,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                horizontal =
                    16.dp,
                vertical =
                    14.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(
                    CircleShape
                )
                .background(
                    if (
                        habit
                            .isCompletedToday
                    ) {
                        Color(
                            0xFF849E83
                        )
                    } else {
                        Color
                            .Transparent
                    }
                )
                .then(
                    if (
                        habit
                            .isCompletedToday
                    ) {
                        Modifier
                    } else {
                        Modifier
                            .background(
                                Color(
                                    0xFFF2F2EF
                                ),
                                CircleShape
                            )
                    }
                ),
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text =
                    if (
                        habit
                            .isCompletedToday
                    ) {
                        "✓"
                    } else {
                        "○"
                    },
                color =
                    if (
                        habit
                            .isCompletedToday
                    ) {
                        Color.White
                    } else {
                        Color(
                            0xFF9B9F9B
                        )
                    },
                fontWeight =
                    FontWeight.Bold
            )
        }

        Spacer(
            modifier =
                Modifier.width(
                    12.dp
                )
        )

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(
                    CircleShape
                )
                .background(
                    Color(
                        0xFFF4F6F0
                    )
                ),
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text =
                    categoryIcon(
                        habit.category
                    ),
                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )
        }

        Spacer(
            modifier =
                Modifier.width(
                    12.dp
                )
        )

        Column(
            modifier =
                Modifier.weight(
                    1f
                )
        ) {

            Text(
                text =
                    habit.title,
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(
                        0xFF25352E
                    )
            )

            if (
                !habit.description
                    .isNullOrBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            3.dp
                        )
                )

                Text(
                    text =
                        habit.description,
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        Color(
                            0xFF8A918C
                        ),
                    maxLines = 1,
                    overflow =
                        TextOverflow
                            .Ellipsis
                )
            }
        }

        Surface(
            shape =
                RoundedCornerShape(
                    50.dp
                ),
            color =
                if (
                    habit
                        .isCompletedToday
                ) {
                    Color(
                        0xFFE7EFE3
                    )
                } else {
                    Color(
                        0xFFFFEEE1
                    )
                }
        ) {

            Text(
                text =
                    if (
                        habit
                            .isCompletedToday
                    ) {
                        "완료"
                    } else {
                        "진행중"
                    },
                modifier =
                    Modifier.padding(
                        horizontal =
                            12.dp,
                        vertical =
                            6.dp
                    ),
                style =
                    MaterialTheme
                        .typography
                        .labelMedium,
                color =
                    if (
                        habit
                            .isCompletedToday
                    ) {
                        Color(
                            0xFF557454
                        )
                    } else {
                        Color(
                            0xFFC36A2D
                        )
                    }
            )
        }
    }
}

@Composable
private fun HabitManagementRow(
    habits:
    List<HabitResponse>,
    onEdit:
        (Long) -> Unit,
    onDelete:
        (Long) -> Unit
) {

    LazyRow(
        contentPadding =
            PaddingValues(
                start = 20.dp,
                end = 20.dp
            ),
        horizontalArrangement =
            Arrangement.spacedBy(
                12.dp
            )
    ) {

        items(
            items =
                habits,
            key = {
                it.id
            }
        ) { habit ->

            HabitManagementCard(
                habit =
                    habit,
                onEdit = {
                    onEdit(
                        habit.id
                    )
                },
                onDelete = {
                    onDelete(
                        habit.id
                    )
                }
            )
        }
    }
}

@Composable
private fun HabitManagementCard(
    habit: HabitResponse,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier =
            Modifier.width(
                180.dp
            ),
        shape =
            RoundedCornerShape(
                22.dp
            ),
        colors =
            CardDefaults
                .cardColors(
                    containerColor =
                        Color.White
                ),
        elevation =
            CardDefaults
                .cardElevation(
                    defaultElevation =
                        1.dp
                )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(
                            46.dp
                        )
                        .clip(
                            RoundedCornerShape(
                                15.dp
                            )
                        )
                        .background(
                            Color(
                                0xFFF0F4EA
                            )
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            categoryIcon(
                                habit.category
                            ),
                        style =
                            MaterialTheme
                                .typography
                                .titleLarge
                    )
                }

                Spacer(
                    modifier =
                        Modifier.weight(
                            1f
                        )
                )

                Text(
                    text =
                        "•••",
                    color =
                        Color(
                            0xFF8C928E
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )

            Text(
                text =
                    habit.title,
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                fontWeight =
                    FontWeight.Bold,
                maxLines = 1,
                overflow =
                    TextOverflow.Ellipsis,
                color =
                    Color(
                        0xFF25362F
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        5.dp
                    )
            )

            Text(
                text =
                    if (
                        habit.description
                            .isNullOrBlank()
                    ) {
                        "꾸준히 이어가는 나만의 습관"
                    } else {
                        habit.description
                    },
                style =
                    MaterialTheme.typography
                        .bodySmall,
                color =
                    Color(
                        0xFF8A918C
                    ),
                minLines = 2,
                maxLines = 2,
                overflow =
                    TextOverflow.Ellipsis
            )

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

// 반복 요일 표시
            Surface(
                shape =
                    RoundedCornerShape(
                        50.dp
                    ),
                color =
                    Color(
                        0xFFF0F4EA
                    )
            ) {

                Text(
                    text =
                        repeatDaysText(
                            habit.repeatDays
                        ),
                    modifier =
                        Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        ),
                    style =
                        MaterialTheme.typography
                            .labelSmall,
                    color =
                        Color(
                            0xFF60745F
                        ),
                    fontWeight =
                        FontWeight.Medium
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )

            Row(
                horizontalArrangement =
                    Arrangement.spacedBy(
                        6.dp
                    )
            ) {

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onEdit()
                        },
                    shape =
                        RoundedCornerShape(
                            50.dp
                        ),
                    color =
                        Color(
                            0xFFF3F3F1
                        )
                ) {

                    Text(
                        text =
                            "수정",
                        modifier =
                            Modifier.padding(
                                vertical =
                                    8.dp
                            ),
                        textAlign =
                            androidx.compose.ui
                                .text.style
                                .TextAlign.Center,
                        style =
                            MaterialTheme
                                .typography
                                .labelMedium
                    )
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onDelete()
                        },
                    shape =
                        RoundedCornerShape(
                            50.dp
                        ),
                    color =
                        Color(
                            0xFFFFEEE7
                        )
                ) {

                    Text(
                        text =
                            "삭제",
                        modifier =
                            Modifier.padding(
                                vertical =
                                    8.dp
                            ),
                        textAlign =
                            androidx.compose.ui
                                .text.style
                                .TextAlign.Center,
                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,
                        color =
                            Color(
                                0xFFC46542
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyHabitCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal =
                    20.dp
            ),
        shape =
            RoundedCornerShape(
                22.dp
            ),
        colors =
            CardDefaults
                .cardColors(
                    containerColor =
                        Color.White
                )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    22.dp
                )
        ) {

            Text(
                text =
                    "아직 등록된 습관이 없어요.",
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(
                        5.dp
                    )
            )

            Text(
                text =
                    "첫 습관을 만들고 오늘부터 시작해보세요.",
                color =
                    Color(
                        0xFF858D87
                    )
            )
        }
    }
}

private fun categoryLabel(
    category: HabitCategory
): String {

    return when (
        category
    ) {

        HabitCategory.WATER ->
            "물"

        HabitCategory.BOOK ->
            "책"

        HabitCategory.SLEEP ->
            "잠"

        HabitCategory.EXERCISE ->
            "운동"

        HabitCategory.STUDY ->
            "공부"
    }
}

private fun categoryIcon(
    category: HabitCategory
): String {

    return when (
        category
    ) {

        HabitCategory.WATER ->
            "💧"

        HabitCategory.BOOK ->
            "📖"

        HabitCategory.SLEEP ->
            "🌙"

        HabitCategory.EXERCISE ->
            "🏃"

        HabitCategory.STUDY ->
            "📚"
    }
}

private fun categoryDefaultTitle(
    category: HabitCategory
): String {

    return when (
        category
    ) {

        HabitCategory.WATER ->
            "물 마시기"

        HabitCategory.BOOK ->
            "책 읽기"

        HabitCategory.SLEEP ->
            "잠자기"

        HabitCategory.EXERCISE ->
            "운동하기"

        HabitCategory.STUDY ->
            "공부하기"
    }
}

@Composable
private fun HabitEditorDialog(
    isEditing: Boolean,
    selectedCategory: HabitCategory?,
    selectedRepeatDays: Set<HabitRepeatDay>,
    title: String,
    description: String,
    isSubmitting: Boolean,
    inputErrorMessage: String?,
    operationErrorMessage: String?,
    onCategorySelected: (HabitCategory) -> Unit,
    onRepeatDaySelected: (HabitRepeatDay) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onEveryDaySelected: () -> Unit,
) {

    val titleError =
        inputErrorMessage
            ?.contains(
                "습관 이름"
            ) == true

    val descriptionError =
        inputErrorMessage
            ?.contains(
                "습관 설명"
            ) == true

    val categoryError =
        inputErrorMessage
            ?.contains(
                "카테고리"
            ) == true

    val repeatDayError =
        inputErrorMessage
            ?.contains(
                "반복 요일"
            ) == true

    AlertDialog(
        onDismissRequest = {

            // 서버 요청 중에는 바깥 터치로 닫히지 않게 함
            if (
                !isSubmitting
            ) {

                onDismiss()
            }
        },

        shape =
            RoundedCornerShape(
                28.dp
            ),

        containerColor =
            Color(0xFFFAF9F5),

        title = {

            Column {

                Text(
                    text =
                        if (
                            isEditing
                        ) {
                            "습관 수정하기"
                        } else {
                            "새 습관 만들기"
                        },
                    style =
                        MaterialTheme.typography
                            .headlineSmall,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF16392F
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            6.dp
                        )
                )

                Text(
                    text =
                        if (
                            isEditing
                        ) {
                            "내 습관을 조금 더 나에게 맞게 바꿔보세요."
                        } else {
                            "작은 습관 하나부터 시작해볼까요?"
                        },
                    style =
                        MaterialTheme.typography
                            .bodyMedium,
                    color =
                        Color(
                            0xFF7A847E
                        )
                )
            }
        },

        text = {

            Column {

                // 카테고리
                Text(
                    text =
                        "어떤 습관인가요?",
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF2A3B33
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                HabitCategorySelector(
                    selectedCategory =
                        selectedCategory,
                    onCategorySelected =
                        onCategorySelected
                )

                if (
                    categoryError
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )

                    Text(
                        text =
                            inputErrorMessage
                                ?: "",
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            22.dp
                        )
                )

                // 반복 요일
                Text(
                    text =
                        "반복 요일",
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF2A3B33
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )

                HabitRepeatDaySelector(
                    selectedRepeatDays =
                        selectedRepeatDays,
                    onEveryDaySelected =
                        onEveryDaySelected,
                    onRepeatDaySelected =
                        onRepeatDaySelected
                )

                if (
                    repeatDayError
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )

                    Text(
                        text =
                            inputErrorMessage
                                ?: "",
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            22.dp
                        )
                )

                // 습관 이름
                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            "습관 이름",
                        style =
                            MaterialTheme.typography
                                .labelLarge,
                        color =
                            Color(
                                0xFF526159
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )

                    Text(
                        text =
                            "${title.length} / 50",
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color =
                            Color(
                                0xFF8A918C
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            7.dp
                        )
                )

                OutlinedTextField(
                    value =
                        title,

                    onValueChange =
                        onTitleChange,

                    modifier =
                        Modifier.fillMaxWidth(),

                    placeholder = {

                        Text(
                            "습관 이름을 입력해주세요."
                        )
                    },

                    singleLine =
                        true,

                    isError =
                        titleError,

                    enabled =
                        !isSubmitting,

                    shape =
                        RoundedCornerShape(
                            16.dp
                        )
                )

                if (
                    titleError
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                5.dp
                            )
                    )

                    Text(
                        text =
                            inputErrorMessage
                                ?: "",
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            18.dp
                        )
                )

                // 설명
                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            "설명",
                        style =
                            MaterialTheme.typography
                                .labelLarge,
                        color =
                            Color(
                                0xFF526159
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )

                    Text(
                        text =
                            "${description.length} / 200",
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color =
                            Color(
                                0xFF8A918C
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            7.dp
                        )
                )

                OutlinedTextField(
                    value =
                        description,

                    onValueChange =
                        onDescriptionChange,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            110.dp
                        ),

                    placeholder = {

                        Text(
                            "이 습관에 대한 짧은 설명을 적어보세요."
                        )
                    },

                    isError =
                        descriptionError,

                    enabled =
                        !isSubmitting,

                    shape =
                        RoundedCornerShape(
                            16.dp
                        )
                )

                if (
                    descriptionError
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                5.dp
                            )
                    )

                    Text(
                        text =
                            inputErrorMessage
                                ?: "",
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }

                // 서버 저장 오류
                if (
                    !operationErrorMessage
                        .isNullOrBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                12.dp
                            )
                    )

                    Surface(
                        shape =
                            RoundedCornerShape(
                                12.dp
                            ),
                        color =
                            Color(
                                0xFFFFEEE7
                            )
                    ) {

                        Text(
                            text =
                                operationErrorMessage,
                            modifier =
                                Modifier.padding(
                                    10.dp
                                ),
                            style =
                                MaterialTheme.typography
                                    .bodySmall,
                            color =
                                Color(
                                    0xFFB55F3E
                                )
                        )
                    }
                }
            }
        },

        confirmButton = {

            Button(
                onClick = {
                    onConfirm()
                },

                // 저장 중일 때만 버튼 막기
                enabled =
                    !isSubmitting,

                shape =
                    RoundedCornerShape(
                        50.dp
                    ),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(
                                0xFF718D71
                            ),
                        contentColor =
                            Color.White
                    )
            ) {

                if (
                    isSubmitting
                ) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(
                                18.dp
                            ),
                        strokeWidth =
                            2.dp,
                        color =
                            Color.White
                    )

                    Spacer(
                        modifier =
                            Modifier.width(
                                8.dp
                            )
                    )

                    Text(
                        text =
                            "저장 중..."
                    )

                } else {

                    Text(
                        text =
                            if (
                                isEditing
                            ) {
                                "수정 완료"
                            } else {
                                "습관 추가"
                            },
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        },

        dismissButton = {

            TextButton(
                onClick = {
                    onDismiss()
                },
                enabled =
                    !isSubmitting
            ) {

                Text(
                    text =
                        "취소",
                    color =
                        Color(
                            0xFF68746D
                        )
                )
            }
        }
    )
}

@Composable
private fun HabitCategorySelector(
    selectedCategory: HabitCategory?,
    onCategorySelected: (HabitCategory) -> Unit
) {

    Column(
        verticalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        // 첫 번째 줄
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            HabitCategoryChip(
                category =
                    HabitCategory.WATER,
                selected =
                    selectedCategory ==
                            HabitCategory.WATER,
                modifier =
                    Modifier.weight(1f),
                onClick =
                    onCategorySelected
            )

            HabitCategoryChip(
                category =
                    HabitCategory.BOOK,
                selected =
                    selectedCategory ==
                            HabitCategory.BOOK,
                modifier =
                    Modifier.weight(1f),
                onClick =
                    onCategorySelected
            )

            HabitCategoryChip(
                category =
                    HabitCategory.SLEEP,
                selected =
                    selectedCategory ==
                            HabitCategory.SLEEP,
                modifier =
                    Modifier.weight(1f),
                onClick =
                    onCategorySelected
            )
        }

        // 두 번째 줄
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            HabitCategoryChip(
                category =
                    HabitCategory.EXERCISE,
                selected =
                    selectedCategory ==
                            HabitCategory.EXERCISE,
                modifier =
                    Modifier.weight(1f),
                onClick =
                    onCategorySelected
            )

            HabitCategoryChip(
                category =
                    HabitCategory.STUDY,
                selected =
                    selectedCategory ==
                            HabitCategory.STUDY,
                modifier =
                    Modifier.weight(1f),
                onClick =
                    onCategorySelected
            )

            // 3칸 정렬을 위한 빈 공간
            Spacer(
                modifier =
                    Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun HabitCategoryChip(
    category: HabitCategory,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (HabitCategory) -> Unit
) {

    Surface(
        modifier = modifier
            .height(76.dp)
            .clickable {

                onClick(
                    category
                )
            },

        shape =
            RoundedCornerShape(18.dp),

        color =
            if (selected) {
                Color(0xFFE1EBDC)
            } else {
                Color(0xFFF3F3EF)
            },

        border =
            if (selected) {

                androidx.compose.foundation.BorderStroke(
                    width = 1.5.dp,
                    color =
                        Color(0xFF789474)
                )

            } else {
                null
            }
    ) {

        Column(
            modifier =
                Modifier.padding(
                    vertical = 10.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {

            Text(
                text =
                    categoryIcon(
                        category
                    ),
                style =
                    MaterialTheme.typography
                        .titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text =
                    categoryLabel(
                        category
                    ),
                style =
                    MaterialTheme.typography
                        .labelLarge,
                fontWeight =
                    if (selected) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Medium
                    },
                color =
                    if (selected) {
                        Color(0xFF476446)
                    } else {
                        Color(0xFF68736C)
                    }
            )
        }
    }
}

@Composable
private fun HabitRepeatDaySelector(
    selectedRepeatDays: Set<HabitRepeatDay>,
    onEveryDaySelected: () -> Unit,
    onRepeatDaySelected: (HabitRepeatDay) -> Unit
) {

    val allDays =
        HabitRepeatDay.entries.toSet()

    val isEveryDaySelected =
        selectedRepeatDays
            .containsAll(
                allDays
            )

    Column(
        verticalArrangement =
            Arrangement.spacedBy(
                10.dp
            )
    ) {

        // 매일 선택
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    42.dp
                )
                .clickable {
                    onEveryDaySelected()
                },
            shape =
                RoundedCornerShape(
                    14.dp
                ),
            color =
                if (
                    isEveryDaySelected
                ) {
                    Color(
                        0xFFE1EBDC
                    )
                } else {
                    Color(
                        0xFFF3F3EF
                    )
                },
            border =
                if (
                    isEveryDaySelected
                ) {

                    androidx.compose.foundation.BorderStroke(
                        width = 1.5.dp,
                        color =
                            Color(
                                0xFF789474
                            )
                    )

                } else {
                    null
                }
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        "매일",
                    style =
                        MaterialTheme.typography
                            .labelLarge,
                    fontWeight =
                        if (
                            isEveryDaySelected
                        ) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Medium
                        },
                    color =
                        if (
                            isEveryDaySelected
                        ) {
                            Color(
                                0xFF476446
                            )
                        } else {
                            Color(
                                0xFF68736C
                            )
                        }
                )
            }
        }

        // 월 ~ 일
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(
                    6.dp
                )
        ) {

            HabitRepeatDay.entries
                .forEach {
                        repeatDay ->

                    HabitRepeatDayChip(
                        repeatDay =
                            repeatDay,
                        selected =
                            selectedRepeatDays
                                .contains(
                                    repeatDay
                                ),
                        modifier =
                            Modifier.weight(
                                1f
                            ),
                        onClick =
                            onRepeatDaySelected
                    )
                }
        }
    }
}

@Composable
private fun HabitRepeatDayChip(
    repeatDay: HabitRepeatDay,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (HabitRepeatDay) -> Unit
) {

    Surface(
        modifier = modifier
            .height(
                42.dp
            )
            .clickable {

                onClick(
                    repeatDay
                )
            },
        shape =
            CircleShape,
        color =
            if (selected) {

                Color(
                    0xFFE1EBDC
                )

            } else {

                Color(
                    0xFFF3F3EF
                )
            },
        border =
            if (selected) {

                androidx.compose.foundation.BorderStroke(
                    width =
                        1.5.dp,
                    color =
                        Color(
                            0xFF789474
                        )
                )

            } else {

                null
            }
    ) {

        Box(
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text =
                    repeatDayLabel(
                        repeatDay
                    ),
                style =
                    MaterialTheme.typography
                        .labelMedium,
                fontWeight =
                    if (selected) {

                        FontWeight.Bold

                    } else {

                        FontWeight.Medium
                    },
                color =
                    if (selected) {

                        Color(
                            0xFF476446
                        )

                    } else {

                        Color(
                            0xFF68736C
                        )
                    }
            )
        }
    }
}

private fun repeatDayLabel(
    repeatDay: HabitRepeatDay
): String {

    return when (
        repeatDay
    ) {

        HabitRepeatDay.MONDAY ->
            "월"

        HabitRepeatDay.TUESDAY ->
            "화"

        HabitRepeatDay.WEDNESDAY ->
            "수"

        HabitRepeatDay.THURSDAY ->
            "목"

        HabitRepeatDay.FRIDAY ->
            "금"

        HabitRepeatDay.SATURDAY ->
            "토"

        HabitRepeatDay.SUNDAY ->
            "일"
    }
}

@Composable
private fun EmptyTodayHabitCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            ),
        shape =
            RoundedCornerShape(
                22.dp
            ),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.White
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    22.dp
                )
        ) {

            Text(
                text =
                    "오늘 예정된 습관이 없어요.",
                style =
                    MaterialTheme.typography
                        .titleMedium,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(
                        5.dp
                    )
            )

            Text(
                text =
                    "오늘은 잠시 쉬어가도 좋아요.",
                color =
                    Color(
                        0xFF858D87
                    )
            )
        }
    }
}

private fun repeatDaysText(
    repeatDays: Set<HabitRepeatDay>
): String {

    val allDays =
        HabitRepeatDay.entries.toSet()

    // 월~일 전부 선택되어 있으면 "매일"
    if (
        repeatDays.containsAll(
            allDays
        )
    ) {
        return "매일"
    }

    return HabitRepeatDay.entries
        .filter {
            repeatDays.contains(
                it
            )
        }
        .joinToString(
            separator = " · "
        ) {
            repeatDayLabel(
                it
            )
        }
}