package com.example.routinemate.presentation.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.routinemate.data.remote.error.ApiErrorParser
import com.example.routinemate.domain.repository.HabitRepository
import com.example.routinemate.domain.validator.HabitValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val apiErrorParser: ApiErrorParser,
    private val habitValidator: HabitValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitUiState())
    val uiState: StateFlow<HabitUiState> = _uiState.asStateFlow()

    // 현재 사용자의 습관 목록 조회
    fun loadHabits() {

        viewModelScope.launch {

            // 로딩 시작
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                // 서버에서 습관 목록 조회
                val habits = habitRepository.getHabits()

                // 조회 성공
                _uiState.update {
                    it.copy(
                        habits = habits,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                val message = if (e is HttpException) {
                    apiErrorParser.parseMessage(e)
                        ?: "습관을 불러오지 못했습니다."
                } else {
                    "습관을 불러오지 못했습니다."
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }
            }
        }
    }

    // 습관 이름 입력값 변경
    fun onTitleChange(title: String) {

        _uiState.update {
            it.copy(
                title = title,
                errorMessage = null
            )
        }
    }

    // 습관 설명 입력값 변경
    fun onDescriptionChange(description: String) {

        _uiState.update {
            it.copy(
                description = description,
                errorMessage = null
            )
        }
    }

    // 새로운 습관 생성
    fun createHabit() {

        val title = uiState.value.title.trim()
        val description = uiState.value.description.trim()

        // 입력값 검증
        val validationMessage = habitValidator.validate(
            title = title,
            description = description
        )

        if (validationMessage != null) {

            _uiState.update {
                it.copy(
                    errorMessage = validationMessage
                )
            }

            return
        }

        viewModelScope.launch {

            // 생성 요청 시작
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    isCreateSuccess = false
                )
            }

            try {

                // 서버에 습관 생성 요청
                habitRepository.createHabit(
                    title = title,
                    description = description.ifBlank { null }
                )

                // 생성 후 최신 목록 다시 조회
                val habits = habitRepository.getHabits()

                _uiState.update {
                    it.copy(
                        habits = habits,
                        title = "",
                        description = "",
                        isLoading = false,
                        isCreateSuccess = true
                    )
                }

            } catch (e: Exception) {

                val message = if (e is HttpException) {
                    apiErrorParser.parseMessage(e)
                        ?: "습관을 생성하지 못했습니다."
                } else {
                    "습관을 생성하지 못했습니다."
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }
            }
        }
    }

    // 습관 삭제
    fun deleteHabit(habitId: Long) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                // 서버에 삭제 요청
                habitRepository.deleteHabit(habitId)

                // 삭제 후 최신 목록 다시 조회
                val habits = habitRepository.getHabits()

                _uiState.update {
                    it.copy(
                        habits = habits,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                val message = if (e is HttpException) {
                    apiErrorParser.parseMessage(e)
                        ?: "습관을 삭제하지 못했습니다."
                } else {
                    "습관을 삭제하지 못했습니다."
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }
            }
        }
    }

    // 수정할 습관 선택
    fun startEditing(habitId: Long) {

        val habit = uiState.value.habits
            .find { it.id == habitId }
            ?: return

        _uiState.update {
            it.copy(
                editingHabitId = habit.id,
                title = habit.title,
                description = habit.description.orEmpty(),
                errorMessage = null
            )
        }
    }

    // 선택한 습관 수정
    fun updateHabit() {

        val habitId = uiState.value.editingHabitId
            ?: return

        val title = uiState.value.title.trim()
        val description = uiState.value.description.trim()

        // 입력값 검증
        val validationMessage = habitValidator.validate(
            title = title,
            description = description
        )

        if (validationMessage != null) {

            _uiState.update {
                it.copy(
                    errorMessage = validationMessage
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

                // 서버에 수정 요청
                habitRepository.updateHabit(
                    habitId = habitId,
                    title = title,
                    description = description.ifBlank { null }
                )

                // 수정 후 최신 목록 다시 조회
                val habits = habitRepository.getHabits()

                _uiState.update {
                    it.copy(
                        habits = habits,
                        title = "",
                        description = "",
                        editingHabitId = null,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {

                val message = if (e is HttpException) {
                    apiErrorParser.parseMessage(e)
                        ?: "습관을 수정하지 못했습니다."
                } else {
                    "습관을 수정하지 못했습니다."
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }
            }
        }
    }

    // 습관 수정 취소
    fun cancelEditing() {

        _uiState.update {
            it.copy(
                editingHabitId = null,
                title = "",
                description = "",
                errorMessage = null
            )
        }
    }
}