package com.paondev.infoplat.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

interface Destination {
    val route: String
    val title: String
    val icon: ImageVector
}


object PlateCheckDestination: Destination {
    override val route = "PlateCheckScreen"
    override val title = "Plate Check"
    override val icon = Icons.Filled.Home
}

object SearchHistoryDestination: Destination {
    override val route = "SearchHistory"
    override val title = "Search History"
    override val icon = Icons.Filled.Home
}

object VehicleDetailDestination: Destination {
    override val route = "VehicleDetail"
    override val title = "Vehicle Detail"
    override val icon = Icons.Filled.Home
}