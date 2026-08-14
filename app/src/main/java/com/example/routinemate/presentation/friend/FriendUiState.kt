package com.example.routinemate.presentation.friend

import com.example.routinemate.data.remote.dto.friend.FriendRequestResponse
import com.example.routinemate.data.remote.dto.friend.FriendUserResponse

data class FriendUiState(

    // 현재 친구 목록
    val friends: List<FriendUserResponse> = emptyList(),

    // 내가 받은 친구 요청
    val receivedRequests: List<FriendRequestResponse> = emptyList(),

    // 사용자 검색 결과
    val searchResults: List<FriendUserResponse> = emptyList(),

    // 검색어
    val searchKeyword: String = "",

    // 화면 로딩 상태
    val isLoading: Boolean = false,

    // 성공 메시지
    val successMessage: String? = null,

    // 에러 메시지
    val errorMessage: String? = null
)