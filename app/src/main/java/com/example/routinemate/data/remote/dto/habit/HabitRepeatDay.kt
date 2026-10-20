package com.example.routinemate.data.remote.dto.habit

import kotlinx.serialization.Serializable

@Serializable
enum class HabitRepeatDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}