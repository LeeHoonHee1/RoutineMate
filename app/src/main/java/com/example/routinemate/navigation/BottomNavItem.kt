package com.example.routinemate.navigation

data class BottomNavItem(
    val route: AppRoute,
    val label: String,
    val iconText: String
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = AppRoute.Home,
        label = "홈",
        iconText = "⌂"
    ),
    BottomNavItem(
        route = AppRoute.Habit,
        label = "습관",
        iconText = "✓"
    ),
    BottomNavItem(
        route = AppRoute.Statistics,
        label = "통계",
        iconText = "▥"
    ),
    BottomNavItem(
        route = AppRoute.Profile,
        label = "마이",
        iconText = "●"
    )
)