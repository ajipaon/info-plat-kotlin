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
import com.paondev.infoplat.ui.screen.VehicleHistoryScreen

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
        route = VehicleHistoryDestination.route + "?data={data}",
        arguments = listOf(
            navArgument("data") {
                type = NavType.StringType
                nullable = true
            }
        )
    ) { backStackEntry ->
        val dataParam = backStackEntry.arguments?.getString("data")
        val jabarPajakData = VehicleHistoryDestination.parseData(dataParam)
        
        if (jabarPajakData != null) {
            VehicleHistoryScreen(
                navController = navController,
                jabarPajakData = jabarPajakData
            )
        }
    }

    composable(
        route = VehicleDetailDestination.route + "?data={data}&rawData={rawData}&province={province}&provinceCode={provinceCode}&headPlat={headPlat}&bodyPlat={bodyPlat}&tailPlat={tailPlat}&noRangka={noRangka}",
        arguments = listOf(
            navArgument("data") {
                type = NavType.StringType
                nullable = true
            },
            navArgument("rawData") {
                type = NavType.StringType
                nullable = true
            },
            navArgument("province") {
                type = NavType.StringType
                nullable = true
            },
            navArgument("provinceCode") {
                type = NavType.StringType
                nullable = true
            },
            navArgument("headPlat") {
                type = NavType.StringType
                nullable = true
            },
            navArgument("bodyPlat") {
                type = NavType.StringType
                nullable = true
            },
            navArgument("tailPlat") {
                type = NavType.StringType
                nullable = true
            },
            navArgument("noRangka") {
                type = NavType.StringType
                nullable = true
            }
        )
    ) { backStackEntry ->
        // Try old route first for backward compatibility
        val dataParam = backStackEntry.arguments?.getString("data")
        val jabarPajakData = VehicleDetailDestination.parseData(dataParam)
        
        // VehicleDetailScreen will handle parsing all parameters and fetch data accordingly
        VehicleDetailScreen(
            navController = navController,
            jabarPajakData = jabarPajakData
        )
    }


}
