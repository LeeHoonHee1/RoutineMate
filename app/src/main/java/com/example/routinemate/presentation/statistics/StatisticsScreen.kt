package com.example.routinemate.presentation.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

private data class StatisticPoint(
    val date: String,
    val completionRate: Int,
    val completedCount: Int,
    val totalCount: Int
)

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {

    val uiState by
    viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadWeeklyStatistics()
    }

    val points =
        uiState.weeklyStatistics.map {
                statistics ->

            StatisticPoint(
                date =
                    statistics.date,
                completionRate =
                    statistics.completionRate,
                completedCount =
                    statistics.completedHabitCount,
                totalCount =
                    statistics.totalHabitCount
            )
        }

    val activeDays =
        points.count {
            it.completedCount > 0
        }

    val perfectDays =
        points.count {
            it.completionRate == 100
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
                    20.dp
                )
        ) {

            // 상단 히어로
            item {

                StatisticsHeader()
            }

            when {

                uiState.isLoading -> {

                    item {

                        StatisticsLoadingCard()
                    }
                }

                uiState.errorMessage != null -> {

                    item {

                        StatisticsErrorCard(
                            message =
                                uiState.errorMessage
                                    ?: "통계를 불러오지 못했어요."
                        )
                    }
                }

                else -> {

                    // 메인 성장 카드
                    item {

                        MainStatisticsCard(
                            averageCompletionRate =
                                uiState.averageCompletionRate,
                            totalCompletedCount =
                                uiState.totalCompletedCount,
                            bestDay =
                                uiState.bestDay
                        )
                    }

                    // 작은 인사이트 카드
                    item {

                        StatisticsInsightRow(
                            activeDays =
                                activeDays,
                            perfectDays =
                                perfectDays,
                            averageRate =
                                uiState.averageCompletionRate
                        )
                    }

                    // 최근 7일 추이
                    if (
                        points.isNotEmpty()
                    ) {

                        item {

                            WeeklyTrendCard(
                                points =
                                    points
                            )
                        }

                        // 일별 달성률
                        item {

                            DailyRateSection(
                                points =
                                    points
                            )
                        }
                    }

                    // 하단 메시지
                    item {

                        WeeklyMessageCard(
                            averageRate =
                                uiState.averageCompletionRate
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatisticsHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .background(
                Color(0xFFF7F3E9)
            )
    ) {

        StatisticsLandscape(
            modifier = Modifier
                .align(
                    Alignment.BottomEnd
                )
                .width(270.dp)
                .height(160.dp)
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
                    text = "통계",
                    style =
                        MaterialTheme.typography
                            .displaySmall,
                    fontWeight =
                        FontWeight.ExtraBold,
                    color =
                        Color(0xFF073D32)
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(
                    text = "🌱",
                    style =
                        MaterialTheme.typography
                            .headlineMedium
                )
            }

            Spacer(
                modifier =
                    Modifier.height(7.dp)
            )

            Text(
                text =
                    "지금까지의 작은 변화가\n더 좋은 내일을 만들어요.",
                style =
                    MaterialTheme.typography
                        .bodyLarge,
                color =
                    Color(0xFF687871)
            )
        }
    }
}

@Composable
private fun StatisticsLandscape(
    modifier: Modifier = Modifier
) {

    Canvas(
        modifier =
            modifier
    ) {

        // 해
        drawCircle(
            color =
                Color(0xFFF4B36F),
            radius =
                size.width * 0.11f,
            center =
                Offset(
                    size.width * 0.76f,
                    size.height * 0.30f
                )
        )

        // 뒤 산
        val back =
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
                    size.width * 0.17f,
                    size.height * 0.56f,
                    size.width * 0.31f,
                    size.height * 0.48f,
                    size.width * 0.47f,
                    size.height * 0.60f
                )

                cubicTo(
                    size.width * 0.60f,
                    size.height * 0.72f,
                    size.width * 0.76f,
                    size.height * 0.43f,
                    size.width,
                    size.height * 0.56f
                )

                lineTo(
                    size.width,
                    size.height
                )

                close()
            }

        drawPath(
            path = back,
            color =
                Color(0xFFDCE5D7)
        )

        // 앞 산
        val front =
            Path().apply {

                moveTo(
                    0f,
                    size.height
                )

                lineTo(
                    0f,
                    size.height * 0.88f
                )

                cubicTo(
                    size.width * 0.18f,
                    size.height * 0.71f,
                    size.width * 0.37f,
                    size.height * 0.66f,
                    size.width * 0.55f,
                    size.height * 0.80f
                )

                cubicTo(
                    size.width * 0.72f,
                    size.height * 0.94f,
                    size.width * 0.86f,
                    size.height * 0.73f,
                    size.width,
                    size.height * 0.68f
                )

                lineTo(
                    size.width,
                    size.height
                )

                close()
            }

        drawPath(
            path = front,
            color =
                Color(0xFFB1C4AA)
        )
    }
}

@Composable
private fun MainStatisticsCard(
    averageCompletionRate: Int,
    totalCompletedCount: Int,
    bestDay: String?
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            ),
        shape =
            RoundedCornerShape(28.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // 큰 원형 달성률
            StatisticsProgressCircle(
                completionRate =
                    averageCompletionRate
            )

            Spacer(
                modifier =
                    Modifier.width(20.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "🌱",
                        style =
                            MaterialTheme.typography
                                .titleLarge
                    )

                    Spacer(
                        modifier =
                            Modifier.width(7.dp)
                    )

                    Text(
                        text = "좋아요!",
                        style =
                            MaterialTheme.typography
                                .titleLarge,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(0xFF123E34)
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(
                    text =
                        "이번 주도 꾸준히 실천하고 있어요.\n조금만 더 힘내봐요!",
                    style =
                        MaterialTheme.typography
                            .bodyMedium,
                    color =
                        Color(0xFF6D7972)
                )

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    StatisticsMetric(
                        icon = "✓",
                        title = "총 완료",
                        value =
                            "${totalCompletedCount}회"
                    )

                    StatisticsMetric(
                        icon = "★",
                        title = "가장 잘한 날",
                        value =
                            bestDay?.let {
                                formatStatisticsShortDate(
                                    it
                                )
                            } ?: "-"
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticsProgressCircle(
    completionRate: Int
) {

    Box(
        modifier =
            Modifier.size(130.dp),
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
                13.dp,
            color =
                Color(0xFF789174),
            trackColor =
                Color(0xFFE9ECE6)
        )

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text = "이번 주",
                style =
                    MaterialTheme.typography
                        .bodySmall,
                color =
                    Color(0xFF728077)
            )

            Text(
                text =
                    "$completionRate%",
                style =
                    MaterialTheme.typography
                        .headlineLarge,
                fontWeight =
                    FontWeight.ExtraBold,
                color =
                    Color(0xFF123D33)
            )

            Text(
                text = "평균 달성률",
                style =
                    MaterialTheme.typography
                        .bodySmall,
                color =
                    Color(0xFF728077)
            )
        }
    }
}

@Composable
private fun StatisticsMetric(
    icon: String,
    title: String,
    value: String
) {

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(
                    Color(0xFFEAF1E6)
                ),
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = icon,
                color =
                    Color(0xFF698568),
                fontWeight =
                    FontWeight.Bold
            )
        }

        Spacer(
            modifier =
                Modifier.height(5.dp)
        )

        Text(
            text = title,
            style =
                MaterialTheme.typography
                    .bodySmall,
            color =
                Color(0xFF7B847E)
        )

        Text(
            text = value,
            style =
                MaterialTheme.typography
                    .titleMedium,
            fontWeight =
                FontWeight.Bold,
            color =
                Color(0xFF173D34)
        )
    }
}

@Composable
private fun StatisticsInsightRow(
    activeDays: Int,
    perfectDays: Int,
    averageRate: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            ),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        InsightCard(
            icon = "📊",
            title = "꾸준히 실천 중",
            value = "${activeDays}일",
            description = "최근 7일 기준",
            modifier = Modifier.weight(1f)
        )

        InsightCard(
            icon = "☀️",
            title = "완벽했던 날",
            value = "${perfectDays}일",
            description = "100% 달성",
            modifier = Modifier.weight(1f)
        )

        InsightCard(
            icon = "🌱",
            title = "평균 달성률",
            value = "$averageRate%",
            description = "이번 주 기록",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun InsightCard(
    icon: String,
    title: String,
    value: String,
    description: String,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 12.dp
            ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Color(0xFFF0F4EA)
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = icon,
                    style =
                        MaterialTheme.typography
                            .titleMedium
                )
            }

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text = title,
                style =
                    MaterialTheme.typography
                        .labelSmall,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(0xFF385448),
                maxLines = 1
            )

            Text(
                text = value,
                style =
                    MaterialTheme.typography
                        .titleMedium,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(0xFF173E34)
            )

            Text(
                text = description,
                style =
                    MaterialTheme.typography
                        .labelSmall,
                color =
                    Color(0xFF89908B),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun WeeklyTrendCard(
    points: List<StatisticPoint>
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            ),
        shape =
            RoundedCornerShape(24.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Column(
            modifier =
                Modifier.padding(18.dp)
        ) {

            Text(
                text =
                    "최근 7일 추이",
                style =
                    MaterialTheme.typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(0xFF123D33)
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "하루하루, 꾸준히 쌓아가는 중이에요.",
                style =
                    MaterialTheme.typography
                        .bodyMedium,
                color =
                    Color(0xFF7A847E)
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            WeeklyLineChart(
                points =
                    points
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                points.forEach {
                        point ->

                    Column(
                        modifier =
                            Modifier.weight(1f),
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text =
                                "${point.completionRate}%",
                            style =
                                MaterialTheme.typography
                                    .labelSmall,
                            color =
                                Color(0xFF375B49)
                        )

                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )

                        Text(
                            text =
                                formatStatisticsShortDate(
                                    point.date
                                ),
                            style =
                                MaterialTheme.typography
                                    .labelSmall,
                            color =
                                Color(0xFF777F7A)
                        )

                        Text(
                            text =
                                "(${formatStatisticsDay(point.date)})",
                            style =
                                MaterialTheme.typography
                                    .labelSmall,
                            color =
                                Color(0xFF969C98)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeeklyLineChart(
    points: List<StatisticPoint>
) {

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {

        if (
            points.isEmpty()
        ) {
            return@Canvas
        }

        val leftPadding =
            8f

        val rightPadding =
            8f

        val topPadding =
            14f

        val bottomPadding =
            12f

        val usableWidth =
            size.width -
                    leftPadding -
                    rightPadding

        val usableHeight =
            size.height -
                    topPadding -
                    bottomPadding

        // 기준선
        drawLine(
            color =
                Color(0xFFE5E8E3),
            start =
                Offset(
                    leftPadding,
                    topPadding
                ),
            end =
                Offset(
                    size.width -
                            rightPadding,
                    topPadding
                ),
            strokeWidth = 1.5f
        )

        drawLine(
            color =
                Color(0xFFE5E8E3),
            start =
                Offset(
                    leftPadding,
                    topPadding +
                            usableHeight /
                            2
                ),
            end =
                Offset(
                    size.width -
                            rightPadding,
                    topPadding +
                            usableHeight /
                            2
                ),
            strokeWidth = 1.5f
        )

        drawLine(
            color =
                Color(0xFFE5E8E3),
            start =
                Offset(
                    leftPadding,
                    topPadding +
                            usableHeight
                ),
            end =
                Offset(
                    size.width -
                            rightPadding,
                    topPadding +
                            usableHeight
                ),
            strokeWidth = 1.5f
        )

        val graphPoints =
            points.mapIndexed {
                    index,
                    point ->

                val x =
                    if (
                        points.size == 1
                    ) {
                        size.width / 2
                    } else {
                        leftPadding +
                                usableWidth *
                                index /
                                (
                                        points.size -
                                                1
                                        )
                    }

                val rate =
                    point.completionRate
                        .coerceIn(
                            0,
                            100
                        )

                val y =
                    topPadding +
                            usableHeight *
                            (
                                    1f -
                                            rate /
                                            100f
                                    )

                Offset(
                    x,
                    y
                )
            }

        // 선
        val path =
            Path()

        graphPoints
            .forEachIndexed {
                    index,
                    point ->

                if (index == 0) {

                    path.moveTo(
                        point.x,
                        point.y
                    )

                } else {

                    val previous =
                        graphPoints[
                            index - 1
                        ]

                    val middleX =
                        (
                                previous.x +
                                        point.x
                                ) / 2

                    path.cubicTo(
                        middleX,
                        previous.y,
                        middleX,
                        point.y,
                        point.x,
                        point.y
                    )
                }
            }

        drawPath(
            path = path,
            color =
                Color(0xFF6E8D70),
            style =
                Stroke(
                    width = 5f,
                    cap =
                        StrokeCap.Round
                )
        )

        // 점
        graphPoints.forEach {
                point ->

            drawCircle(
                color =
                    Color.White,
                radius = 9f,
                center =
                    point
            )

            drawCircle(
                color =
                    Color(0xFF638765),
                radius = 6f,
                center =
                    point
            )
        }
    }
}

@Composable
private fun DailyRateSection(
    points: List<StatisticPoint>
) {

    Column {

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
                text = "일별 달성률",
                style =
                    MaterialTheme.typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(0xFF123D33)
            )

            Spacer(
                modifier =
                    Modifier.weight(1f)
            )

            Text(
                text = "최근 7일",
                style =
                    MaterialTheme.typography
                        .bodyMedium,
                color =
                    Color(0xFF7E8580)
            )
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp
                ),
            shape =
                RoundedCornerShape(22.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp,
                        vertical = 14.dp
                    ),
                horizontalArrangement =
                    Arrangement.spacedBy(4.dp)
            ) {

                points.forEach { point ->

                    DailyRateCard(
                        point = point,
                        modifier =
                            Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyRateCard(
    point: StatisticPoint,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        // 날짜
        Text(
            text =
                formatStatisticsShortDate(
                    point.date
                ),
            style =
                MaterialTheme.typography
                    .labelSmall,
            color =
                Color(0xFF6E7771),
            maxLines = 1
        )

        // 요일
        Text(
            text =
                formatStatisticsDay(
                    point.date
                ),
            style =
                MaterialTheme.typography
                    .labelSmall,
            color =
                Color(0xFF9A9F9B),
            maxLines = 1
        )

        Spacer(
            modifier =
                Modifier.height(7.dp)
        )

        Box(
            modifier =
                Modifier.size(38.dp),
            contentAlignment =
                Alignment.Center
        ) {

            CircularProgressIndicator(
                progress = {
                    (
                            point.completionRate /
                                    100f
                            ).coerceIn(
                            0f,
                            1f
                        )
                },
                modifier =
                    Modifier.fillMaxSize(),
                strokeWidth = 5.dp,
                color =
                    if (
                        point.completionRate ==
                        100
                    ) {
                        Color(0xFF557B58)
                    } else {
                        Color(0xFF82997F)
                    },
                trackColor =
                    Color(0xFFE8EBE6)
            )

            Text(
                text =
                    "${point.completionRate}%",
                style =
                    MaterialTheme.typography
                        .labelSmall,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(0xFF2D4D3F)
            )
        }

        Spacer(
            modifier =
                Modifier.height(5.dp)
        )

        Text(
            text =
                "${point.completedCount}/${point.totalCount}",
            style =
                MaterialTheme.typography
                    .labelSmall,
            color =
                Color(0xFF777F7A),
            maxLines = 1
        )
    }
}

@Composable
private fun WeeklyMessageCard(
    averageRate: Int
) {

    val message =
        when {

            averageRate >= 80 ->
                "이번 주 정말 잘하고 있어요!"

            averageRate >= 50 ->
                "이번 주도 꾸준히 잘 이어가고 있어요!"

            averageRate > 0 ->
                "작은 실천부터 천천히 이어가봐요!"

            else ->
                "오늘부터 첫 기록을 만들어볼까요?"
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            ),
        shape =
            RoundedCornerShape(24.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFFF0F4EA)
            )
    ) {

        Row(
            modifier =
                Modifier.padding(18.dp),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Color(0xFFDDE9D8)
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "🌱",
                    style =
                        MaterialTheme.typography
                            .headlineMedium
                )
            }

            Spacer(
                modifier =
                    Modifier.width(14.dp)
            )

            Column {

                Text(
                    text =
                        "$message 🌱",
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(0xFF244A3B)
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        "작은 실천이 모여 더 좋은 변화를 만들 거예요.",
                    style =
                        MaterialTheme.typography
                            .bodySmall,
                    color =
                        Color(0xFF708078)
                )
            }
        }
    }
}

@Composable
private fun StatisticsLoadingCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            ),
        shape =
            RoundedCornerShape(24.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            CircularProgressIndicator(
                color =
                    Color(0xFF6E8D71)
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text =
                    "통계를 불러오고 있어요",
                color =
                    Color(0xFF7A847E)
            )
        }
    }
}

@Composable
private fun StatisticsErrorCard(
    message: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            ),
        shape =
            RoundedCornerShape(24.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme
                        .colorScheme
                        .errorContainer
            )
    ) {

        Column(
            modifier =
                Modifier.padding(20.dp)
        ) {

            Text(
                text =
                    "통계를 불러오지 못했어요.",
                style =
                    MaterialTheme.typography
                        .titleMedium,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )

            Text(
                text = message,
                style =
                    MaterialTheme.typography
                        .bodyMedium
            )
        }
    }
}

// 2026-09-25 → 9/25
private fun formatStatisticsShortDate(
    dateString: String
): String {

    return try {

        val date =
            LocalDate.parse(
                dateString
            )

        "${date.monthValue}/${date.dayOfMonth}"

    } catch (
        e: Exception
    ) {

        dateString
    }
}

// 요일 반환
private fun formatStatisticsDay(
    dateString: String
): String {

    return try {

        val date =
            LocalDate.parse(
                dateString
            )

        date.dayOfWeek
            .getDisplayName(
                TextStyle.SHORT,
                Locale.KOREAN
            )

    } catch (
        e: Exception
    ) {

        ""
    }
}