package com.paondev.infoplat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paondev.infoplat.data.Province
import com.paondev.infoplat.data.allProvinces
import com.paondev.infoplat.data.repository.ProvinceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProvinceViewModel(
    private val repository: ProvinceRepository
) : ViewModel() {

    private val _provinces = MutableStateFlow<List<Province>>(allProvinces)
    val provinces: StateFlow<List<Province>> = _provinces.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchProvinces()
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
}