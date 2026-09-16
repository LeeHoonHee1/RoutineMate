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

    // 화면 전체 로딩
    val isLoading: Boolean = false,

    // 검색 중 여부
    val isSearching: Boolean = false,

    // 친구 요청 보내는 중인 사용자 ID
    val sendingRequestUserId: Long? = null,

    // 수락/거절 처리 중인 친구 요청 ID
    val processingRequestId: Long? = null,

    // 성공 메시지
    val successMessage: String? = null,

    // 에러 메시지
    val errorMessage: String? = null
)