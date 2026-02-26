package com.paondev.infoplat.data

data class Province(
    val name: String,
    val kode: String,
    val isActive: Boolean
)

val allProvinces = listOf(
    Province(
        kode = "DKI",
        name = "DKI Jakarta",
        isActive = true
    ),
    Province(
        kode = "JBR",
        name = "Jawa Barat",
        isActive = true
    ),
    Province(
        kode = "JTG",
        name = "Jawa Tengah",
        isActive = true
    ),
    Province(
        kode = "JTM",
        name = "Jawa Timur",
        isActive = true
    ),
    Province(
        kode = "BAL",
        name = "Bali",
        isActive = true

    ),
    Province(
        kode = "BTN",
        name = "Banten",
        isActive = true
    ),
    Province(
        kode = "SUMUT",
        name = "Sumatera Utara",
        isActive = true
    ),
    Province(
        kode = "SUMBAR",
        name = "Sumatera Barat",
        isActive = true
    ),
    Province(
        kode = "SULSEL",
        name = "Sulawesi Selatan",
        isActive = true
    ),
    Province(
        kode = "KALBAR",
        name = "Kalimantan Barat",
        isActive = true
    )
)