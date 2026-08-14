package com.example.routinemate.presentation.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.domain.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendViewModel @Inject constructor(
    private val friendRepository: FriendRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(FriendUiState())

    val uiState: StateFlow<FriendUiState> =
        _uiState.asStateFlow()

    init {
        loadFriendData()
    }

    // 친구 목록 + 받은 요청 조회
    fun loadFriendData() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                val friends =
                    friendRepository.getFriends()

                val receivedRequests =
                    friendRepository
                        .getReceivedFriendRequests()

                _uiState.update {
                    it.copy(
                        friends = friends,
                        receivedRequests = receivedRequests,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "친구 정보를 불러오지 못했습니다."
                    )
                }
            }
        }
    }

    // 검색어 변경
    fun onSearchKeywordChange(
        keyword: String
    ) {

        _uiState.update {
            it.copy(
                searchKeyword = keyword
            )
        }
    }

    // 사용자 검색
    fun searchUsers() {

        val keyword =
            _uiState.value.searchKeyword.trim()

        if (keyword.isBlank()) {

            _uiState.update {
                it.copy(
                    searchResults = emptyList()
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                val results =
                    friendRepository.searchUsers(
                        keyword = keyword
                    )

                _uiState.update {
                    it.copy(
                        searchResults = results,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            "사용자 검색에 실패했습니다."
                    )
                }
            }
        }
    }

    // 친구 요청 보내기
    fun sendFriendRequest(
        receiverId: Long
    ) {

        viewModelScope.launch {

            try {

                friendRepository.sendFriendRequest(
                    receiverId = receiverId
                )

                _uiState.update { state ->
                    state.copy(
                        searchResults =
                            state.searchResults.filter {
                                it.id != receiverId
                            },
                        successMessage =
                            "친구 요청을 보냈습니다."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        errorMessage =
                            "친구 요청을 보내지 못했습니다."
                    )
                }
            }
        }
    }

    // 친구 요청 수락
    fun acceptFriendRequest(
        requestId: Long
    ) {

        viewModelScope.launch {

            try {

                friendRepository.acceptFriendRequest(
                    requestId = requestId
                )

                // 처리된 요청 먼저 제거
                _uiState.update { state ->
                    state.copy(
                        receivedRequests =
                            state.receivedRequests.filter {
                                it.id != requestId
                            },
                        successMessage =
                            "친구 요청을 수락했습니다."
                    )
                }

                // 친구 목록 새로 조회
                refreshFriends()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        errorMessage =
                            "친구 요청을 수락하지 못했습니다."
                    )
                }
            }
        }
    }

    // 친구 요청 거절
    fun rejectFriendRequest(
        requestId: Long
    ) {

        viewModelScope.launch {

            try {

                friendRepository.rejectFriendRequest(
                    requestId = requestId
                )

                _uiState.update { state ->
                    state.copy(
                        receivedRequests =
                            state.receivedRequests.filter {
                                it.id != requestId
                            },
                        successMessage =
                            "친구 요청을 거절했습니다."
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        errorMessage =
                            "친구 요청을 거절하지 못했습니다."
                    )
                }
            }
        }
    }

    // 친구 목록만 새로 조회
    private suspend fun refreshFriends() {

        try {

            val friends =
                friendRepository.getFriends()

            _uiState.update {
                it.copy(
                    friends = friends
                )
            }

        } catch (e: Exception) {

            _uiState.update {
                it.copy(
                    errorMessage =
                        "친구 목록을 새로고침하지 못했습니다."
                )
            }
        }
    }

    // Snackbar 메시지 초기화
    fun clearMessage() {

        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null
            )
        }
    }
}