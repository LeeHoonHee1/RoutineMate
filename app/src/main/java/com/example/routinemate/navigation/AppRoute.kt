package com.example.routinemate.navigation

sealed interface AppRoute {

    val route: String

    data object Login : AppRoute {
        override val route = "login"
    }

    data object Register : AppRoute {
        override val route = "register"
    }

    data object Home : AppRoute {
        override val route = "home"
    }

    data object Habit : AppRoute {
        override val route = "habit"
    }

    data object Statistics : AppRoute {
        override val route = "statistics"
    }

    data object Profile : AppRoute {
        override val route = "profile"
    }

    data object Friend : AppRoute {
        override val route = "friend"
    }

    data object Challenge : AppRoute {
        override val route = "challenge"
    }

    data object ChallengeDetail : AppRoute {
        override val route =
            "challenge_detail/{challengeId}"

        fun createRoute(
            challengeId: Long
        ): String {
            return "challenge_detail/$challengeId"
        }
    }
}