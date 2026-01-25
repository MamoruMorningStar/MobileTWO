package com.example.mobiletwo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mobiletwo.ui.screens.HistoryScreen
import com.example.mobiletwo.ui.screens.HomeScreen
import com.example.mobiletwo.ui.screens.InputScreen
import com.example.mobiletwo.ui.screens.ResultScreen
import com.example.mobiletwo.viewmodel.SplitViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: SplitViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onStartClick = {
                    navController.navigate(Screen.Input.route)
                },
                onHistoryClick = {
                    navController.navigate(Screen.History.route)
                }
            )
        }
        
        composable(Screen.Input.route) {
            InputScreen(
                viewModel = viewModel,
                onCalculateClick = { calcId ->
                    navController.navigate(Screen.Result.createRoute(calcId))
                }
            )
        }
        
        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("calcId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val calcId = backStackEntry.arguments?.getString("calcId") ?: ""
            val calculation = viewModel.getCalculationById(calcId)
            
            ResultScreen(
                calculation = calculation,
                onBackToEdit = {
                    navController.popBackStack()
                },
                onNewCalculation = {
                    viewModel.reset()
                    navController.navigate(Screen.Input.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }
        
        composable(Screen.History.route) {
            HistoryScreen(
                viewModel = viewModel,
                onCalculationClick = { calcId ->
                    navController.navigate(Screen.Result.createRoute(calcId))
                }
            )
        }
    }
}
