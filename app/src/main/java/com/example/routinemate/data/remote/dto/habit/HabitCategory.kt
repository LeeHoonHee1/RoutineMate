package com.example.routinemate.data.remote.dto.habit

import kotlinx.serialization.Serializable

@Serializable
enum class HabitCategory {
    WATER,
    BOOK,
    SLEEP,
    EXERCISE,
    STUDY
}