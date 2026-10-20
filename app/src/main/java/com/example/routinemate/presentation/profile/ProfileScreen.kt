package com.example.routinemate.presentation.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.routinemate.presentation.statistics.StatisticsViewModel
import java.time.LocalDate
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.example.routinemate.data.remote.NetworkConstants

@Composable
fun ProfileScreen(
    onHabitClick: () -> Unit,
    onFriendClick: () -> Unit,
    onChallengeClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    statisticsViewModel: StatisticsViewModel = hiltViewModel()
) {

    val uiState by
    viewModel.uiState.collectAsState()

    // 통계 화면에서 사용하는 같은 데이터
    val statisticsUiState by
    statisticsViewModel.uiState
        .collectAsStateWithLifecycle()

    val snackbarHostState =
        androidx.compose.runtime.remember {
            SnackbarHostState()
        }

    val context =
        LocalContext.current

    // 갤러리에서 프로필 이미지 선택
    val profileImageLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {

                val file =
                    uriToProfileImagePart(
                        context = context,
                        uri = uri
                    )

                viewModel.uploadProfileImage(
                    file = file
                )
            }
        }

    // 프로필 화면 진입 시 최근 7일 통계 조회
    LaunchedEffect(Unit) {
        statisticsViewModel
            .loadWeeklyStatistics()
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

    // 에러 메시지
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

        when {

            // 최초 프로필 조회
            uiState.isLoading &&
                    uiState.profile == null -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            innerPadding
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color =
                            Color(0xFF6E8D71)
                    )
                }
            }

            uiState.profile != null -> {

                val profile =
                    uiState.profile!!

                val summary =
                    uiState.summary

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

                        ProfileHeader()
                    }

                    // 프로필 카드
                    item {

                        Box(
                            modifier =
                                Modifier.padding(
                                    horizontal = 20.dp
                                )
                        ) {

                            ProfileHeroCard(
                                nickname =
                                    profile.nickname,
                                email =
                                    profile.email,

                                profileImageUrl =
                                    profile.profileImageUrl,

                                onImageClick = {

                                    if (
                                        !uiState.isSavingProfileImage
                                    ) {
                                        profileImageLauncher.launch(
                                            "image/*"
                                        )
                                    }
                                },

                                isSavingProfileImage =
                                    uiState.isSavingProfileImage,

                                onEditClick = {

                                    viewModel
                                        .startNicknameEdit()
                                }
                            )
                        }
                    }

                    // 나의 활동
                    item {

                        Column {

                            SectionTitle(
                                title = "나의 활동"
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        12.dp
                                    )
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal =
                                            20.dp
                                    ),
                                horizontalArrangement =
                                    Arrangement.spacedBy(
                                        10.dp
                                    )
                            ) {

                                ActivityCard(
                                    icon = "🌱",
                                    title = "습관",
                                    value =
                                        summary
                                            ?.habitCount
                                            ?: 0,
                                    modifier =
                                        Modifier.weight(1f),
                                    onClick =
                                        onHabitClick
                                )

                                ActivityCard(
                                    icon = "👥",
                                    title = "친구",
                                    value =
                                        summary
                                            ?.friendCount
                                            ?: 0,
                                    modifier =
                                        Modifier.weight(1f),
                                    onClick =
                                        onFriendClick
                                )

                                ActivityCard(
                                    icon = "🚩",
                                    title = "챌린지",
                                    value =
                                        summary
                                            ?.challengeCount
                                            ?: 0,
                                    modifier =
                                        Modifier.weight(1f),
                                    onClick =
                                        onChallengeClick
                                )
                            }
                        }
                    }

                    // 나의 기록
                    item {

                        Column {

                            SectionTitle(
                                title = "나의 기록"
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        12.dp
                                    )
                            )

                            MyRecordCard(
                                activeDaysSinceSignup =
                                    summary
                                        ?.activeDaysSinceSignup
                                        ?: 0,

                                topHabitCategory =
                                    summary
                                        ?.topHabitCategory,

                                totalChallengeParticipationCount =
                                    summary
                                        ?.totalChallengeParticipationCount
                                        ?: 0
                            )
                        }
                    }

                    // 이번 주 성장
                    item {

                        Column {

                            SectionTitle(
                                title = "이번 주 성장"
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        12.dp
                                    )
                            )

                            WeeklyGrowthCard(
                                averageCompletionRate =
                                    statisticsUiState
                                        .averageCompletionRate,

                                totalCompletedCount =
                                    statisticsUiState
                                        .totalCompletedCount,

                                bestDay =
                                    statisticsUiState
                                        .bestDay,

                                isLoading =
                                    statisticsUiState
                                        .isLoading
                            )
                        }
                    }

                    // 성장 메시지
                    item {

                        GrowthMessageCard(
                            habitCount =
                                summary
                                    ?.habitCount
                                    ?: 0,
                            challengeCount =
                                summary
                                    ?.challengeCount
                                    ?: 0,
                            averageCompletionRate =
                                statisticsUiState
                                    .averageCompletionRate
                        )
                    }

                    // 바로가기
                    item {

                        Column {

                            SectionTitle(
                                title = "바로가기"
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        12.dp
                                    )
                            )

                            QuickMenuCard(
                                onFriendClick =
                                    onFriendClick,
                                onChallengeClick =
                                    onChallengeClick,
                                onHabitClick =
                                    onHabitClick
                            )
                        }
                    }

                    // 로그아웃
                    item {

                        OutlinedButton(
                            onClick =
                                onLogout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal =
                                        20.dp
                                )
                                .height(
                                    54.dp
                                ),
                            shape =
                                RoundedCornerShape(
                                    18.dp
                                )
                        ) {

                            Text(
                                text =
                                    "↪  로그아웃",
                                color =
                                    Color(
                                        0xFF6F7772
                                    ),
                                fontWeight =
                                    FontWeight.Medium
                            )
                        }
                    }
                }
            }

            else -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            innerPadding
                        )
                        .padding(
                            20.dp
                        ),
                    horizontalAlignment =
                        Alignment.CenterHorizontally,
                    verticalArrangement =
                        Arrangement.Center
                ) {

                    Text(
                        text =
                            "프로필 정보를 불러올 수 없습니다.",
                        style =
                            MaterialTheme.typography
                                .bodyLarge
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                12.dp
                            )
                    )

                    Button(
                        onClick = {

                            viewModel
                                .loadProfile()
                        },
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        Color(
                                            0xFF6D8C70
                                        )
                                )
                    ) {

                        Text(
                            "다시 시도"
                        )
                    }
                }
            }
        }
    }

    // 닉네임 수정 창
    if (
        uiState.isEditingNickname
    ) {

        AlertDialog(
            onDismissRequest = {

                viewModel
                    .cancelNicknameEdit()
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
                            "닉네임 수정",
                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(
                                0xFF163B31
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
                            "새로운 닉네임을 입력해주세요.",
                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium,
                        color =
                            Color(
                                0xFF7B847E
                            )
                    )
                }
            },

            text = {

                Column {

                    OutlinedTextField(
                        value =
                            uiState.nicknameInput,

                        onValueChange = {

                            viewModel
                                .updateNicknameInput(
                                    it
                                )
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text(
                                "닉네임"
                            )
                        },

                        // 검증 오류가 있으면 빨간 테두리
                        isError =
                            uiState.nicknameErrorMessage !=
                                    null,

                        // 저장 중에는 수정 불가
                        enabled =
                            !uiState.isSavingNickname,

                        shape =
                            RoundedCornerShape(
                                16.dp
                            ),

                        singleLine =
                            true
                    )

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Row(
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        // 입력 오류
                        Text(
                            text =
                                uiState.nicknameErrorMessage
                                    ?: "",
                            modifier =
                                Modifier.weight(1f),
                            style =
                                MaterialTheme.typography
                                    .bodySmall,
                            color =
                                MaterialTheme.colorScheme
                                    .error
                        )

                        // 글자 수 표시
                        Text(
                            text =
                                "${uiState.nicknameInput.length} / 20",
                            style =
                                MaterialTheme.typography
                                    .bodySmall,
                            color =
                                Color(0xFF8A918C)
                        )
                    }
                }
            },

            confirmButton = {

                Button(
                    onClick = {

                        viewModel
                            .saveNickname()
                    },

                    // 저장 중에는 다시 클릭 불가
                    enabled =
                        !uiState.isSavingNickname,

                    shape =
                        RoundedCornerShape(
                            50.dp
                        ),

                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    Color(
                                        0xFF6C8C70
                                    )
                            )
                ) {

                    if (
                        uiState.isSavingNickname
                    ) {

                        CircularProgressIndicator(
                            modifier =
                                Modifier.size(18.dp),
                            strokeWidth =
                                2.dp,
                            color =
                                Color.White
                        )

                        Spacer(
                            modifier =
                                Modifier.width(8.dp)
                        )

                        Text(
                            "저장 중..."
                        )

                    } else {

                        Text(
                            "저장"
                        )
                    }
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {

                        viewModel
                            .cancelNicknameEdit()
                    },

                    enabled =
                        !uiState.isSavingNickname
                ) {

                    Text(
                        text = "취소",
                        color =
                            Color(0xFF69736D)
                    )
                }
            }
        )
    }
}

@Composable
private fun ProfileHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(
                Color(0xFFF7F3E9)
            )
    ) {

        ProfileLandscape(
            modifier = Modifier
                .align(
                    Alignment.BottomEnd
                )
                .width(220.dp)
                .height(120.dp)
        )

        Column(
            modifier = Modifier
                .align(
                    Alignment.CenterStart
                )
                .padding(
                    horizontal =
                        20.dp
                )
        ) {

            Text(
                text =
                    "내 페이지",
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
                    "오늘도 조금씩,\n더 나은 내가 되어가요.",
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
private fun ProfileLandscape(
    modifier: Modifier = Modifier
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
                size.width * 0.11f,
            center =
                Offset(
                    size.width * 0.77f,
                    size.height * 0.33f
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
                    size.height * 0.76f
                )

                cubicTo(
                    size.width * 0.18f,
                    size.height * 0.58f,
                    size.width * 0.31f,
                    size.height * 0.49f,
                    size.width * 0.48f,
                    size.height * 0.61f
                )

                cubicTo(
                    size.width * 0.66f,
                    size.height * 0.75f,
                    size.width * 0.81f,
                    size.height * 0.49f,
                    size.width,
                    size.height * 0.59f
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
                Color(
                    0xFFDCE5D7
                )
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
                    size.height * 0.90f
                )

                cubicTo(
                    size.width * 0.23f,
                    size.height * 0.73f,
                    size.width * 0.38f,
                    size.height * 0.70f,
                    size.width * 0.57f,
                    size.height * 0.83f
                )

                cubicTo(
                    size.width * 0.73f,
                    size.height * 0.95f,
                    size.width * 0.87f,
                    size.height * 0.77f,
                    size.width,
                    size.height * 0.71f
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
                Color(
                    0xFFB2C5AB
                )
        )
    }
}

@Composable
private fun ProfileHeroCard(
    nickname: String,
    email: String,
    profileImageUrl: String?,
    onImageClick: () -> Unit,
    isSavingProfileImage: Boolean,
    onEditClick: () -> Unit
) {

    Card(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(
                26.dp
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
                .padding(
                    20.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // 프로필 이미지 대신 닉네임 첫 글자
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(
                        CircleShape
                    )
                    .background(
                        Color(
                            0xFFDDE9D8
                        )
                    )
                    .clickable(
                        enabled =
                            !isSavingProfileImage
                    ) {
                        onImageClick()
                    },
                contentAlignment =
                    Alignment.Center
            ) {

                if (
                    isSavingProfileImage
                ) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(28.dp),
                        strokeWidth =
                            3.dp,
                        color =
                            Color(0xFF527157)
                    )

                } else if (
                    !profileImageUrl.isNullOrBlank()
                ) {

                    AsyncImage(
                        model =
                            "${NetworkConstants.BASE_URL.removeSuffix("/")}$profileImageUrl",
                        contentDescription =
                            "프로필 이미지",
                        modifier =
                            Modifier.fillMaxSize(),
                        contentScale =
                            ContentScale.Crop
                    )

                } else {

                    Text(
                        text =
                            nickname
                                .firstOrNull()
                                ?.toString()
                                ?.uppercase()
                                ?: "R",
                        style =
                            MaterialTheme.typography
                                .headlineLarge,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            Color(0xFF527157)
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.width(
                        16.dp
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
                            .titleLarge,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF173D33
                        ),
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis
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
                            0xFF7C857F
                        ),
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )

                Text(
                    text =
                        "🌿 오늘도 조금씩 성장하고 있어요.",
                    style =
                        MaterialTheme.typography
                            .bodySmall,
                    color =
                        Color(
                            0xFF61786A
                        )
                )
            }

            Surface(
                modifier =
                    Modifier.clickable {
                        onEditClick()
                    },
                shape =
                    CircleShape,
                color =
                    Color(
                        0xFFF0F3ED
                    )
            ) {

                Box(
                    modifier =
                        Modifier.size(
                            42.dp
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = "✎",
                        style =
                            MaterialTheme.typography
                                .titleLarge,
                        color =
                            Color(
                                0xFF62736A
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String
) {

    Text(
        text =
            title,
        modifier =
            Modifier.padding(
                horizontal =
                    20.dp
            ),
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
}

@Composable
private fun ActivityCard(
    icon: String,
    title: String,
    value: Long,
    onClick: () -> Unit,
    modifier: Modifier =
        Modifier
) {

    Card(
        modifier =
            modifier.clickable {
                onClick()
            },
        shape =
            RoundedCornerShape(
                20.dp
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 14.dp,
                    horizontal = 8.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
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
                    text = icon,
                    style =
                        MaterialTheme.typography
                            .titleLarge
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        7.dp
                    )
            )

            Text(
                text =
                    title,
                style =
                    MaterialTheme.typography
                        .bodySmall,
                color =
                    Color(
                        0xFF6F7B74
                    )
            )

            Text(
                text =
                    "$value${activityUnit(title)}",
                style =
                    MaterialTheme.typography
                        .titleLarge,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(
                        0xFF173F34
                    )
            )
        }
    }
}

@Composable
private fun MyRecordCard(
    activeDaysSinceSignup: Long,
    topHabitCategory: String?,
    totalChallengeParticipationCount: Long
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
                defaultElevation = 1.dp
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 18.dp,
                    vertical = 8.dp
                )
        ) {

            RecordRow(
                icon = "📅",
                title = "함께한 지",
                value = "${activeDaysSinceSignup}일"
            )

            RecordDivider()

            RecordRow(
                icon = "🌱",
                title = "가장 많이 한 습관",
                value =
                    habitCategoryLabel(
                        topHabitCategory
                    )
            )

            RecordDivider()

            RecordRow(
                icon = "🚩",
                title = "누적 챌린지",
                value =
                    "${totalChallengeParticipationCount}개"
            )
        }
    }
}

@Composable
private fun RecordRow(
    icon: String,
    title: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 14.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
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
                text = icon,
                style =
                    MaterialTheme.typography
                        .titleMedium
            )
        }

        Spacer(
            modifier =
                Modifier.width(
                    12.dp
                )
        )

        Text(
            text = title,
            modifier =
                Modifier.weight(1f),
            style =
                MaterialTheme.typography
                    .bodyMedium,
            color =
                Color(
                    0xFF6F7B74
                )
        )

        Text(
            text = value,
            style =
                MaterialTheme.typography
                    .titleMedium,
            fontWeight =
                FontWeight.Bold,
            color =
                Color(
                    0xFF173F34
                )
        )
    }
}

@Composable
private fun RecordDivider() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 54.dp
            )
            .height(1.dp)
            .background(
                Color(
                    0xFFF0F1EE
                )
            )
    )
}

@Composable
private fun WeeklyGrowthCard(
    averageCompletionRate: Int,
    totalCompletedCount: Int,
    bestDay: String?,
    isLoading: Boolean
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
                26.dp
            ),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation =
                    2.dp
            )
    ) {

        if (isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        180.dp
                    ),
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

        } else {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        18.dp
                    ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                // 이번 주 평균 달성률
                Box(
                    modifier =
                        Modifier.size(
                            120.dp
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator(
                        progress = {
                            (
                                    averageCompletionRate /
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
                                0xFFE9ECE6
                            )
                    )

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text =
                                "이번 주",
                            style =
                                MaterialTheme.typography
                                    .bodySmall,
                            color =
                                Color(
                                    0xFF748078
                                )
                        )

                        Text(
                            text =
                                "$averageCompletionRate%",
                            style =
                                MaterialTheme.typography
                                    .headlineMedium,
                            fontWeight =
                                FontWeight.ExtraBold,
                            color =
                                Color(
                                    0xFF163F34
                                )
                        )

                        Text(
                            text =
                                "달성률",
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

                Spacer(
                    modifier =
                        Modifier.width(
                            20.dp
                        )
                )

                Column(
                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    WeeklyGrowthMetric(
                        icon =
                            "✓",
                        label =
                            "총 완료 횟수",
                        value =
                            "${totalCompletedCount}회",
                        accentColor =
                            Color(
                                0xFF648266
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )

                    WeeklyGrowthMetric(
                        icon =
                            "☀",
                        label =
                            "가장 잘한 날",
                        value =
                            bestDay?.let {
                                formatProfileDate(
                                    it
                                )
                            } ?: "-",
                        accentColor =
                            Color(
                                0xFFE58A45
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun WeeklyGrowthMetric(
    icon: String,
    label: String,
    value: String,
    accentColor: Color
) {

    Row(
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(
                    CircleShape
                )
                .background(
                    accentColor.copy(
                        alpha = 0.13f
                    )
                ),
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text =
                    icon,
                color =
                    accentColor,
                fontWeight =
                    FontWeight.Bold
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
                    MaterialTheme.typography
                        .bodySmall,
                color =
                    Color(
                        0xFF79827C
                    )
            )

            Text(
                text =
                    value,
                style =
                    MaterialTheme.typography
                        .titleMedium,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(
                        0xFF173E34
                    )
            )
        }
    }
}

@Composable
private fun GrowthMessageCard(
    habitCount: Long,
    challengeCount: Long,
    averageCompletionRate: Int
) {

    val message =
        when {

            averageCompletionRate >=
                    80 ->

                "이번 주 정말 잘하고 있어요!"

            averageCompletionRate >=
                    50 ->

                "좋은 흐름을 이어가고 있어요!"

            habitCount > 0 &&
                    challengeCount > 0 ->

                "꾸준히 성장하고 있어요!"

            habitCount > 0 ->

                "작은 습관이 쌓이고 있어요!"

            else ->

                "오늘부터 시작해볼까요?"
        }

    val description =
        when {

            averageCompletionRate >=
                    80 ->

                "지금처럼 꾸준히 이어가면 더 좋은 변화가 생길 거예요."

            averageCompletionRate >=
                    50 ->

                "조금씩 쌓이는 기록이 더 좋은 하루를 만들고 있어요."

            habitCount > 0 ->

                "작은 습관 하나하나가 성장으로 이어지고 있어요."

            else ->

                "작은 습관 하나가 새로운 변화를 만들 수 있어요."
        }

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

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(
                        CircleShape
                    )
                    .background(
                        Color(
                            0xFFDCE8D7
                        )
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
                    Modifier.width(
                        14.dp
                    )
            )

            Column {

                Text(
                    text =
                        message,
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        Color(
                            0xFF244A3B
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
                            0xFF6F7E76
                        )
                )
            }
        }
    }
}

@Composable
private fun QuickMenuCard(
    onFriendClick: () -> Unit,
    onChallengeClick: () -> Unit,
    onHabitClick: () -> Unit
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

        Column {

            QuickMenuRow(
                icon = "👥",
                title =
                    "친구 관리",
                description =
                    "친구 검색과 요청을 관리해요.",
                onClick =
                    onFriendClick
            )

            QuickMenuDivider()

            QuickMenuRow(
                icon = "🚩",
                title =
                    "내 챌린지",
                description =
                    "함께하는 목표를 확인해요.",
                onClick =
                    onChallengeClick
            )

            QuickMenuDivider()

            QuickMenuRow(
                icon = "✓",
                title =
                    "습관 관리",
                description =
                    "나의 루틴을 관리해요.",
                onClick =
                    onHabitClick
            )
        }
    }
}

@Composable
private fun QuickMenuRow(
    icon: String,
    title: String,
    description: String,
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
                .size(42.dp)
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
                    icon,
                style =
                    MaterialTheme.typography
                        .titleMedium
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
                    title,
                style =
                    MaterialTheme.typography
                        .titleMedium,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(
                        0xFF31433A
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        2.dp
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
                        0xFF838B86
                    )
            )
        }

        Text(
            text = "›",
            style =
                MaterialTheme.typography
                    .headlineSmall,
            color =
                Color(
                    0xFF808982
                )
        )
    }
}

@Composable
private fun QuickMenuDivider() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 70.dp,
                end = 16.dp
            )
            .height(1.dp)
            .background(
                Color(
                    0xFFF0F1EE
                )
            )
    )
}

private fun activityUnit(
    title: String
): String {

    return when (title) {

        "친구" ->
            "명"

        else ->
            "개"
    }
}

private fun habitCategoryLabel(
    category: String?
): String {

    return when (category) {

        "WATER" ->
            "물 마시기"

        "BOOK" ->
            "독서"

        "SLEEP" ->
            "수면"

        "EXERCISE" ->
            "운동"

        "STUDY" ->
            "공부"

        else ->
            "-"
    }
}

// 2026-09-15 → 9/15
private fun formatProfileDate(
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