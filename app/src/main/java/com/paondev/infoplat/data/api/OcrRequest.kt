package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class OcrRequest(
    @SerializedName("image")
    val image: String
)