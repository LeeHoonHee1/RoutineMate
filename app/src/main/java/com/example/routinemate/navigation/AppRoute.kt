package com.example.routinemate.navigation

sealed interface AppRoute {

    val route: String

    data object Login : AppRoute {
        override val route: String = "login"
    }

    // 회원가입 화면
    data object Register : AppRoute {
        override val route: String = "register"
    }

    data object Home : AppRoute {
        override val route: String = "home"
    }

    data object Habit : AppRoute {
        override val route: String = "habit"
    }

    data object Statistics : AppRoute {
        override val route: String = "statistics"
    }

    data object Profile : AppRoute {
        override val route: String = "profile"
    }
}