package com.paondev.infoplat.data.repository

import com.paondev.infoplat.BuildConfig
import com.paondev.infoplat.data.Province
import com.paondev.infoplat.data.api.InfoPlatApi
import com.paondev.infoplat.data.api.JabarPajakRequest
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.data.api.toProvince
import com.paondev.infoplat.data.allProvinces
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProvinceRepository(
    private val api: InfoPlatApi
) {
    suspend fun getProvinces(): Result<List<Province>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getProvinces()
                if (response.success) {
                    val provinces = response.data.map { it.toProvince() }
                    Result.success(provinces)
                } else {
                    Result.failure(Exception("API returned success: false"))
                }
            } catch (e: Exception) {
                // Fallback ke data hardcoded jika API gagal
                Result.success(allProvinces)
            }
        }
    }

    suspend fun getVehicleInfo(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String
    ): Result<JabarPajakResponse> {
        return withContext(Dispatchers.IO) {
            try {
                if (provinceCode == "JBR") {
                    val noPolisi = "$headPlat $bodyPlat $tailPlat".trim()
                    val request = JabarPajakRequest(
                        noPolisi = noPolisi,
                        kodePlat = "1",
                        nik = null
                    )
                    val response = api.getJabarPajakInfo(
                        url = BuildConfig.JABAR_PAJAK_API_URL,
                        request = request
                    )
                    if (response.isSuccessful && response.body() != null) {
                        Result.success(response.body()!!)
                    } else {
                        Result.failure(Exception("Failed to get vehicle info: ${response.code()}"))
                    }
                } else {
                    Result.failure(Exception("Province code is not JBR"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
