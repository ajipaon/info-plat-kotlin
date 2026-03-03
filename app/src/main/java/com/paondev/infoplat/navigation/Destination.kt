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
    private const val PROVINCE_PARAM = "province"
    private const val RAW_DATA_PARAM = "rawData"
    
    // Old route for backward compatibility - accepts pre-converted JabarPajakResponse
    fun createRoute(data: JabarPajakResponse): String {
        val gson = Gson()
        val json = gson.toJson(data)
        val encodedData = Uri.encode(json)
        return "$route?$DATA_PARAM=$encodedData"
    }
    
    // New route - accepts raw province-specific response data
    fun createRoute(rawData: String, provinceCode: String): String {
        val encodedData = Uri.encode(rawData)
        return "$route?$RAW_DATA_PARAM=$encodedData&$PROVINCE_PARAM=$provinceCode"
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
    
    fun parseRawData(encodedRawData: String?, provinceCode: String?): Pair<String?, String?> {
        return try {
            val decodedRawData = Uri.decode(encodedRawData)
            Pair(decodedRawData, provinceCode)
        } catch (e: Exception) {
            Pair(null, null)
        }
    }
}
