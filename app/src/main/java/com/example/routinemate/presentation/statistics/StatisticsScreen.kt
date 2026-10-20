package com.example.routinemate.presentation.statistics

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
import androidx.compose.foundation.clickable
import androidx.compose.material3.LinearProgressIndicator
import com.example.routinemate.data.remote.dto.habit.HabitCategory
import com.example.routinemate.data.remote.dto.statistics.CategoryHabitStatisticsResponse

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

        viewModel.loadCategoryStatistics()

        // 연속 달성일 조회
        viewModel.loadStreakStatistics()
    }

    val points =
        uiState.statistics.map {
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

    val periodText =
        when (
            uiState.selectedPeriod
        ) {

            StatisticsPeriod.WEEKLY ->
                "최근 7일"

            StatisticsPeriod.THIRTY_DAYS ->
                "최근 30일"

            StatisticsPeriod.MONTHLY ->
                "이번 달"
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

            // 7일 / 30일 선택
            item {

                StatisticsPeriodSelector(
                    selectedPeriod =
                        uiState.selectedPeriod,
                    onPeriodSelected = { period ->

                        viewModel.onPeriodChange(
                            period
                        )
                    }
                )
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
                                uiState.bestDay,
                            periodText =
                                periodText
                        )
                    }

                    // 연속 달성 카드
                    item {

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 20.dp
                                ),
                            shape =
                                RoundedCornerShape(
                                    24.dp
                                ),
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
                                        18.dp
                                    ),
                                horizontalArrangement =
                                    Arrangement.SpaceBetween,
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Column {

                                    Text(
                                        text =
                                            "🔥 현재 연속 달성",
                                        style =
                                            MaterialTheme.typography
                                                .bodyMedium,
                                        color =
                                            Color(
                                                0xFF6D7972
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
                                            "${uiState.currentStreak}일",
                                        style =
                                            MaterialTheme.typography
                                                .headlineMedium,
                                        fontWeight =
                                            FontWeight.Bold,
                                        color =
                                            Color(
                                                0xFF123D33
                                            )
                                    )
                                }

                                Column(
                                    horizontalAlignment =
                                        Alignment.End
                                ) {

                                    Text(
                                        text =
                                            "🏆 최고 기록",
                                        style =
                                            MaterialTheme.typography
                                                .bodyMedium,
                                        color =
                                            Color(
                                                0xFF6D7972
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
                                            "${uiState.longestStreak}일",
                                        style =
                                            MaterialTheme.typography
                                                .headlineMedium,
                                        fontWeight =
                                            FontWeight.Bold,
                                        color =
                                            Color(
                                                0xFF123D33
                                            )
                                    )
                                }
                            }
                        }
                    }

                    // 작은 인사이트 카드
                    item {

                        StatisticsInsightRow(
                            activeDays =
                                activeDays,
                            perfectDays =
                                perfectDays,
                            averageRate =
                                uiState.averageCompletionRate,
                            periodText =
                                periodText
                        )
                    }

                    // 최근 7일 추이
                    if (
                        points.isNotEmpty()
                    ) {

                        item {

                            WeeklyTrendCard(
                                points =
                                    points,
                                periodText =
                                    periodText
                            )
                        }

                        // 일별 달성률
                        item {

                            DailyRateSection(
                                points =
                                    points,
                                periodText =
                                    periodText
                            )
                        }

                        // 카테고리별 달성률
                        if (
                            uiState.categoryStatistics
                                .isNotEmpty()
                        ) {

                            item {

                                CategoryStatisticsSection(
                                    statistics =
                                        uiState.categoryStatistics
                                )
                            }
                        }
                    }

                    // 하단 메시지
                    item {

                        StatisticsMessageCard(
                            averageRate =
                                uiState.averageCompletionRate,
                            periodText =
                                periodText
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
    bestDay: String?,
    periodText: String
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
                    averageCompletionRate,
                periodText =
                    periodText
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
                        "$periodText 동안 꾸준히 실천하고 있어요.\n조금씩 기록을 쌓아가고 있어요!",
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
    completionRate: Int,
    periodText: String
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
                text = periodText,
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
    averageRate: Int,
    periodText: String
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
            description = "$periodText 기준",
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
            description = "$periodText 기록",
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
    points: List<StatisticPoint>,
    periodText: String
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
                    "$periodText 추이",
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

                val labelPoints =
                    if (points.size <= 7) {

                        points

                    } else {

                        listOf(
                            points.first(),
                            points[points.lastIndex / 4],
                            points[points.lastIndex / 2],
                            points[(points.lastIndex * 3) / 4],
                            points.last()
                        )
                    }

                Row(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    labelPoints.forEach {
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
                                    Color(
                                        0xFF375B49
                                    )
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        3.dp
                                    )
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
                                    Color(
                                        0xFF777F7A
                                    )
                            )
                        }
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
    points: List<StatisticPoint>,
    periodText: String
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
                text = periodText,
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

            LazyRow(
                modifier =
                    Modifier.fillMaxWidth(),
                contentPadding =
                    PaddingValues(
                        horizontal = 12.dp
                    ),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                items(
                    count =
                        points.size
                ) { index ->

                    DailyRateCard(
                        point =
                            points[index],
                        modifier =
                            Modifier.width(
                                64.dp
                            )
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
private fun StatisticsMessageCard(
    averageRate: Int,
    periodText: String
) {

    val message =
        when {

            averageRate >= 80 ->
                "$periodText 정말 잘하고 있어요!"

            averageRate >= 50 ->
                "$periodText 꾸준히 잘 이어가고 있어요!"

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

@Composable
private fun StatisticsPeriodSelector(
    selectedPeriod: StatisticsPeriod,
    onPeriodSelected: (StatisticsPeriod) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            ),
        horizontalArrangement =
            Arrangement.spacedBy(
                8.dp
            )
    ) {

        StatisticsPeriodButton(
            text = "7일",
            selected =
                selectedPeriod ==
                        StatisticsPeriod.WEEKLY,
            modifier =
                Modifier.weight(1f),
            onClick = {
                onPeriodSelected(
                    StatisticsPeriod.WEEKLY
                )
            }
        )

        StatisticsPeriodButton(
            text = "30일",
            selected =
                selectedPeriod ==
                        StatisticsPeriod.THIRTY_DAYS,
            modifier =
                Modifier.weight(1f),
            onClick = {
                onPeriodSelected(
                    StatisticsPeriod.THIRTY_DAYS
                )
            }
        )

        StatisticsPeriodButton(
            text = "이번 달",
            selected =
                selectedPeriod ==
                        StatisticsPeriod.MONTHLY,
            modifier =
                Modifier.weight(1f),
            onClick = {
                onPeriodSelected(
                    StatisticsPeriod.MONTHLY
                )
            }
        )
    }
}

@Composable
private fun StatisticsPeriodButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    Surface(
        modifier = modifier
            .height(46.dp)
            .clickable {
                onClick()
            },
        shape =
            RoundedCornerShape(
                16.dp
            ),
        color =
            if (selected) {

                Color(
                    0xFFE1EBDC
                )

            } else {

                Color.White
            }
    ) {

        Box(
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text = text,
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
                        Color(0xFF737C76)
                    }
            )
        }
    }
}

@Composable
private fun CategoryStatisticsSection(
    statistics:
    List<CategoryHabitStatisticsResponse>
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
                text =
                    "카테고리별 달성률",
                style =
                    MaterialTheme.typography
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
                    "최근 30일",
                style =
                    MaterialTheme.typography
                        .bodyMedium,
                color =
                    Color(
                        0xFF7E8580
                    )
            )
        }

        Spacer(
            modifier =
                Modifier.height(
                    12.dp
                )
        )

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
                count =
                    statistics.size
            ) { index ->

                CategoryStatisticsCard(
                    statistics =
                        statistics[index]
                )
            }
        }
    }
}

@Composable
private fun CategoryStatisticsCard(
    statistics:
    CategoryHabitStatisticsResponse
) {

    Card(
        modifier =
            Modifier.width(
                170.dp
            ),
        shape =
            RoundedCornerShape(
                22.dp
            ),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(
                            42.dp
                        )
                        .clip(
                            CircleShape
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
                            categoryStatisticsIcon(
                                statistics.category
                            ),
                        style =
                            MaterialTheme.typography
                                .titleLarge
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(
                            10.dp
                        )
                )

                Text(
                    text =
                        categoryStatisticsLabel(
                            statistics.category
                        ),
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF25362F
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )

            Text(
                text =
                    "${statistics.completionRate}%",
                style =
                    MaterialTheme.typography
                        .headlineMedium,
                fontWeight =
                    FontWeight.ExtraBold,
                color =
                    Color(
                        0xFF476446
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            LinearProgressIndicator(
                progress = {
                    (
                            statistics.completionRate /
                                    100f
                            ).coerceIn(
                            0f,
                            1f
                        )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        8.dp
                    )
                    .clip(
                        CircleShape
                    ),
                color =
                    Color(
                        0xFF789174
                    ),
                trackColor =
                    Color(
                        0xFFE9ECE6
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            Text(
                text =
                    "${statistics.completedCount} / " +
                            "${statistics.totalScheduledCount}회 완료",
                style =
                    MaterialTheme.typography
                        .bodySmall,
                color =
                    Color(
                        0xFF7A847E
                    )
            )
        }
    }
}

private fun categoryStatisticsLabel(
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
            "수면"

        HabitCategory.EXERCISE ->
            "운동"

        HabitCategory.STUDY ->
            "공부"
    }
}

private fun categoryStatisticsIcon(
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