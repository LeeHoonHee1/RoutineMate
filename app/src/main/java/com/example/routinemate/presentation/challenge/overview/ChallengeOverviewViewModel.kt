package com.example.routinemate.presentation.challenge.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.domain.repository.ChallengeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ChallengeOverviewViewModel @Inject constructor(
    private val challengeRepository:
    ChallengeRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            ChallengeOverviewUiState()
        )

    val uiState:
            StateFlow<ChallengeOverviewUiState> =
        _uiState.asStateFlow()

    fun loadChallenges() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                val challenges =
                    challengeRepository
                        .getHomeChallenges()

                _uiState.update {
                    it.copy(
                        challenges =
                            challenges,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "챌린지 정보를 불러오지 못했습니다."
                    )
                }
            }
        }
    }
}