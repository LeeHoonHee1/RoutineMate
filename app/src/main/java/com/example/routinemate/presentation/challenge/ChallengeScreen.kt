package com.example.routinemate.presentation.challenge

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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun ChallengeScreen(
    onChallengeClick: (Long) -> Unit,
    viewModel: ChallengeViewModel = hiltViewModel()
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

            // 상단 히어로
            item {

                ChallengeHeader()
            }

            // 함께하는 성장
            item {

                Box(
                    modifier =
                        Modifier.padding(
                            horizontal =
                                20.dp
                        )
                ) {

                    ChallengeGrowthCard(
                        challengeCount =
                            uiState
                                .challenges
                                .size,
                        invitationCount =
                            uiState
                                .invitations
                                .size
                    )
                }
            }

            // 로딩
            if (uiState.isLoading) {

                item {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        CircularProgressIndicator(
                            color =
                                Color(
                                    0xFF6E8D71
                                )
                        )
                    }
                }
            }

            // 받은 초대
            item {

                SectionTitle(
                    title =
                        "받은 초대",
                    count =
                        uiState
                            .invitations
                            .size
                )
            }

            if (
                uiState.invitations
                    .isEmpty()
            ) {

                item {

                    EmptyChallengeCard(
                        title =
                            "아직 받은 초대가 없어요.",
                        description =
                            "친구의 챌린지 초대를 기다려보세요.",
                        icon =
                            "✉️"
                    )
                }

            } else {

                items(
                    items =
                        uiState.invitations,
                    key = {
                        it.memberId
                    }
                ) { invitation ->

                    InvitationCard(
                        title =
                            invitation.title,

                        ownerNickname =
                            invitation.ownerNickname,

                        // 현재 이 초대를 처리 중인지
                        isProcessing =
                            uiState.processingInvitationId ==
                                    invitation.memberId,

                        onAccept = {

                            viewModel
                                .acceptInvitation(
                                    invitation.memberId
                                )
                        },

                        onReject = {

                            viewModel
                                .rejectInvitation(
                                    invitation.memberId
                                )
                        }
                    )
                }
            }

            // 내 챌린지
            item {

                SectionTitle(
                    title =
                        "내 챌린지",
                    count =
                        uiState
                            .challenges
                            .size
                )
            }

            if (
                uiState.challenges
                    .isEmpty()
            ) {

                item {

                    EmptyChallengeCard(
                        title =
                            "함께하는 챌린지가 아직 없어요.",
                        description =
                            "친구와 작은 목표부터 시작해보세요.",
                        icon =
                            "🌱"
                    )
                }

            } else {

                items(
                    items =
                        uiState.challenges,
                    key = {
                        it.id
                    }
                ) { challenge ->

                    MyChallengeCard(
                        title =
                            challenge.title,

                        description =
                            challenge.description,

                        startDate =
                            challenge.startDate,

                        endDate =
                            challenge.endDate,

                        friendNames =
                            uiState.friends
                                .map { friend ->
                                    friend.nickname
                                },

                        // 추가
                        invitingFriendId =
                            uiState.invitingFriendId,

                        onChallengeClick = {

                            onChallengeClick(
                                challenge.id
                            )
                        },

                        onInviteFriend = { friendId ->

                            viewModel
                                .inviteFriend(
                                    challengeId =
                                        challenge.id,
                                    friendId =
                                        friendId
                                )
                        },

                        friends =
                            uiState.friends
                                .map { friend ->

                                    ChallengeFriendUi(
                                        id =
                                            friend.id,
                                        nickname =
                                            friend.nickname
                                    )
                                }
                    )
                }
            }

            // 챌린지 만들기
            item {

                ChallengeCreateSection(
                    title =
                        uiState.title,

                    description =
                        uiState.description,

                    startDate =
                        uiState.startDate,

                    endDate =
                        uiState.endDate,

                    isSubmitting =
                        uiState.isSubmitting,

                    inputErrorMessage =
                        uiState.inputErrorMessage,

                    onTitleChange =
                        viewModel::onTitleChange,

                    onDescriptionChange =
                        viewModel::onDescriptionChange,

                    onStartDateChange =
                        viewModel::onStartDateChange,

                    onEndDateChange =
                        viewModel::onEndDateChange,

                    onCreate = {

                        viewModel.createChallenge()
                    }
                )
            }
        }
    }
}

@Composable
private fun ChallengeHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .background(
                Color(0xFFF7F3E9)
            )
    ) {

        ChallengeLandscape(
            modifier = Modifier
                .align(
                    Alignment.BottomEnd
                )
                .width(250.dp)
                .height(145.dp)
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
                    text =
                        "챌린지",
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
                            9.dp
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
                        5.dp
                    )
            )

            Text(
                text =
                    "친구와 함께 목표를 이어가보세요.",
                style =
                    MaterialTheme
                        .typography
                        .bodyLarge,
                color =
                    Color(
                        0xFF66766E
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        17.dp
                    )
            )

            Text(
                text =
                    "함께하는 매일이\n더 좋은 습관이 돼요. 🌱",
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,
                color =
                    Color(
                        0xFF4E7160
                    )
            )
        }
    }
}

@Composable
private fun ChallengeLandscape(
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
                    0xFFF4B36F
                ),
            radius =
                size.width *
                        0.11f,
            center =
                Offset(
                    size.width *
                            0.78f,
                    size.height *
                            0.34f
                )
        )

        // 뒤 산
        val backMountain =
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
                            0.18f,
                    size.height *
                            0.61f,
                    size.width *
                            0.29f,
                    size.height *
                            0.48f,
                    size.width *
                            0.44f,
                    size.height *
                            0.58f
                )

                cubicTo(
                    size.width *
                            0.60f,
                    size.height *
                            0.69f,
                    size.width *
                            0.70f,
                    size.height *
                            0.47f,
                    size.width *
                            0.84f,
                    size.height *
                            0.56f
                )

                cubicTo(
                    size.width *
                            0.91f,
                    size.height *
                            0.60f,
                    size.width *
                            0.96f,
                    size.height *
                            0.57f,
                    size.width,
                    size.height *
                            0.52f
                )

                lineTo(
                    size.width,
                    size.height
                )

                close()
            }

        drawPath(
            path =
                backMountain,
            color =
                Color(
                    0xFFDCE5D7
                )
        )

        // 앞 산
        val frontMountain =
            Path().apply {

                moveTo(
                    0f,
                    size.height
                )

                lineTo(
                    0f,
                    size.height *
                            0.88f
                )

                cubicTo(
                    size.width *
                            0.22f,
                    size.height *
                            0.69f,
                    size.width *
                            0.40f,
                    size.height *
                            0.66f,
                    size.width *
                            0.57f,
                    size.height *
                            0.80f
                )

                cubicTo(
                    size.width *
                            0.72f,
                    size.height *
                            0.92f,
                    size.width *
                            0.87f,
                    size.height *
                            0.72f,
                    size.width,
                    size.height *
                            0.69f
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
                Color(
                    0xFFB4C7AD
                )
        )
    }
}

@Composable
private fun ChallengeGrowthCard(
    challengeCount: Int,
    invitationCount: Int
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(
                26.dp
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
                        2.dp
                )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    20.dp
                )
        ) {

            Text(
                text =
                    "함께하는 성장",
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
                    Modifier.height(
                        18.dp
                    )
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                GrowthCountItem(
                    icon =
                        "👥",
                    label =
                        "진행 중",
                    value =
                        challengeCount,
                    modifier =
                        Modifier.weight(
                            1f
                        )
                )

                GrowthCountItem(
                    icon =
                        "✉️",
                    label =
                        "받은 초대",
                    value =
                        invitationCount,
                    modifier =
                        Modifier.weight(
                            1f
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )

            Text(
                text =
                    "친구와 같이 하면\n조금 더 오래 이어갈 수 있어요. 🌱",
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,
                color =
                    Color(
                        0xFF5E7467
                    )
            )
        }
    }
}

@Composable
private fun GrowthCountItem(
    icon: String,
    label: String,
    value: Int,
    modifier: Modifier =
        Modifier
) {

    Row(
        modifier =
            modifier,
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(
                    CircleShape
                )
                .background(
                    Color(
                        0xFFEEF3E9
                    )
                ),
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text =
                    icon,
                style =
                    MaterialTheme
                        .typography
                        .titleLarge
            )
        }

        Spacer(
            modifier =
                Modifier.width(
                    10.dp
                )
        )

        Column {

            Text(
                text =
                    label,
                style =
                    MaterialTheme
                        .typography
                        .bodySmall,
                color =
                    Color(
                        0xFF718078
                    )
            )

            Text(
                text =
                    value.toString(),
                style =
                    MaterialTheme
                        .typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(
                        0xFF163E34
                    )
            )
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    count: Int
) {

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
                title,
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
                "${count}개",
            style =
                MaterialTheme
                    .typography
                    .bodyMedium,
            color =
                Color(
                    0xFF7E8580
                )
        )
    }
}

@Composable
private fun InvitationCard(
    title: String,
    ownerNickname: String,
    isProcessing: Boolean,
    onAccept: () -> Unit,
    onReject: () -> Unit
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
                ),
        elevation =
            CardDefaults
                .cardElevation(
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

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(
                        CircleShape
                    )
                    .background(
                        Color(
                            0xFFEAF2E6
                        )
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

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
                    Modifier.width(
                        13.dp
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
                        "${ownerNickname}님이 초대했어요!",
                    style =
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        Color(
                            0xFF7A847E
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
                        title,
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF25372F
                        ),
                    maxLines = 1,
                    overflow =
                        TextOverflow
                            .Ellipsis
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            8.dp
                        )
                ) {

                    OutlinedButton(
                        onClick =
                            onReject,

                        // 처리 중이면 비활성화
                        enabled =
                            !isProcessing,

                        shape =
                            RoundedCornerShape(
                                50.dp
                            ),

                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            "거절"
                        )
                    }

                    Button(
                        onClick =
                            onAccept,

                        // 처리 중이면 비활성화
                        enabled =
                            !isProcessing,

                        shape =
                            RoundedCornerShape(
                                50.dp
                            ),

                        modifier =
                            Modifier.weight(1f),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFF658868)
                            )
                    ) {

                        Text(
                            text =
                                if (isProcessing) {
                                    "처리 중..."
                                } else {
                                    "참여하기"
                                }
                        )
                    }
                }
            }
        }
    }
}

private data class ChallengeFriendUi(
    val id: Long,
    val nickname: String
)

@Composable
private fun MyChallengeCard(
    title: String,
    description: String?,
    startDate: String,
    endDate: String,
    friendNames: List<String>,
    friends: List<ChallengeFriendUi>,
    invitingFriendId: Long?,
    onChallengeClick: () -> Unit,
    onInviteFriend: (Long) -> Unit
) {

    Card(
        onClick =
            onChallengeClick,
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
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(
                            RoundedCornerShape(
                                17.dp
                            )
                        )
                        .background(
                            Color(
                                0xFFFFF0DE
                            )
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            challengeIcon(
                                title
                            ),
                        style =
                            MaterialTheme
                                .typography
                                .headlineMedium
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(
                            13.dp
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
                            title,
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(
                                0xFF25362F
                            ),
                        maxLines = 1,
                        overflow =
                            TextOverflow
                                .Ellipsis
                    )

                    if (
                        !description
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
                                description,
                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall,
                            color =
                                Color(
                                    0xFF7A847E
                                ),
                            maxLines = 1,
                            overflow =
                                TextOverflow
                                    .Ellipsis
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(
                                5.dp
                            )
                    )

                    Text(
                        text =
                            "${formatDate(startDate)} ~ ${formatDate(endDate)}",
                        style =
                            MaterialTheme
                                .typography
                                .bodySmall,
                        color =
                            Color(
                                0xFF777F7A
                            )
                    )
                }

                Surface(
                    shape =
                        RoundedCornerShape(
                            50.dp
                        ),
                    color =
                        Color(
                            0xFFFFE8D5
                        )
                ) {

                    Text(
                        text =
                            calculateDday(
                                endDate
                            ),
                        modifier =
                            Modifier.padding(
                                horizontal =
                                    10.dp,
                                vertical =
                                    6.dp
                            ),
                        style =
                            MaterialTheme
                                .typography
                                .labelLarge,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(
                                0xFFD46F2A
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(
                            7.dp
                        )
                )

                Text(
                    text = "›",
                    style =
                        MaterialTheme
                            .typography
                            .headlineSmall,
                    color =
                        Color(
                            0xFF708078
                        )
                )
            }

            if (
                friends.isNotEmpty()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            14.dp
                        )
                )

                HorizontalDivider(
                    color =
                        Color(
                            0xFFF0F1ED
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                Text(
                    text =
                        "친구 초대",
                    style =
                        MaterialTheme
                            .typography
                            .labelLarge,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF557064
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )

                friends.forEach {
                        friend ->

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(
                                    28.dp
                                )
                                .clip(
                                    CircleShape
                                )
                                .background(
                                    Color(
                                        0xFFE9F0E5
                                    )
                                ),
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text =
                                    friend.nickname
                                        .firstOrNull()
                                        ?.toString()
                                        ?: "?"
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.width(
                                    8.dp
                                )
                        )

                        Text(
                            text =
                                friend.nickname,
                            modifier =
                                Modifier.weight(
                                    1f
                                ),
                            style =
                                MaterialTheme
                                    .typography
                                    .bodyMedium
                        )

                        OutlinedButton(
                            onClick = {

                                onInviteFriend(
                                    friend.id
                                )
                            },

                            // 이 친구에게 초대 보내는 중이면 잠금
                            enabled =
                                invitingFriendId !=
                                        friend.id,

                            shape =
                                RoundedCornerShape(
                                    50.dp
                                )
                        ) {

                            Text(
                                text =
                                    if (
                                        invitingFriendId ==
                                        friend.id
                                    ) {
                                        "초대 중..."
                                    } else {
                                        "초대"
                                    }
                            )
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(
                                6.dp
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun ChallengeCreateSection(
    title: String,
    description: String,
    startDate: String,
    endDate: String,
    isSubmitting: Boolean,
    inputErrorMessage: String?,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onStartDateChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onCreate: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            )
    ) {

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "🌱",
                style =
                    MaterialTheme.typography
                        .headlineMedium
            )

            Spacer(
                modifier =
                    Modifier.width(8.dp)
            )

            Column {

                Text(
                    text =
                        "챌린지 만들기",
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

                Text(
                    text =
                        "나만의 챌린지를 만들고 친구와 함께 도전해보세요!",
                    style =
                        MaterialTheme.typography
                            .bodySmall,
                    color =
                        Color(
                            0xFF76827B
                        )
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(
                    24.dp
                ),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color(
                            0xFFF4F7F0
                        )
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(
                        16.dp
                    )
            ) {

                // 챌린지 이름
                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            "챌린지 이름",
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
                            Modifier.weight(1f)
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
                        Modifier.height(6.dp)
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
                            "예) 하루 10분 명상하기"
                        )
                    },

                    enabled =
                        !isSubmitting,

                    shape =
                        RoundedCornerShape(
                            15.dp
                        ),

                    singleLine =
                        true
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
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
                            Modifier.weight(1f)
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
                        Modifier.height(6.dp)
                )

                OutlinedTextField(
                    value =
                        description,

                    onValueChange =
                        onDescriptionChange,

                    modifier =
                        Modifier.fillMaxWidth(),

                    placeholder = {

                        Text(
                            "어떤 챌린지인가요?"
                        )
                    },

                    enabled =
                        !isSubmitting,

                    shape =
                        RoundedCornerShape(
                            15.dp
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                Text(
                    text =
                        "기간",
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
                        Modifier.height(6.dp)
                )

                Row(
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            8.dp
                        )
                ) {

                    OutlinedTextField(
                        value =
                            startDate,

                        onValueChange =
                            onStartDateChange,

                        modifier =
                            Modifier.weight(1f),

                        label = {

                            Text(
                                "시작일"
                            )
                        },

                        placeholder = {

                            Text(
                                "2026-09-15"
                            )
                        },

                        enabled =
                            !isSubmitting,

                        shape =
                            RoundedCornerShape(
                                15.dp
                            ),

                        singleLine =
                            true
                    )

                    OutlinedTextField(
                        value =
                            endDate,

                        onValueChange =
                            onEndDateChange,

                        modifier =
                            Modifier.weight(1f),

                        label = {

                            Text(
                                "종료일"
                            )
                        },

                        placeholder = {

                            Text(
                                "2026-09-30"
                            )
                        },

                        enabled =
                            !isSubmitting,

                        shape =
                            RoundedCornerShape(
                                15.dp
                            ),

                        singleLine =
                            true
                    )
                }

                // 입력 검증 오류
                if (
                    !inputErrorMessage
                        .isNullOrBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
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
                                inputErrorMessage,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        10.dp
                                    ),
                            style =
                                MaterialTheme.typography
                                    .bodySmall,
                            color =
                                Color(
                                    0xFFB85F3E
                                )
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                Button(
                    onClick =
                        onCreate,

                    // 요청 중에는 다시 누를 수 없음
                    enabled =
                        !isSubmitting,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            52.dp
                        ),

                    shape =
                        RoundedCornerShape(
                            18.dp
                        ),

                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                Color(
                                    0xFF628468
                                )
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
                                "만드는 중..."
                        )

                    } else {

                        Text(
                            text =
                                "＋  챌린지 만들기",
                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyChallengeCard(
    title: String,
    description: String,
    icon: String
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
                        Color(
                            0xFFF1F4EC
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
                    MaterialTheme
                        .typography
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
                        title,
                    style =
                        MaterialTheme
                            .typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF41544A
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
                        MaterialTheme
                            .typography
                            .bodySmall,
                    color =
                        Color(
                            0xFF7A847E
                        )
                )
            }
        }
    }
}

private fun calculateDday(
    endDate: String
): String {

    return try {

        val today =
            LocalDate.now()

        val end =
            LocalDate.parse(
                endDate,
                DateTimeFormatter
                    .ISO_LOCAL_DATE
            )

        val days =
            ChronoUnit.DAYS
                .between(
                    today,
                    end
                )

        when {

            days > 0 ->
                "D-$days"

            days == 0L ->
                "D-Day"

            else ->
                "종료"
        }

    } catch (
        e: Exception
    ) {

        ""
    }
}

private fun formatDate(
    date: String
): String {

    return try {

        val localDate =
            LocalDate.parse(
                date,
                DateTimeFormatter
                    .ISO_LOCAL_DATE
            )

        "${localDate.monthValue}/${localDate.dayOfMonth}"

    } catch (
        e: Exception
    ) {

        date
    }
}

private fun challengeIcon(
    title: String
): String {

    return when {

        title.contains("물") ->
            "💧"

        title.contains("책") ||
                title.contains("독서") ->
            "📖"

        title.contains("잠") ||
                title.contains("수면") ->
            "🌙"

        title.contains("운동") ||
                title.contains("스트레칭") ->
            "🏃"

        title.contains("공부") ->
            "📚"

        else ->
            "🌱"
    }
}