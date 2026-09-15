package com.example.routinemate.presentation.challenge.overview

import com.example.routinemate.data.remote.dto.challenge.HomeChallengeResponse

data class ChallengeOverviewUiState(

    val challenges:
    List<HomeChallengeResponse> =
        emptyList(),

    val isLoading: Boolean = false,

    val errorMessage: String? = null
)