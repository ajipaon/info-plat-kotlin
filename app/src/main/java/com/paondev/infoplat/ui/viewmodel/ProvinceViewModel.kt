package com.paondev.infoplat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paondev.infoplat.data.Province
import com.paondev.infoplat.data.allProvinces
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.data.api.JatimCaptchaResponse
import com.paondev.infoplat.data.api.JatimPkbResponse
import com.paondev.infoplat.data.repository.ProvinceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvinceViewModel @Inject constructor(
    private val repository: ProvinceRepository
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
    }

    fun selectProvince(province: Province) {
        _selectedProvince.value = province
        // Reset captcha state when province changes
        _captchaData.value = null
        _captchaError.value = null
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

    fun clearCaptchaError() {
        _captchaError.value = null
    }
}
