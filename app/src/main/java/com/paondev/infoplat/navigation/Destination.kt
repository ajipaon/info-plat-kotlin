package com.paondev.infoplat.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

interface Destination {
    val route: String
    val title: String
    val icon: ImageVector
}


object CatalogDestination: Destination {
    override val route = "catalog"
    override val title = "catalog"
    override val icon = Icons.Filled.Home
}
