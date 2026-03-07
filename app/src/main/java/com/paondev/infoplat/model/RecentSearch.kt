package com.paondev.infoplat.model

data class RecentSearch(
    val plate: String,
    val carModel: String,
    val status: VehicleStatus,
    val statusLabel: String,
    val data: String = "",
    val imageUrl: String? = null
)