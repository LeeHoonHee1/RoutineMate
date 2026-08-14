package com.example.routinemate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.routinemate.presentation.auth.AuthViewModel
import com.example.routinemate.presentation.auth.LoginScreen
import com.example.routinemate.presentation.auth.RegisterScreen
import com.example.routinemate.presentation.friend.FriendScreen
import com.example.routinemate.presentation.habit.HabitScreen
import com.example.routinemate.presentation.home.HomeScreen
import com.example.routinemate.presentation.profile.ProfileScreen
import com.example.routinemate.presentation.statistics.StatisticsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(route = AppRoute.Login.route) {

            val viewModel: AuthViewModel = hiltViewModel()

            LoginScreen(
                viewModel = viewModel,

                // 로그인 성공 시 Home으로 이동
                onLoginSuccess = {
                    navController.navigate(
                        AppRoute.Home.route
                    ) {
                        popUpTo(
                            AppRoute.Login.route
                        ) {
                            inclusive = true
                        }
                    }
                },

                // 회원가입 화면으로 이동
                onRegisterClick = {
                    navController.navigate(
                        AppRoute.Register.route
                    )
                }
            )
        }

        composable(route = AppRoute.Register.route) {

            val viewModel: AuthViewModel = hiltViewModel()

            RegisterScreen(
                viewModel = viewModel,

                // 회원가입 성공 후 로그인으로 복귀
                onSignupSuccess = {
                    navController.popBackStack()
                }
            )
        }

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

            val viewModel: AuthViewModel = hiltViewModel()

            ProfileScreen(
                viewModel = viewModel,

                // 친구 화면으로 이동
                onFriendClick = {
                    navController.navigate(
                        AppRoute.Friend.route
                    )
                }
            )
        }

        // 친구 화면
        composable(route = AppRoute.Friend.route) {
            FriendScreen()
        }
    }
}