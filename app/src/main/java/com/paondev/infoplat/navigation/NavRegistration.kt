package com.paondev.infoplat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.paondev.infoplat.ui.screen.PlateCheckScreen
import com.paondev.infoplat.ui.screen.SearchHistoryScreen

fun NavGraphBuilder.navRegistration(navController: NavController) {

    composable(PlateCheckDestination.route) {
        PlateCheckScreen(
            navController = navController
        )
    }

    composable(SearchHistoryDestination.route) {
        SearchHistoryScreen(
            navController = navController
        )
    }


}
