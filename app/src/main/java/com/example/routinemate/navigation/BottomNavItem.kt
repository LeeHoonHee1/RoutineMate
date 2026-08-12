package com.example.routinemate.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: AppRoute,
    val label: String,

    // 선택되지 않았을 때 아이콘
    val unselectedIcon: ImageVector,

    // 선택되었을 때 아이콘
    val selectedIcon: ImageVector
)

val bottomNavItems = listOf(

    BottomNavItem(
        route = AppRoute.Home,
        label = "홈",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Rounded.Home
    ),

    BottomNavItem(
        route = AppRoute.Habit,
        label = "습관",
        unselectedIcon = Icons.Outlined.CheckCircle,
        selectedIcon = Icons.Rounded.CheckCircle
    ),

    BottomNavItem(
        route = AppRoute.Statistics,
        label = "통계",
        unselectedIcon = Icons.Outlined.BarChart,
        selectedIcon = Icons.Rounded.BarChart
    ),

    BottomNavItem(
        route = AppRoute.Profile,
        label = "마이",
        unselectedIcon = Icons.Outlined.Person,
        selectedIcon = Icons.Rounded.Person
    )
)