package com.paondev.infoplat.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
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

    composable(VehicleDetailDestination.route) {
        VehicleDetailScreen(
            navController = navController
        )
    }


}
