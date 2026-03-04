package com.paondev.infoplat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paondev.infoplat.data.Province
import com.paondev.infoplat.data.allProvinces
import com.paondev.infoplat.data.api.BantenPajakResponse
import com.paondev.infoplat.data.api.BaliPajakResponse
import com.paondev.infoplat.data.api.BangkaBelitungPajakResponse
import com.paondev.infoplat.data.api.LampungPajakResponse
import com.paondev.infoplat.data.api.RiauPajakResponse
import com.paondev.infoplat.data.api.SumbarPajakResponse
import com.paondev.infoplat.data.api.OcrResponse
import com.paondev.infoplat.data.api.DiypPajakResponse
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.data.api.JatimCaptchaResponse
import com.paondev.infoplat.data.api.JatimPkbResponse
import com.paondev.infoplat.data.repository.ProvinceRepository
import com.paondev.infoplat.config.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvinceViewModel @Inject constructor(
    private val repository: ProvinceRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _provinces = MutableStateFlow<List<Province>>(emptyList())
    val provinces: StateFlow<List<Province>> = _provinces.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedProvince = MutableStateFlow<Province?>(null)
    val selectedProvince: StateFlow<Province?> = _selectedProvince.asStateFlow()

    // JTM Captcha States
    private val _isCaptchaLoading = MutableStateFlow(false)
    val isCaptchaLoading: StateFlow<Boolean> = _isCaptchaLoading.asStateFlow()

    private val _captchaData = MutableStateFlow<JatimCaptchaResponse?>(null)
    val captchaData: StateFlow<JatimCaptchaResponse?> = _captchaData.asStateFlow()

    private val _captchaError = MutableStateFlow<String?>(null)
    val captchaError: StateFlow<String?> = _captchaError.asStateFlow()

    init {
        fetchProvinces()
        loadSelectedProvince()
    }

    private fun loadSelectedProvince() {
        viewModelScope.launch {
            dataStoreManager.getSelectedProvince().collect { provinceCode ->
                if (provinceCode != null && _provinces.value.isNotEmpty()) {
                    val province = _provinces.value.find { it.kode == provinceCode }
                    if (province != null && province.isActive) {
                        _selectedProvince.value = province
                    }
                }
            }
        }
    }

    fun selectProvince(province: Province) {
        _selectedProvince.value = province
        // Reset captcha state when province changes
        _captchaData.value = null
        _captchaError.value = null
        // Save selected province to DataStore
        viewModelScope.launch {
            dataStoreManager.saveSelectedProvince(province.kode)
        }
    }

    fun fetchProvinces() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getProvinces()
                .onSuccess { provinceList ->
                    _provinces.value = provinceList
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    suspend fun getVehicleInfo(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String
    ): Result<JabarPajakResponse> {
        return repository.getVehicleInfo(
            provinceCode = provinceCode,
            headPlat = headPlat,
            bodyPlat = bodyPlat,
            tailPlat = tailPlat
        )
    }

    suspend fun getJatimCaptcha(): Result<JatimCaptchaResponse> {
        _isCaptchaLoading.value = true
        _captchaError.value = null

        val result = repository.getJatimCaptcha()

        _isCaptchaLoading.value = false

        result.onSuccess { captchaResponse ->
            _captchaData.value = captchaResponse
        }.onFailure { exception ->
            _captchaError.value = exception.message
        }

        return result
    }

    suspend fun getJatimVehicleInfo(
        sessionId: String,
        captchaCode: String,
        nopol: String,
        norang: String
    ): Result<JatimPkbResponse> {
        _captchaError.value = null

        val result = repository.getJatimVehicleInfo(
            sessionId = sessionId,
            captchaCode = captchaCode,
            nopol = nopol,
            norang = norang
        )

        result.onFailure { exception ->
            _captchaError.value = exception.message
        }

        return result
    }

    suspend fun getDiypVehicleInfo(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String
    ): Result<DiypPajakResponse> {
        return repository.getDiypVehicleInfo(
            provinceCode = provinceCode,
            headPlat = headPlat,
            bodyPlat = bodyPlat,
            tailPlat = tailPlat
        )
    }

    suspend fun getBantenVehicleInfo(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String
    ): Result<BantenPajakResponse> {
        return repository.getBantenVehicleInfo(
            provinceCode = provinceCode,
            headPlat = headPlat,
            bodyPlat = bodyPlat,
            tailPlat = tailPlat
        )
    }

    suspend fun getBaliVehicleInfo(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String,
        noRangka: String
    ): Result<BaliPajakResponse> {
        return repository.getBaliVehicleInfo(
            provinceCode = provinceCode,
            headPlat = headPlat,
            bodyPlat = bodyPlat,
            tailPlat = tailPlat,
            noRangka = noRangka
        )
    }

    suspend fun getBangkaBelitungVehicleInfo(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String
    ): Result<BangkaBelitungPajakResponse> {
        return repository.getBangkaBelitungVehicleInfo(
            provinceCode = provinceCode,
            headPlat = headPlat,
            bodyPlat = bodyPlat,
            tailPlat = tailPlat
        )
    }

    suspend fun getLampungVehicleInfo(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String,
        noRangka: String
    ): Result<LampungPajakResponse> {
        return repository.getLampungVehicleInfo(
            provinceCode = provinceCode,
            headPlat = headPlat,
            bodyPlat = bodyPlat,
            tailPlat = tailPlat,
            noRangka = noRangka
        )
    }

    suspend fun getRiauVehicleInfo(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String,
        noRangka: String
    ): Result<RiauPajakResponse> {
        return repository.getRiauVehicleInfo(
            provinceCode = provinceCode,
            headPlat = headPlat,
            bodyPlat = bodyPlat,
            tailPlat = tailPlat,
            noRangka = noRangka
        )
    }

    suspend fun getSumbarVehicleInfo(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String,
        noRangka: String
    ): Result<SumbarPajakResponse> {
        return repository.getSumbarVehicleInfo(
            provinceCode = provinceCode,
            headPlat = headPlat,
            bodyPlat = bodyPlat,
            tailPlat = tailPlat,
            noRangka = noRangka
        )
    }

    suspend fun solveOcr(image: String): Result<OcrResponse> {
        return repository.solveOcr(image)
    }

    fun clearCaptchaData() {
        _captchaData.value = null
    }

    fun setCaptchaData(captchaResponse: com.paondev.infoplat.data.api.JatimCaptchaResponse) {
        _captchaData.value = captchaResponse
    }

    fun clearCaptchaError() {
        _captchaError.value = null
    }
}
