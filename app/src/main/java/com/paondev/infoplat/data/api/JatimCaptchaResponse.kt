package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class JatimCaptchaResponse(
    @SerializedName("sessionId")
    val sessionId: String,
    
    @SerializedName("image")
    val image: String,
    
    @SerializedName("expiresIn")
    val expiresIn: Int
)