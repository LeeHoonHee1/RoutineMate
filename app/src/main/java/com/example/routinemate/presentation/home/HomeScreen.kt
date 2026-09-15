package com.example.routinemate.presentation.home

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.routinemate.data.remote.dto.challenge.HomeChallengeResponse
import com.example.routinemate.data.remote.dto.habit.HabitResponse
import com.example.routinemate.ui.theme.RoutineAccentOrange
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onChallengeClick: () -> Unit,
    onHabitClick: () -> Unit,
    onChallengeDetailClick: (Long) -> Unit,
    onChallengeCreateClick: () -> Unit
) {

    val uiState by
    viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFFFAFAF7)
            )
    ) {

        // 상단 브랜드 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 34.dp,
                        bottomEnd = 34.dp
                    )
                )
                .background(
                    Color(0xFFF7F3EA)
                )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = 32.dp
            ),
            verticalArrangement =
                Arrangement.spacedBy(24.dp)
        ) {

            // 상단 브랜드 / 히어로
            item {

                Column(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 20.dp
                    )
                ) {

                    RoutineMateHeader()

                    Spacer(
                        modifier =
                            Modifier.height(28.dp)
                    )

                    WelcomeSection()
                }
            }

            when {

                uiState.isLoading -> {

                    item {

                        Box(
                            modifier = Modifier.padding(
                                horizontal = 20.dp
                            )
                        ) {

                            HomeLoadingCard()
                        }
                    }
                }

                uiState.errorMessage != null -> {

                    item {

                        Box(
                            modifier = Modifier.padding(
                                horizontal = 20.dp
                            )
                        ) {

                            HomeErrorCard(
                                message =
                                    uiState.errorMessage
                                        ?: "홈 정보를 불러오지 못했습니다."
                            )
                        }
                    }
                }

                else -> {

                    // 오늘 진행
                    item {

                        Box(
                            modifier = Modifier.padding(
                                horizontal = 20.dp
                            )
                        ) {

                            TodayProgressCard(
                                habits =
                                    uiState.todayHabits,
                                completedCount =
                                    uiState.completedHabitCount,
                                totalCount =
                                    uiState.totalHabitCount,
                                completionRate =
                                    uiState.todayCompletionRate,
                                onViewHabitsClick =
                                    onHabitClick,
                                onToggleHabit = {
                                        habitId,
                                        isCompletedToday ->

                                    viewModel
                                        .toggleHabitCompletion(
                                            habitId =
                                                habitId,
                                            isCompletedToday =
                                                isCompletedToday
                                        )
                                }
                            )
                        }
                    }

                    // 챌린지 헤더
                    item {

                        ChallengeSectionHeader(
                            onCreateClick =
                                onChallengeCreateClick,
                            onViewAllClick =
                                onChallengeClick
                        )
                    }

                    // 챌린지 목록
                    item {

                        if (
                            uiState.homeChallenges
                                .isEmpty()
                        ) {

                            EmptyChallengeCard(
                                onCreateClick =
                                    onChallengeCreateClick
                            )

                        } else {

                            ChallengeLazyRow(
                                challenges =
                                    uiState.homeChallenges,
                                onChallengeClick =
                                    onChallengeDetailClick
                            )
                        }
                    }

                    // 이번 주 성장
                    item {

                        WeeklyGrowthSection(
                            weeklyAverageCompletionRate =
                                uiState
                                    .weeklyAverageCompletionRate,
                            weeklyTotalCompletedCount =
                                uiState
                                    .weeklyTotalCompletedCount,
                            bestDay =
                                uiState.bestDay
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutineMateHeader() {

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        // 새 RoutineMate 브랜드 로고
        BrandLogo()

        Spacer(
            modifier =
                Modifier.width(14.dp)
        )

        Text(
            text = buildAnnotatedString {

                withStyle(
                    SpanStyle(
                        color =
                            Color(0xFF083C32),
                        fontWeight =
                            FontWeight.ExtraBold
                    )
                ) {
                    append("Routine")
                }

                withStyle(
                    SpanStyle(
                        color =
                            Color(0xFF89A184),
                        fontWeight =
                            FontWeight.Bold
                    )
                ) {
                    append("Mate")
                }
            },
            style =
                MaterialTheme.typography
                    .headlineMedium
        )
    }
}

@Composable
private fun BrandLogo() {

    Box(
        modifier = Modifier
            .size(62.dp)
            .clip(
                RoundedCornerShape(20.dp)
            ),
        contentAlignment =
            Alignment.Center
    ) {

        // 유기적인 배경 레이어
        Canvas(
            modifier =
                Modifier.fillMaxSize()
        ) {

            // 가장 연한 배경
            drawCircle(
                color =
                    Color(0xFFDCE8D5),
                radius =
                    size.width * 0.58f,
                center =
                    Offset(
                        size.width * 0.48f,
                        size.height * 0.48f
                    )
            )

            // 뒤쪽 산
            val backHill =
                Path().apply {

                    moveTo(
                        0f,
                        size.height * 0.58f
                    )

                    cubicTo(
                        size.width * 0.20f,
                        size.height * 0.38f,
                        size.width * 0.43f,
                        size.height * 0.38f,
                        size.width * 0.65f,
                        size.height * 0.57f
                    )

                    cubicTo(
                        size.width * 0.82f,
                        size.height * 0.70f,
                        size.width * 0.92f,
                        size.height * 0.64f,
                        size.width,
                        size.height * 0.56f
                    )

                    lineTo(
                        size.width,
                        size.height
                    )

                    lineTo(
                        0f,
                        size.height
                    )

                    close()
                }

            drawPath(
                path =
                    backHill,
                color =
                    Color(0xFFA9BFA2)
            )

            // 앞쪽 산
            val frontHill =
                Path().apply {

                    moveTo(
                        0f,
                        size.height * 0.72f
                    )

                    cubicTo(
                        size.width * 0.21f,
                        size.height * 0.54f,
                        size.width * 0.42f,
                        size.height * 0.50f,
                        size.width * 0.60f,
                        size.height * 0.69f
                    )

                    cubicTo(
                        size.width * 0.76f,
                        size.height * 0.84f,
                        size.width * 0.91f,
                        size.height * 0.76f,
                        size.width,
                        size.height * 0.68f
                    )

                    lineTo(
                        size.width,
                        size.height
                    )

                    lineTo(
                        0f,
                        size.height
                    )

                    close()
                }

            drawPath(
                path =
                    frontHill,
                color =
                    Color(0xFF718E70)
            )

            // 새싹 줄기
            drawLine(
                color =
                    Color(0xFF477044),
                start =
                    Offset(
                        size.width * 0.76f,
                        size.height * 0.36f
                    ),
                end =
                    Offset(
                        size.width * 0.70f,
                        size.height * 0.08f
                    ),
                strokeWidth =
                    3f
            )

            // 왼쪽 잎
            rotate(
                degrees = -30f,
                pivot =
                    Offset(
                        size.width * 0.67f,
                        size.height * 0.17f
                    )
            ) {

                drawOval(
                    color =
                        Color(0xFF5F8C50),
                    topLeft =
                        Offset(
                            size.width * 0.57f,
                            size.height * 0.11f
                        ),
                    size =
                        androidx.compose.ui.geometry.Size(
                            size.width * 0.19f,
                            size.height * 0.10f
                        )
                )
            }

            // 오른쪽 잎
            rotate(
                degrees = 30f,
                pivot =
                    Offset(
                        size.width * 0.76f,
                        size.height * 0.12f
                    )
            ) {

                drawOval(
                    color =
                        Color(0xFF78A460),
                    topLeft =
                        Offset(
                            size.width * 0.70f,
                            size.height * 0.06f
                        ),
                    size =
                        androidx.compose.ui.geometry.Size(
                            size.width * 0.18f,
                            size.height * 0.10f
                        )
                )
            }
        }

        Text(
            text = "RM",
            style =
                MaterialTheme.typography
                    .headlineMedium,
            fontWeight =
                FontWeight.Bold,
            color =
                Color.White
        )
    }
}

@Composable
private fun WelcomeSection() {

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text = "좋은 하루예요! 🌱",
                style =
                    MaterialTheme.typography
                        .headlineMedium,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(0xFF073E33)
            )

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text =
                    "오늘도, 조금씩 더 나아가는\n당신을 응원해요.",
                style =
                    MaterialTheme.typography
                        .bodyLarge,
                color =
                    Color(0xFF708078)
            )
        }

        Spacer(
            modifier =
                Modifier.width(10.dp)
        )

        // 자연스러운 산 + 해 일러스트
        NatureIllustration(
            modifier = Modifier
                .width(155.dp)
                .height(125.dp)
        )
    }
}

@Composable
private fun NatureIllustration(
    modifier: Modifier = Modifier
) {

    Canvas(
        modifier = modifier
    ) {

        /*
         * 해를 산보다 먼저 그려서
         * 실제로 산 뒤에 있는 것처럼 보이게 한다.
         */
        drawCircle(
            color =
                Color(0xFFF4B576),
            radius =
                size.width * 0.16f,
            center =
                Offset(
                    size.width * 0.67f,
                    size.height * 0.44f
                )
        )

        // 가장 뒤 산
        val farMountain =
            Path().apply {

                moveTo(
                    0f,
                    size.height
                )

                lineTo(
                    0f,
                    size.height * 0.72f
                )

                cubicTo(
                    size.width * 0.14f,
                    size.height * 0.64f,
                    size.width * 0.25f,
                    size.height * 0.52f,
                    size.width * 0.38f,
                    size.height * 0.60f
                )

                cubicTo(
                    size.width * 0.53f,
                    size.height * 0.70f,
                    size.width * 0.66f,
                    size.height * 0.38f,
                    size.width * 0.82f,
                    size.height * 0.53f
                )

                cubicTo(
                    size.width * 0.90f,
                    size.height * 0.60f,
                    size.width * 0.95f,
                    size.height * 0.54f,
                    size.width,
                    size.height * 0.48f
                )

                lineTo(
                    size.width,
                    size.height
                )

                close()
            }

        drawPath(
            path =
                farMountain,
            color =
                Color(0xFFD8E3D3)
        )

        // 가운데 산
        val middleMountain =
            Path().apply {

                moveTo(
                    0f,
                    size.height
                )

                lineTo(
                    0f,
                    size.height * 0.78f
                )

                cubicTo(
                    size.width * 0.18f,
                    size.height * 0.61f,
                    size.width * 0.32f,
                    size.height * 0.58f,
                    size.width * 0.46f,
                    size.height * 0.71f
                )

                cubicTo(
                    size.width * 0.59f,
                    size.height * 0.82f,
                    size.width * 0.72f,
                    size.height * 0.61f,
                    size.width * 0.84f,
                    size.height * 0.66f
                )

                cubicTo(
                    size.width * 0.91f,
                    size.height * 0.70f,
                    size.width * 0.96f,
                    size.height * 0.66f,
                    size.width,
                    size.height * 0.62f
                )

                lineTo(
                    size.width,
                    size.height
                )

                close()
            }

        drawPath(
            path =
                middleMountain,
            color =
                Color(0xFFB8CAB1)
        )

        // 앞쪽 산
        val frontMountain =
            Path().apply {

                moveTo(
                    0f,
                    size.height
                )

                lineTo(
                    0f,
                    size.height * 0.91f
                )

                cubicTo(
                    size.width * 0.18f,
                    size.height * 0.75f,
                    size.width * 0.32f,
                    size.height * 0.76f,
                    size.width * 0.47f,
                    size.height * 0.87f
                )

                cubicTo(
                    size.width * 0.62f,
                    size.height * 0.97f,
                    size.width * 0.80f,
                    size.height * 0.79f,
                    size.width,
                    size.height * 0.74f
                )

                lineTo(
                    size.width,
                    size.height
                )

                close()
            }

        drawPath(
            path =
                frontMountain,
            color =
                Color(0xFF91AA8C)
        )

        // 오른쪽 식물 줄기
        drawLine(
            color =
                Color(0xFF4E704E),
            start =
                Offset(
                    size.width * 0.91f,
                    size.height
                ),
            end =
                Offset(
                    size.width * 0.89f,
                    size.height * 0.66f
                ),
            strokeWidth =
                3f
        )

        // 잎 1
        rotate(
            degrees = -35f,
            pivot =
                Offset(
                    size.width * 0.84f,
                    size.height * 0.76f
                )
        ) {

            drawOval(
                color =
                    Color(0xFF668465),
                topLeft =
                    Offset(
                        size.width * 0.78f,
                        size.height * 0.71f
                    ),
                size =
                    androidx.compose.ui.geometry.Size(
                        size.width * 0.12f,
                        size.height * 0.08f
                    )
            )
        }

        // 잎 2
        rotate(
            degrees = 35f,
            pivot =
                Offset(
                    size.width * 0.93f,
                    size.height * 0.70f
                )
        ) {

            drawOval(
                color =
                    Color(0xFF587757),
                topLeft =
                    Offset(
                        size.width * 0.88f,
                        size.height * 0.65f
                    ),
                size =
                    androidx.compose.ui.geometry.Size(
                        size.width * 0.11f,
                        size.height * 0.08f
                    )
            )
        }
    }
}

@Composable
private fun TodayProgressCard(
    habits: List<HabitResponse>,
    completedCount: Int,
    totalCount: Int,
    completionRate: Int,
    onViewHabitsClick: () -> Unit,
    onToggleHabit: (
        Long,
        Boolean
    ) -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(28.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            Color(0xFFFFDFC1)
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = "◎",
                        color =
                            Color(0xFFC66D26),
                        fontWeight =
                            FontWeight.Bold
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(10.dp)
                )

                Text(
                    text = "오늘의 진행",
                    style =
                        MaterialTheme.typography
                            .titleLarge,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(0xFF26312B)
                )

                Spacer(
                    modifier =
                        Modifier.weight(1f)
                )

                Row(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                12.dp
                            )
                        )
                        .clickable {
                            onViewHabitsClick()
                        }
                        .padding(
                            horizontal = 4.dp,
                            vertical = 2.dp
                        ),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            "$completedCount / $totalCount 완료",
                        style =
                            MaterialTheme.typography
                                .bodyMedium,
                        color =
                            Color(0xFF5C685F)
                    )

                    Spacer(
                        modifier =
                            Modifier.width(4.dp)
                    )

                    Text(
                        text = "›",
                        style =
                            MaterialTheme.typography
                                .titleLarge,
                        color =
                            Color(0xFF899089)
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(18.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
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
                        .clip(CircleShape),
                    color =
                        Color(0xFF879E87),
                    trackColor =
                        Color(0xFFECEDEA)
                )

                Spacer(
                    modifier =
                        Modifier.width(14.dp)
                )

                Text(
                    text =
                        "$completionRate%",
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    color =
                        Color(0xFF697169)
                )
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            if (habits.isEmpty()) {

                Text(
                    text =
                        "오늘 등록된 습관이 없어요.",
                    color =
                        Color(0xFF828782)
                )

            } else {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceEvenly
                ) {

                    habits
                        .take(5)
                        .forEach { habit ->

                            HomeHabitItem(
                                habit =
                                    habit,
                                onToggleHabit =
                                    onToggleHabit
                            )
                        }
                }
            }
        }
    }
}

@Composable
private fun HomeHabitItem(
    habit: HabitResponse,
    onToggleHabit: (
        Long,
        Boolean
    ) -> Unit
) {

    Column(
        modifier =
            Modifier.width(56.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                    if (
                        habit.isCompletedToday
                    ) {
                        Color(0xFF8BA38A)
                    } else {
                        Color(0xFFF0F1ED)
                    }
                )
                .clickable {

                    onToggleHabit(
                        habit.id,
                        habit.isCompletedToday
                    )
                },
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text =
                    if (
                        habit.isCompletedToday
                    ) {
                        "✓"
                    } else {
                        "○"
                    },
                style =
                    MaterialTheme.typography
                        .titleLarge,
                color =
                    if (
                        habit.isCompletedToday
                    ) {
                        Color.White
                    } else {
                        Color(0xFF9EA29D)
                    }
            )
        }

        Spacer(
            modifier =
                Modifier.height(7.dp)
        )

        Text(
            text =
                habit.title,
            style =
                MaterialTheme.typography
                    .labelMedium,
            color =
                Color(0xFF6F776F),
            maxLines = 1,
            overflow =
                TextOverflow.Ellipsis,
            textAlign =
                TextAlign.Center
        )
    }
}

@Composable
private fun ChallengeSectionHeader(
    onCreateClick: () -> Unit,
    onViewAllClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text =
                "함께하는 챌린지",
            style =
                MaterialTheme.typography
                    .titleLarge,
            fontWeight =
                FontWeight.Bold,
            color =
                Color(0xFF26312B)
        )

        Spacer(
            modifier =
                Modifier.weight(1f)
        )

        TextButton(
            onClick =
                onCreateClick
        ) {

            Text(
                text =
                    "+ 만들기",
                color =
                    Color(0xFFC57434)
            )
        }

        TextButton(
            onClick =
                onViewAllClick
        ) {

            Text(
                text =
                    "전체보기 ›",
                color =
                    Color(0xFF647168)
            )
        }
    }
}

@Composable
private fun ChallengeLazyRow(
    challenges:
    List<HomeChallengeResponse>,
    onChallengeClick:
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
                14.dp
            )
    ) {

        items(
            items =
                challenges,
            key = {
                it.id
            }
        ) { challenge ->

            HomeChallengeCard(
                challenge =
                    challenge,
                onClick = {
                    onChallengeClick(
                        challenge.id
                    )
                }
            )
        }
    }
}

@Composable
private fun HomeChallengeCard(
    challenge:
    HomeChallengeResponse,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .width(290.dp)
            .clickable {
                onClick()
            },
        shape =
            RoundedCornerShape(24.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFFFFF0E4)
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        challenge.title,
                    modifier =
                        Modifier.weight(1f),
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis
                )

                Surface(
                    shape =
                        RoundedCornerShape(
                            50.dp
                        ),
                    color =
                        Color(0xFFF5B37B)
                ) {

                    Text(
                        text =
                            formatDday(
                                challenge
                                    .remainingDays
                            ),
                        modifier =
                            Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 5.dp
                            ),
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            Text(
                text =
                    "${formatChallengeDate(challenge.startDate)} ~ " +
                            formatChallengeDate(
                                challenge.endDate
                            ),
                color =
                    Color(0xFF796F67)
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            HorizontalDivider(
                color =
                    Color(0xFFEAD8CA)
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text =
                    if (
                        challenge
                            .isCompletedToday
                    ) {
                        "✓ 나는 오늘 완료했어요"
                    } else {
                        "○ 오늘 아직 완료하지 않았어요"
                    },
                color =
                    if (
                        challenge
                            .isCompletedToday
                    ) {
                        Color(0xFF57725B)
                    } else {
                        Color(0xFF8B7463)
                    }
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "오늘 ${challenge.todayCompletedMemberCount} / " +
                            "${challenge.totalMemberCount}명 성공",
                color =
                    Color(0xFF625A54)
            )
        }
    }
}

@Composable
private fun EmptyChallengeCard(
    onCreateClick: () -> Unit
) {

    Box(
        modifier = Modifier.padding(
            horizontal = 20.dp
        )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onCreateClick()
                },
            shape =
                RoundedCornerShape(24.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color(0xFFFFF0E4)
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(20.dp)
            ) {

                Text(
                    text =
                        "아직 진행 중인 챌린지가 없어요.",
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Text(
                    text =
                        "+ 챌린지를 만들고 친구와 함께 시작해보세요."
                )
            }
        }
    }
}

@Composable
private fun WeeklyGrowthSection(
    weeklyAverageCompletionRate: Int,
    weeklyTotalCompletedCount: Int,
    bestDay: String?
) {

    Column(
        modifier = Modifier.padding(
            horizontal = 20.dp
        )
    ) {

        Text(
            text =
                "이번 주 성장",
            style =
                MaterialTheme.typography
                    .titleLarge,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(24.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                GrowthItem(
                    title =
                        "7일 평균",
                    value =
                        "$weeklyAverageCompletionRate%",
                    modifier =
                        Modifier.weight(1f),
                    highlight =
                        true
                )

                GrowthItem(
                    title =
                        "총 완료",
                    value =
                        "${weeklyTotalCompletedCount}회",
                    modifier =
                        Modifier.weight(1f)
                )

                GrowthItem(
                    title =
                        "Best Day",
                    value =
                        bestDay?.let {
                            formatChallengeDate(
                                it
                            )
                        } ?: "-",
                    modifier =
                        Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun GrowthItem(
    title: String,
    value: String,
    modifier: Modifier,
    highlight: Boolean = false
) {

    Column(
        modifier =
            modifier,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text =
                value,
            style =
                MaterialTheme.typography
                    .titleLarge,
            fontWeight =
                FontWeight.Bold,
            color =
                if (highlight) {
                    RoutineAccentOrange
                } else {
                    Color(0xFF344038)
                }
        )

        Text(
            text =
                title,
            style =
                MaterialTheme.typography
                    .bodySmall,
            color =
                Color(0xFF858B86)
        )
    }
}

@Composable
private fun HomeLoadingCard() {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(28.dp),
        color =
            Color.White
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator()

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text =
                    "오늘의 루틴을 불러오고 있어요."
            )
        }
    }
}

@Composable
private fun HomeErrorCard(
    message: String
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(26.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme
                        .errorContainer
            )
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp)
        ) {

            Text(
                text =
                    "홈 정보를 불러오지 못했어요.",
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    message
            )
        }
    }
}

private fun formatDday(
    remainingDays: Long
): String {

    return when {

        remainingDays == 0L ->
            "D-Day"

        remainingDays > 0L ->
            "D-$remainingDays"

        else ->
            "종료"
    }
}

private fun formatChallengeDate(
    dateString: String
): String {

    return try {

        val date =
            LocalDate.parse(
                dateString,
                DateTimeFormatter.ISO_LOCAL_DATE
            )

        "${date.monthValue}/${date.dayOfMonth}"

    } catch (e: Exception) {

        dateString
    }
}