package com.example.mobiletwo.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Input : Screen("input")
    object Result : Screen("result/{calcId}") {
        fun createRoute(calcId: String) = "result/$calcId"
    }
    object History : Screen("history")
}
