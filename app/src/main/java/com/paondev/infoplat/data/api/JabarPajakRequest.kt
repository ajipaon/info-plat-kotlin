package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class JabarPajakRequest(
    @SerializedName("noPolisi")
    val noPolisi: String,
    @SerializedName("kodePlat")
    val kodePlat: String = "1",
    @SerializedName("nik")
    val nik: String? = null
)