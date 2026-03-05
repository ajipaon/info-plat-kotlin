package com.paondev.infoplat.data

import kotlin.collections.emptyList

data class Province(
    val name: String,
    val kode: String,
    val isActive: Boolean,
    val withNoRangka: Boolean,
    val withNik: Boolean
)

val allProvinces : List<Province> = emptyList()
