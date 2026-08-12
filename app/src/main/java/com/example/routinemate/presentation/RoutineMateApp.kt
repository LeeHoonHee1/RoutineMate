package com.example.routinemate.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.routinemate.navigation.AppNavHost
import com.example.routinemate.navigation.AppRoute
import com.example.routinemate.navigation.bottomNavItems
import com.example.routinemate.presentation.app.AppViewModel

@Composable
fun RoutineMateApp() {

    val navController = rememberNavController()

    // 앱 전체 상태 ViewModel
    val appViewModel: AppViewModel = hiltViewModel()

    val isSessionExpired by
    appViewModel.isSessionExpired.collectAsState()

    val isAuthChecked by
    appViewModel.isAuthChecked.collectAsState()

    val isLoggedIn by
    appViewModel.isLoggedIn.collectAsState()

    // DataStore 확인이 끝나기 전에는 Navigation을 만들지 않음
    if (!isAuthChecked) {
        return
    }

    // 저장된 토큰 여부에 따라 시작 화면 결정
    val startDestination =
        if (isLoggedIn) {
            AppRoute.Home.route
        } else {
            AppRoute.Login.route
        }

    // 현재 Navigation 경로
    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry?.destination?.route

    // 세션 만료 시 로그인 화면으로 이동
    LaunchedEffect(isSessionExpired) {

        if (isSessionExpired) {

            navController.navigate(
                AppRoute.Login.route
            ) {

                popUpTo(
                    navController.graph
                        .findStartDestination()
                        .id
                ) {
                    inclusive = true
                }

                launchSingleTop = true
            }
        }
    }

    // 로그인/회원가입에서는 BottomBar 숨김
    val showBottomBar =
        currentRoute != null &&
                currentRoute != AppRoute.Login.route &&
                currentRoute != AppRoute.Register.route

    Scaffold(
        containerColor =
            MaterialTheme.colorScheme.background,

        bottomBar = {

            if (showBottomBar) {

                NavigationBar(
                    containerColor =
                        MaterialTheme.colorScheme.surface,

                    contentColor =
                        MaterialTheme.colorScheme.onSurface,

                    tonalElevation =
                        NavigationBarDefaults.Elevation
                ) {

                    bottomNavItems.forEach { item ->

                        val isSelected =
                            currentRoute == item.route.route

                        NavigationBarItem(
                            selected = isSelected,

                            onClick = {

                                navController.navigate(
                                    item.route.route
                                ) {

                                    popUpTo(
                                        navController.graph
                                            .findStartDestination()
                                            .id
                                    ) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },

                            // 실제 Material Icon 사용
                            icon = {

                                Icon(
                                    imageVector =
                                        if (isSelected) {
                                            item.selectedIcon
                                        } else {
                                            item.unselectedIcon
                                        },

                                    contentDescription =
                                        item.label
                                )
                            },

                            label = {

                                Text(
                                    text = item.label,
                                    style =
                                        MaterialTheme.typography.labelMedium
                                )
                            },

                            alwaysShowLabel = true,

                            colors =
                                NavigationBarItemDefaults.colors(

                                    // 선택된 탭
                                    selectedIconColor =
                                        MaterialTheme.colorScheme.primary,

                                    selectedTextColor =
                                        MaterialTheme.colorScheme.primary,

                                    indicatorColor =
                                        MaterialTheme.colorScheme
                                            .primaryContainer,

                                    // 선택되지 않은 탭
                                    unselectedIconColor =
                                        MaterialTheme.colorScheme
                                            .onSurfaceVariant,

                                    unselectedTextColor =
                                        MaterialTheme.colorScheme
                                            .onSurfaceVariant
                                )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        AppNavHost(
            navController = navController,

            // 로그인 여부에 따라 시작 화면 전달
            startDestination = startDestination,

            modifier = Modifier.padding(
                innerPadding
            )
        )
    }
}