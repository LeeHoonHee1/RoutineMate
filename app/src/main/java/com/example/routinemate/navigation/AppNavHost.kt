package com.example.routinemate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.routinemate.presentation.auth.AuthViewModel
import com.example.routinemate.presentation.auth.LoginScreen
import com.example.routinemate.presentation.auth.RegisterScreen
import com.example.routinemate.presentation.challenge.ChallengeScreen
import com.example.routinemate.presentation.challenge.detail.ChallengeDetailScreen
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

        // 로그인
        composable(
            route = AppRoute.Login.route
        ) {

            val viewModel: AuthViewModel =
                hiltViewModel()

            LoginScreen(
                viewModel = viewModel,

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

                onRegisterClick = {

                    navController.navigate(
                        AppRoute.Register.route
                    )
                }
            )
        }

        // 회원가입
        composable(
            route = AppRoute.Register.route
        ) {

            val viewModel: AuthViewModel =
                hiltViewModel()

            RegisterScreen(
                viewModel = viewModel,

                onSignupSuccess = {
                    navController.popBackStack()
                }
            )
        }

        // 홈
        composable(
            route = AppRoute.Home.route
        ) {

            HomeScreen(
                onChallengeClick = {

                    navController.navigate(
                        AppRoute.Challenge.route
                    )
                }
            )
        }

        // 습관
        composable(
            route = AppRoute.Habit.route
        ) {

            HabitScreen()
        }

        // 통계
        composable(
            route = AppRoute.Statistics.route
        ) {

            StatisticsScreen()
        }

        // 프로필
        composable(
            route = AppRoute.Profile.route
        ) {

            val authViewModel: AuthViewModel =
                hiltViewModel()

            ProfileScreen(

                // 습관 화면
                onHabitClick = {

                    navController.navigate(
                        AppRoute.Habit.route
                    )
                },

                // 친구 화면
                onFriendClick = {

                    navController.navigate(
                        AppRoute.Friend.route
                    )
                },

                // 챌린지 화면
                onChallengeClick = {

                    navController.navigate(
                        AppRoute.Challenge.route
                    )
                },

                // 로그아웃
                onLogout = {

                    authViewModel.logout()

                    navController.navigate(
                        AppRoute.Login.route
                    ) {

                        popUpTo(
                            AppRoute.Home.route
                        ) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // 친구
        composable(
            route = AppRoute.Friend.route
        ) {

            FriendScreen()
        }

        // 챌린지
        composable(
            route = AppRoute.Challenge.route
        ) {

            ChallengeScreen(
                onChallengeClick = { challengeId ->

                    navController.navigate(
                        AppRoute.ChallengeDetail.createRoute(
                            challengeId = challengeId
                        )
                    )
                }
            )
        }

        // 챌린지 상세
        composable(
            route = AppRoute.ChallengeDetail.route,
            arguments = listOf(
                navArgument("challengeId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->

            val challengeId =
                backStackEntry.arguments
                    ?.getLong("challengeId")
                    ?: return@composable

            ChallengeDetailScreen(
                challengeId = challengeId
            )
        }
    }
}