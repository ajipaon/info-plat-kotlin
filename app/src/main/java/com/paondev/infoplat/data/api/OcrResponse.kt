package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class OcrResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: String
)