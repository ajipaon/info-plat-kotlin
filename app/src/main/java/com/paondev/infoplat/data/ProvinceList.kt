package com.paondev.infoplat.data

data class Province(
    val name: String,
    val kode: String,
    val isActive: Boolean,
    val withNoRangka: Boolean,
    val withNik: Boolean,
    val plateCodes: List<String>
)

// Data provinsi sekarang diambil dari API melalui ProvinceResponse.toProvince()
// Fallback mapping untuk plateCodes tersedia di ProvinceApiResponse.kt
val allProvinces: List<Province> = emptyList()
