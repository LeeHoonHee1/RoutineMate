package com.example.routinemate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.routinemate.presentation.habit.HabitScreen
import com.example.routinemate.presentation.home.HomeScreen
import com.example.routinemate.presentation.profile.ProfileScreen
import com.example.routinemate.presentation.statistics.StatisticsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Home.route,
        modifier = modifier
    ) {
        composable(route = AppRoute.Home.route) {
            HomeScreen()
        }

        composable(route = AppRoute.Habit.route) {
            HabitScreen()
        }

        composable(route = AppRoute.Statistics.route) {
            StatisticsScreen()
        }

        composable(route = AppRoute.Profile.route) {
            ProfileScreen()
        }
    }
}