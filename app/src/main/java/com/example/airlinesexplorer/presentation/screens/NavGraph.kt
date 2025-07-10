package com.example.airlinesexplorer.presentation.screens

// NavGraph.kt

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.airlinesexplorer.presentation.screens.airlines.AirlinesScreen
import com.example.airlinesexplorer.presentation.screens.details.AirlineDetailsScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "airlines",
        modifier = modifier
    ) {
        composable("airlines") {
            AirlinesScreen(
                onAirlineClick = { airlineId ->
                    navController.navigate("airline_details/$airlineId")
                }
            )
        }

        composable("airline_details/{airlineId}") { backStackEntry ->
            val airlineId = backStackEntry.arguments?.getString("airlineId") ?: ""
            AirlineDetailsScreen(
                airlineId = airlineId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}