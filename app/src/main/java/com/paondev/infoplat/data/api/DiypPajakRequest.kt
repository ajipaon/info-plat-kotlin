package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class DiypPajakRequest(
    @field:SerializedName("nomer")
    val nomer: String,

    @field:SerializedName("kode_belakang")
    val kodeBelakang: String
)