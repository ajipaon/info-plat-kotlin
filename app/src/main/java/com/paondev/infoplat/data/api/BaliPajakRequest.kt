package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class BaliPajakRequest(
    @SerializedName("kode")
    val kode: String,

    @SerializedName("head_plat")
    val headPlat: String,

    @SerializedName("body_plat")
    val bodyPlat: String,

    @SerializedName("tail_plat")
    val tailPlat: String,

    @SerializedName("no_rangka")
    val noRangka: String? = null,

    @SerializedName("no_nik")
    val noNik: String? = null
)
