package com.paondev.infoplat.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.paondev.infoplat.ui.screen.PlateCheckScreen
import com.paondev.infoplat.ui.screen.SearchHistoryScreen
import com.paondev.infoplat.ui.screen.VehicleDetailScreen

fun NavGraphBuilder.navRegistration(navController: NavController) {

    composable(PlateCheckDestination.route) {
        PlateCheckScreen(
            navController = navController,
            padding = PaddingValues(0.dp, 0.dp, 0.dp, 0.dp)
        )
    }

    composable(SearchHistoryDestination.route) {
        SearchHistoryScreen(
            navController = navController
        )
    }

    composable(
        route = VehicleDetailDestination.route + "?data={data}",
        arguments = listOf(
            navArgument("data") {
                type = NavType.StringType
                nullable = true
            }
        )
    ) { backStackEntry ->
        val dataParam = backStackEntry.arguments?.getString("data")
        val jabarPajakData = VehicleDetailDestination.parseData(dataParam)
        VehicleDetailScreen(
            navController = navController,
            jabarPajakData = jabarPajakData
        )
    }


}
