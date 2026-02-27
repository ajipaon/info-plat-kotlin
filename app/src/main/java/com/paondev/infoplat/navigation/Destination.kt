package com.paondev.infoplat.navigation

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.Gson
import com.paondev.infoplat.data.api.JabarPajakResponse

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
    
    private const val DATA_PARAM = "data"
    
    fun createRoute(data: JabarPajakResponse): String {
        val gson = Gson()
        val json = gson.toJson(data)
        val encodedData = Uri.encode(json)
        return "$route?$DATA_PARAM=$encodedData"
    }
    
    fun parseData(encodedData: String?): JabarPajakResponse? {
        return try {
            val decodedData = Uri.decode(encodedData)
            val gson = Gson()
            gson.fromJson(decodedData, JabarPajakResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
