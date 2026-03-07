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
    
    // New route - accepts plate parameters to fetch data in VehicleDetailViewModel
    fun createRoute(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String,
        noRangka: String = "",
        noNik: String = ""
    ): String {
        return "$route" +
                "?provinceCode=$provinceCode" +
                "&headPlat=$headPlat" +
                "&bodyPlat=$bodyPlat" +
                "&tailPlat=$tailPlat" +
                "&noRangka=$noRangka" +
                "&noNik=$noNik"
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
    
    // Parse plate parameters for new flow
    fun parsePlateParameters(
        provinceCode: String?,
        headPlat: String?,
        bodyPlat: String?,
        tailPlat: String?,
        noRangka: String?,
        noNik: String?
    ): PlateParameters? {
        return if (provinceCode != null && headPlat != null && bodyPlat != null && tailPlat != null) {
            PlateParameters(
                provinceCode = provinceCode,
                headPlat = headPlat,
                bodyPlat = bodyPlat,
                tailPlat = tailPlat,
                noRangka = noRangka ?: "",
                noNik = noNik ?: ""
            )
        } else {
            null
        }
    }
}

data class PlateParameters(
    val provinceCode: String,
    val headPlat: String,
    val bodyPlat: String,
    val tailPlat: String,
    val noRangka: String,
    val noNik: String
)

object VehicleHistoryDestination: Destination {
    override val route = "VehicleHistory"
    override val title = "Vehicle History"
    override val icon = Icons.Filled.Home
    
    private const val DATA_PARAM = "data"
    
    // Create route with pre-converted JabarPajakResponse data
    fun createRoute(data: JabarPajakResponse): String {
        val gson = Gson()
        val json = gson.toJson(data)
        val encodedData = Uri.encode(json)
        return "$route?$DATA_PARAM=$encodedData"
    }
    
    // Parse data from route parameters
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
