package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class ProvinceApiResponse(
    val success: Boolean,
    val data: List<ProvinceResponse>
)

data class ProvinceResponse(
    val kode: String,
    val name: String,
    @SerializedName("is_active")
    val isActive: Int
)

// Extension function untuk convert ke model lokal
fun ProvinceResponse.toProvince() = com.paondev.infoplat.data.Province(
    kode = kode,
    name = name,
    isActive = isActive == 1
)