package com.example.routinemate.presentation.friend

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

@Composable
fun FriendScreen(
    viewModel: FriendViewModel = hiltViewModel()
) {

    val uiState by
    viewModel.uiState.collectAsState()

    val snackbarHostState =
        remember {
            SnackbarHostState()
        }

    // 성공 메시지
    LaunchedEffect(
        uiState.successMessage
    ) {

        uiState.successMessage
            ?.let { message ->

                snackbarHostState
                    .showSnackbar(
                        message = message
                    )

                viewModel.clearMessage()
            }
    }

    // 실패 메시지
    LaunchedEffect(
        uiState.errorMessage
    ) {

        uiState.errorMessage
            ?.let { message ->

                snackbarHostState
                    .showSnackbar(
                        message = message
                    )

                viewModel.clearMessage()
            }
    }

    Scaffold(
        containerColor =
            Color(0xFFFAF9F5),

        snackbarHost = {

            SnackbarHost(
                hostState =
                    snackbarHostState
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    innerPadding
                ),
            contentPadding =
                PaddingValues(
                    bottom = 32.dp
                ),
            verticalArrangement =
                Arrangement.spacedBy(
                    22.dp
                )
        ) {

            // 상단
            item {

                FriendHeader()
            }

            // 상단 요약
            item {

                FriendSummaryCard(
                    friendCount =
                        uiState.friends.size,
                    requestCount =
                        uiState.receivedRequests.size,

                    // TODO 나중에 실제 챌린지 개수와 연결
                    challengeCount = 3
                )
            }

            // 로딩
            if (uiState.isLoading) {

                item {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            color =
                                Color(
                                    0xFF66886C
                                )
                        )
                    }
                }
            }

            // 받은 친구 요청 제목
            item {

                FriendSectionTitle(
                    title =
                        "받은 친구 요청",
                    count =
                        uiState.receivedRequests.size
                )
            }

            // 받은 요청
            if (
                uiState.receivedRequests
                    .isEmpty()
            ) {

                item {

                    FriendEmptyCard(
                        icon = "✉️",
                        title =
                            "받은 친구 요청이 없어요.",
                        description =
                            "새로운 친구의 요청을 기다려보세요."
                    )
                }

            } else {

                items(
                    items =
                        uiState.receivedRequests,
                    key = { request ->
                        request.id
                    }
                ) { request ->

                    FriendRequestCard(
                        nickname =
                            request.requesterNickname,

                        onAccept = {

                            viewModel
                                .acceptFriendRequest(
                                    requestId =
                                        request.id
                                )
                        },

                        onReject = {

                            viewModel
                                .rejectFriendRequest(
                                    requestId =
                                        request.id
                                )
                        }
                    )
                }
            }

            // 내 친구 제목
            item {

                FriendSectionTitle(
                    title =
                        "내 친구",
                    count =
                        uiState.friends.size
                )
            }

            // 친구 목록
            if (
                uiState.friends
                    .isEmpty()
            ) {

                item {

                    FriendEmptyCard(
                        icon = "🌱",
                        title =
                            "아직 친구가 없어요.",
                        description =
                            "아래에서 함께 루틴을 이어갈 친구를 찾아보세요."
                    )
                }

            } else {

                items(
                    items =
                        uiState.friends,
                    key = { friend ->
                        friend.id
                    }
                ) { friend ->

                    FriendListCard(
                        nickname =
                            friend.nickname,
                        email =
                            friend.email
                    )
                }
            }

            // 친구 찾기
            item {

                FriendSearchSection(
                    keyword =
                        uiState.searchKeyword,

                    onKeywordChange =
                        viewModel::onSearchKeywordChange,

                    onSearch = {

                        viewModel.searchUsers()
                    }
                )
            }

            // 검색 결과 제목
            if (
                uiState.searchResults
                    .isNotEmpty()
            ) {

                item {

                    FriendSectionTitle(
                        title =
                            "검색 결과",
                        count =
                            uiState.searchResults.size
                    )
                }

                items(
                    items =
                        uiState.searchResults,
                    key = { user ->
                        user.id
                    }
                ) { user ->

                    SearchResultCard(
                        nickname =
                            user.nickname,
                        email =
                            user.email,

                        onRequest = {

                            viewModel
                                .sendFriendRequest(
                                    receiverId =
                                        user.id
                                )
                        }
                    )
                }
            }

            // 하단 메시지
            item {

                FriendFooterCard()
            }
        }
    }
}

@Composable
private fun FriendHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Color(0xFFF7F3E9)
            )
    ) {

        FriendLandscape(
            modifier = Modifier
                .align(
                    Alignment.BottomEnd
                )
                .width(270.dp)
                .height(150.dp)
        )

        Column(
            modifier = Modifier
                .align(
                    Alignment.CenterStart
                )
                .padding(
                    horizontal = 20.dp
                )
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(
                            RoundedCornerShape(
                                14.dp
                            )
                        )
                        .background(
                            Color(
                                0xFF789477
                            )
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = "RM",
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color.White
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
                        "RoutineMate",
                    style =
                        MaterialTheme.typography
                            .titleLarge,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF45654F
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        22.dp
                    )
            )

            Text(
                text = "친구",
                style =
                    MaterialTheme.typography
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
                    Modifier.height(
                        5.dp
                    )
            )

            Text(
                text =
                    "함께 루틴을 이어갈 친구를 관리해보세요.",
                style =
                    MaterialTheme.typography
                        .bodyLarge,
                color =
                    Color(
                        0xFF687871
                    )
            )
        }
    }
}

@Composable
private fun FriendLandscape(
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
                    0xFFF5B571
                ),
            radius =
                size.width * 0.10f,
            center =
                Offset(
                    size.width * 0.77f,
                    size.height * 0.27f
                )
        )

        // 뒤 언덕
        val back =
            Path().apply {

                moveTo(
                    0f,
                    size.height
                )

                lineTo(
                    0f,
                    size.height * 0.77f
                )

                cubicTo(
                    size.width * 0.20f,
                    size.height * 0.58f,
                    size.width * 0.39f,
                    size.height * 0.55f,
                    size.width * 0.56f,
                    size.height * 0.68f
                )

                cubicTo(
                    size.width * 0.72f,
                    size.height * 0.80f,
                    size.width * 0.86f,
                    size.height * 0.55f,
                    size.width,
                    size.height * 0.60f
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
                    0xFFDCE5D7
                )
        )

        // 앞 언덕
        val front =
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
                    size.height * 0.74f,
                    size.width * 0.37f,
                    size.height * 0.72f,
                    size.width * 0.54f,
                    size.height * 0.84f
                )

                cubicTo(
                    size.width * 0.70f,
                    size.height * 0.95f,
                    size.width * 0.86f,
                    size.height * 0.78f,
                    size.width,
                    size.height * 0.72f
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
                    0xFFB4C7AD
                )
        )

        // 식물 줄기
        drawLine(
            color =
                Color(
                    0xFF648063
                ),
            start =
                Offset(
                    size.width * 0.70f,
                    size.height
                ),
            end =
                Offset(
                    size.width * 0.72f,
                    size.height * 0.55f
                ),
            strokeWidth =
                4f
        )

        drawCircle(
            color =
                Color(
                    0xFF789777
                ),
            radius =
                11f,
            center =
                Offset(
                    size.width * 0.67f,
                    size.height * 0.67f
                )
        )

        drawCircle(
            color =
                Color(
                    0xFF638464
                ),
            radius =
                12f,
            center =
                Offset(
                    size.width * 0.77f,
                    size.height * 0.60f
                )
        )
    }
}

@Composable
private fun FriendSummaryCard(
    friendCount: Int,
    requestCount: Int,
    challengeCount: Int
) {

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
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 14.dp,
                    horizontal = 10.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            FriendSummaryItem(
                icon = "👥",
                title = "친구",
                value = "${friendCount}명",
                modifier =
                    Modifier.weight(1f),
                backgroundColor =
                    Color(0xFFEAF2E6)
            )

            SummaryDivider()

            FriendSummaryItem(
                icon = "✉️",
                title = "받은 요청",
                value = "${requestCount}건",
                modifier =
                    Modifier.weight(1f),
                backgroundColor =
                    Color(0xFFFFEEE2)
            )

            SummaryDivider()

            FriendSummaryItem(
                icon = "🏆",
                title = "함께 챌린지",
                value = "${challengeCount}개",
                modifier =
                    Modifier.weight(1f),
                backgroundColor =
                    Color(0xFFEAF2E6)
            )
        }
    }
}

@Composable
private fun FriendSummaryItem(
    icon: String,
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color
) {

    Row(
        modifier = modifier,
        verticalAlignment =
            Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(
                    CircleShape
                )
                .background(
                    backgroundColor
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
                Modifier.width(8.dp)
        )

        Column {

            Text(
                text = title,
                style =
                    MaterialTheme.typography
                        .labelSmall,
                color =
                    Color(0xFF747E78),
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
                    Color(0xFF183F35)
            )
        }
    }
}

@Composable
private fun SummaryDivider() {

    Box(
        modifier = Modifier
            .width(1.dp)
            .height(74.dp)
            .background(
                Color(
                    0xFFEDEEEB
                )
            )
    )
}

@Composable
private fun FriendSectionTitle(
    title: String,
    count: Int
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
                title,
            style =
                MaterialTheme.typography
                    .titleLarge,
            fontWeight =
                FontWeight.Bold,
            color =
                Color(
                    0xFF153D33
                )
        )

        Spacer(
            modifier =
                Modifier.width(
                    8.dp
                )
        )

        if (count > 0) {

            Surface(
                shape =
                    CircleShape,
                color =
                    Color(
                        0xFFF29A58
                    )
            ) {

                Text(
                    text =
                        count.toString(),
                    modifier =
                        Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 3.dp
                        ),
                    style =
                        MaterialTheme.typography
                            .labelMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color.White
                )
            }
        }

        Spacer(
            modifier =
                Modifier.weight(
                    1f
                )
        )

        Text(
            text =
                "전체 보기 ›",
            style =
                MaterialTheme.typography
                    .bodyMedium,
            color =
                Color(
                    0xFF6F7C74
                )
        )
    }
}

@Composable
private fun FriendRequestCard(
    nickname: String,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {

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
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    1.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    16.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            FriendAvatar(
                nickname =
                    nickname
            )

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
                        nickname,
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF27372F
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
                        "함께 좋은 습관을 만들어봐요! 🌿",
                    style =
                        MaterialTheme.typography
                            .bodySmall,
                    color =
                        Color(
                            0xFF7B847E
                        )
                )
            }

            Column(
                verticalArrangement =
                    Arrangement.spacedBy(
                        7.dp
                    )
            ) {

                Button(
                    onClick =
                        onAccept,
                    modifier =
                        Modifier.width(
                            82.dp
                        ),
                    shape =
                        RoundedCornerShape(
                            50.dp
                        ),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(
                                    0xFF668A6B
                                )
                        )
                ) {

                    Text(
                        "수락"
                    )
                }

                OutlinedButton(
                    onClick =
                        onReject,
                    modifier =
                        Modifier.width(
                            82.dp
                        ),
                    shape =
                        RoundedCornerShape(
                            50.dp
                        )
                ) {

                    Text(
                        text =
                            "거절",
                        color =
                            Color(
                                0xFF59665F
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun FriendListCard(
    nickname: String,
    email: String
) {

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
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    1.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    15.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            FriendAvatar(
                nickname =
                    nickname
            )

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
                        nickname,
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF26372F
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
                        email,
                    style =
                        MaterialTheme.typography
                            .bodyMedium,
                    color =
                        Color(
                            0xFF7B8580
                        ),
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            5.dp
                        )
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(
                                7.dp
                            )
                            .clip(
                                CircleShape
                            )
                            .background(
                                Color(
                                    0xFF7BA67B
                                )
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.width(
                                5.dp
                            )
                    )

                    Text(
                        text =
                            "함께 루틴을 이어가는 중",
                        style =
                            MaterialTheme.typography
                                .bodySmall,
                        color =
                            Color(
                                0xFF6C7B72
                            )
                    )
                }
            }

            Surface(
                shape =
                    RoundedCornerShape(
                        50.dp
                    ),
                color =
                    Color(
                        0xFFEAF2E6
                    )
            ) {

                Text(
                    text =
                        "🌱 친구",
                    modifier =
                        Modifier.padding(
                            horizontal =
                                12.dp,
                            vertical =
                                7.dp
                        ),
                    style =
                        MaterialTheme.typography
                            .labelMedium,
                    color =
                        Color(
                            0xFF517252
                        )
                )
            }
        }
    }
}

@Composable
private fun FriendAvatar(
    nickname: String
) {

    Box(
        modifier = Modifier
            .size(
                56.dp
            )
            .clip(
                CircleShape
            )
            .background(
                Color(
                    0xFFE4EEDC
                )
            ),
        contentAlignment =
            Alignment.Center
    ) {

        Text(
            text =
                nickname
                    .firstOrNull()
                    ?.toString()
                    ?: "?",
            style =
                MaterialTheme.typography
                    .headlineMedium,
            fontWeight =
                FontWeight.Bold,
            color =
                Color(
                    0xFF587458
                )
        )
    }
}

@Composable
private fun FriendSearchSection(
    keyword: String,
    onKeywordChange:
        (String) -> Unit,
    onSearch: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal =
                    20.dp
            )
    ) {

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text =
                    "친구 찾기",
                style =
                    MaterialTheme.typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(
                        0xFF153D33
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
                    "좋은 친구와 더 멀리 가요 🌱",
                style =
                    MaterialTheme.typography
                        .bodySmall,
                color =
                    Color(
                        0xFF749078
                    )
            )
        }

        Spacer(
            modifier =
                Modifier.height(
                    12.dp
                )
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically,
            horizontalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {

            OutlinedTextField(
                value =
                    keyword,
                onValueChange =
                    onKeywordChange,
                modifier =
                    Modifier.weight(
                        1f
                    ),
                singleLine =
                    true,
                shape =
                    RoundedCornerShape(
                        18.dp
                    ),
                leadingIcon = {

                    Text(
                        text =
                            "⌕",
                        style =
                            MaterialTheme.typography
                                .titleLarge,
                        color =
                            Color(
                                0xFF768078
                            )
                    )
                },
                placeholder = {

                    Text(
                        text =
                            "닉네임 또는 이메일",
                        color =
                            Color(
                                0xFF929792
                            )
                    )
                }
            )

            Button(
                onClick =
                    onSearch,
                modifier =
                    Modifier.height(
                        56.dp
                    ),
                shape =
                    RoundedCornerShape(
                        18.dp
                    ),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(
                                0xFF628267
                            )
                    )
            ) {

                Text(
                    text =
                        "검색",
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SearchResultCard(
    nickname: String,
    email: String,
    onRequest: () -> Unit
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
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    15.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            FriendAvatar(
                nickname =
                    nickname
            )

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
                        nickname,
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            3.dp
                        )
                )

                Text(
                    text =
                        email,
                    style =
                        MaterialTheme.typography
                            .bodyMedium,
                    color =
                        Color(
                            0xFF7B847E
                        ),
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis
                )
            }

            Button(
                onClick =
                    onRequest,
                shape =
                    RoundedCornerShape(
                        50.dp
                    ),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(
                                0xFF688B6D
                            )
                    )
            ) {

                Text(
                    text =
                        "친구 요청"
                )
            }
        }
    }
}

@Composable
private fun FriendEmptyCard(
    icon: String,
    title: String,
    description: String
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
            CardDefaults.cardColors(
                containerColor =
                    Color(
                        0xFFF0F4EA
                    )
            )
    ) {

        Row(
            modifier =
                Modifier.padding(
                    18.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text =
                    icon,
                style =
                    MaterialTheme.typography
                        .headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.width(
                        13.dp
                    )
            )

            Column {

                Text(
                    text =
                        title,
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF405349
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
                        description,
                    style =
                        MaterialTheme.typography
                            .bodySmall,
                    color =
                        Color(
                            0xFF7B857F
                        )
                )
            }
        }
    }
}

@Composable
private fun FriendFooterCard() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal =
                    20.dp
            ),
        shape =
            RoundedCornerShape(
                24.dp
            ),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(
                        0xFFF0F4EA
                    )
            )
    ) {

        Row(
            modifier =
                Modifier.padding(
                    18.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "🌿",
                style =
                    MaterialTheme.typography
                        .headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.width(
                        12.dp
                    )
            )

            Column {

                Text(
                    text =
                        "함께하는 루틴, 더 특별한 하루",
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF365447
                        )
                )

                Text(
                    text =
                        "친구와 서로 응원하며 꾸준히 이어가보세요.",
                    style =
                        MaterialTheme.typography
                            .bodySmall,
                    color =
                        Color(
                            0xFF748078
                        )
                )
            }
        }
    }
}