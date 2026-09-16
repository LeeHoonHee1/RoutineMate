package com.example.routinemate.presentation.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.data.remote.error.ApiErrorParser
import com.example.routinemate.domain.repository.HabitRepository
import com.example.routinemate.domain.validator.HabitValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val apiErrorParser: ApiErrorParser,
    private val habitValidator: HabitValidator
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            HabitUiState()
        )

    val uiState: StateFlow<HabitUiState> =
        _uiState.asStateFlow()

    // 현재 사용자의 습관 목록 조회
    fun loadHabits() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                val habits =
                    habitRepository.getHabits()

                updateHabitList(
                    habits = habits
                )

                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            parseErrorMessage(
                                exception = e,
                                defaultMessage =
                                    "습관을 불러오지 못했습니다."
                            )
                    )
                }
            }
        }
    }

    // 습관 이름 입력값 변경
    fun onTitleChange(
        title: String
    ) {

        // 최대 50자까지만 입력
        val limitedTitle =
            title.take(
                HabitValidator.TITLE_MAX_LENGTH
            )

        _uiState.update {
            it.copy(
                title = limitedTitle,
                inputErrorMessage = null
            )
        }
    }

    // 습관 설명 입력값 변경
    fun onDescriptionChange(
        description: String
    ) {

        // 최대 200자까지만 입력
        val limitedDescription =
            description.take(
                HabitValidator.DESCRIPTION_MAX_LENGTH
            )

        _uiState.update {
            it.copy(
                description =
                    limitedDescription,
                inputErrorMessage = null
            )
        }
    }

    // 새로운 습관 생성
    fun createHabit() {

        // 이미 저장 요청 중이면 중복 요청 방지
        if (
            uiState.value.isSubmitting
        ) {
            return
        }

        val title =
            uiState.value.title.trim()

        val description =
            uiState.value.description.trim()

        val validationMessage =
            habitValidator.validate(
                title = title,
                description = description
            )

        // 검증 실패
        if (
            validationMessage != null
        ) {

            _uiState.update {
                it.copy(
                    inputErrorMessage =
                        validationMessage
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    errorMessage = null,
                    inputErrorMessage = null,
                    isCreateSuccess = false,
                    isSaveSuccess = false
                )
            }

            try {

                habitRepository.createHabit(
                    title = title,
                    description =
                        description.ifBlank {
                            null
                        }
                )

                // 생성은 성공했으므로 먼저 성공 처리
                _uiState.update {
                    it.copy(
                        title = "",
                        description = "",
                        isSubmitting = false,
                        isCreateSuccess = true,
                        isSaveSuccess = true
                    )
                }

                // 생성된 최신 목록 다시 조회
                refreshHabitsAfterMutation()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage =
                            parseErrorMessage(
                                exception = e,
                                defaultMessage =
                                    "습관을 생성하지 못했습니다."
                            )
                    )
                }
            }
        }
    }

    // 습관 삭제
    fun deleteHabit(
        habitId: Long
    ) {

        if (
            uiState.value.isLoading
        ) {
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

                habitRepository
                    .deleteHabit(
                        habitId
                    )

                refreshHabitsAfterMutation()

                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            parseErrorMessage(
                                exception = e,
                                defaultMessage =
                                    "습관을 삭제하지 못했습니다."
                            )
                    )
                }
            }
        }
    }

    // 수정할 습관 선택
    fun startEditing(
        habitId: Long
    ) {

        val habit =
            uiState.value.habits
                .find {
                    it.id == habitId
                }
                ?: return

        _uiState.update {
            it.copy(
                editingHabitId =
                    habit.id,

                title =
                    habit.title,

                description =
                    habit.description.orEmpty(),

                inputErrorMessage =
                    null,

                errorMessage =
                    null,

                isSaveSuccess =
                    false
            )
        }
    }

    // 선택한 습관 수정
    fun updateHabit() {

        if (
            uiState.value.isSubmitting
        ) {
            return
        }

        val habitId =
            uiState.value.editingHabitId
                ?: return

        val title =
            uiState.value.title.trim()

        val description =
            uiState.value.description.trim()

        val validationMessage =
            habitValidator.validate(
                title = title,
                description = description
            )

        // 검증 실패
        if (
            validationMessage != null
        ) {

            _uiState.update {
                it.copy(
                    inputErrorMessage =
                        validationMessage
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    errorMessage = null,
                    inputErrorMessage = null,
                    isSaveSuccess = false
                )
            }

            try {

                habitRepository.updateHabit(
                    habitId = habitId,
                    title = title,
                    description =
                        description.ifBlank {
                            null
                        }
                )

                _uiState.update {
                    it.copy(
                        title = "",
                        description = "",
                        editingHabitId = null,
                        isSubmitting = false,
                        isSaveSuccess = true
                    )
                }

                // 수정된 최신 목록 조회
                refreshHabitsAfterMutation()

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage =
                            parseErrorMessage(
                                exception = e,
                                defaultMessage =
                                    "습관을 수정하지 못했습니다."
                            )
                    )
                }
            }
        }
    }

    // 습관 수정 취소
    fun cancelEditing() {

        // 저장 요청 중에는 취소하지 않음
        if (
            uiState.value.isSubmitting
        ) {
            return
        }

        _uiState.update {
            it.copy(
                editingHabitId = null,
                title = "",
                description = "",
                inputErrorMessage = null,
                isSaveSuccess = false
            )
        }
    }

    // 생성 / 수정 성공 상태 소비
    fun consumeSaveSuccess() {

        _uiState.update {
            it.copy(
                isSaveSuccess = false,
                isCreateSuccess = false
            )
        }
    }

    // 오류 메시지 제거
    fun clearErrorMessage() {

        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }

    // 오늘 습관 완료 상태 토글
    fun toggleHabitCompletion(
        habitId: Long,
        isCompletedToday: Boolean
    ) {

        // 연속 클릭 방지
        if (
            uiState.value.isLoading
        ) {
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

                if (
                    isCompletedToday
                ) {

                    habitRepository
                        .cancelHabitCompletion(
                            habitId
                        )

                } else {

                    habitRepository
                        .completeHabit(
                            habitId
                        )
                }

                refreshHabitsAfterMutation()

                _uiState.update {
                    it.copy(
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage =
                            parseErrorMessage(
                                exception = e,
                                defaultMessage =
                                    "습관 완료 상태를 변경하지 못했습니다."
                            )
                    )
                }
            }
        }
    }

    // 생성 / 수정 / 삭제 후 최신 목록 조회
    private suspend fun refreshHabitsAfterMutation() {

        try {

            val habits =
                habitRepository.getHabits()

            updateHabitList(
                habits = habits
            )

        } catch (e: Exception) {

            /*
             * 생성/수정 자체는 성공했을 수 있으므로
             * "저장 실패"라고 표시하지 않는다.
             */
            _uiState.update {
                it.copy(
                    errorMessage =
                        "변경사항은 저장됐지만 목록을 새로고침하지 못했습니다."
                )
            }
        }
    }

    // 습관 목록 + 진행률 계산
    private fun updateHabitList(
        habits:
        List<com.example.routinemate.data.remote.dto.habit.HabitResponse>
    ) {

        val completedCount =
            habits.count { habit ->
                habit.isCompletedToday
            }

        val completionRate =
            calculateCompletionRate(
                completedCount =
                    completedCount,
                totalCount =
                    habits.size
            )

        _uiState.update {
            it.copy(
                habits = habits,
                totalHabitCount =
                    habits.size,
                completedHabitCount =
                    completedCount,
                completionRate =
                    completionRate
            )
        }
    }

    // API 오류 메시지 처리
    private fun parseErrorMessage(
        exception: Exception,
        defaultMessage: String
    ): String {

        return if (
            exception is HttpException
        ) {

            apiErrorParser
                .parseMessage(
                    exception
                )
                ?: defaultMessage

        } else {

            defaultMessage
        }
    }

    // 오늘 습관 달성률 계산
    private fun calculateCompletionRate(
        completedCount: Int,
        totalCount: Int
    ): Int {

        if (
            totalCount == 0
        ) {
            return 0
        }

        return (
                completedCount *
                        100.0 /
                        totalCount
                ).roundToInt()
    }
}