package com.paondev.infoplat.data.repository

import androidx.compose.ui.text.toUpperCase
import com.google.gson.Gson
import com.paondev.infoplat.BuildConfig
import com.paondev.infoplat.data.Province
import com.paondev.infoplat.data.api.DiypPajakResponse
import com.paondev.infoplat.data.api.InfoPlatApi
import com.paondev.infoplat.data.api.JabarPajakRequest
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.data.api.JatimCaptchaResponse
import com.paondev.infoplat.data.api.JatimPkbRequest
import com.paondev.infoplat.data.api.JatimPkbResponse
import com.paondev.infoplat.data.api.toProvince
import com.paondev.infoplat.data.allProvinces
import com.paondev.infoplat.data.locale.InfoPlatDao
import com.paondev.infoplat.di.JatimApi
import com.paondev.infoplat.di.MainApi
import com.paondev.infoplat.model.History
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

class ProvinceRepository(
    @MainApi private val api: InfoPlatApi,
    private val dao: InfoPlatDao,
    @JatimApi private val jatimApi: InfoPlatApi
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

    suspend fun getJatimCaptcha(): Result<JatimCaptchaResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = jatimApi.getJatimCaptcha()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to generate captcha: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getJatimVehicleInfo(
        sessionId: String,
        captchaCode: String,
        nopol: String,
        norang: String
    ): Result<JatimPkbResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = JatimPkbRequest(
                    timestamp = System.currentTimeMillis(),
                    sessionId = sessionId,
                    code = captchaCode,
                    nopol = nopol.uppercase(Locale.ROOT),
                    norang = norang
                )
                val response = jatimApi.verifyJatimCaptcha(request = request)
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!
                    
                    // Save to history if successful
                    if (responseBody.status == "success" && responseBody.data != null) {
                        val gson = Gson()
                        val jsonData = gson.toJson(responseBody)
                        val history = History(
                            code = nopol.uppercase(),
                            requestDate = Date(),
                            region = "JTM",
                            data = jsonData
                        )
                        dao.insertHistory(history)
                    }
                    
                    Result.success(responseBody)
                } else {
                    Result.failure(Exception("Failed to verify captcha: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
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
                        val responseBody = response.body()!!
                        
                        // Save to history
                        val gson = Gson()
                        val jsonData = gson.toJson(responseBody)
                        val history = History(
                            code = noPolisi.uppercase(),
                            requestDate = Date(),
                            region = "JBR",
                            data = jsonData
                        )
                        dao.insertHistory(history)
                        
                        Result.success(responseBody)
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

    suspend fun getDiypVehicleInfo(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String
    ): Result<DiypPajakResponse> {
        return withContext(Dispatchers.IO) {
            try {
                if (provinceCode == "DIY") {
                    val noPolisi = "$headPlat $bodyPlat $tailPlat".trim()
                    val response = api.getDiypPajakInfo(
                        nomer = bodyPlat.uppercase(Locale.ROOT),
                        kodeBelakang = tailPlat.uppercase(Locale.ROOT)
                    )
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!
                        
                        // Save to history if data is found (not null)
                        if (responseBody.status == "success" && responseBody.data != null) {
                            val gson = Gson()
                            val jsonData = gson.toJson(responseBody)
                            val history = History(
                                code = noPolisi.uppercase(),
                                requestDate = Date(),
                                region = "DIY",
                                data = jsonData
                            )
                            dao.insertHistory(history)
                        }
                        
                        Result.success(responseBody)
                    } else {
                        Result.failure(Exception("Failed to get vehicle info: ${response.code()}"))
                    }
                } else {
                    Result.failure(Exception("Province code is not DIY"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
