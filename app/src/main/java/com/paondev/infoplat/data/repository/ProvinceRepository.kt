package com.paondev.infoplat.data.repository

import com.paondev.infoplat.data.Province
import com.paondev.infoplat.data.api.InfoPlatApi
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
}