package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class JatimPkbRequest(
    @SerializedName("app")
    val app: String = "info-bapenda",
    
    @SerializedName("timestamp")
    val timestamp: Long,
    
    @SerializedName("sessionId")
    val sessionId: String,
    
    @SerializedName("code")
    val code: String,
    
    @SerializedName("nopol")
    val nopol: String,
    
    @SerializedName("norang")
    val norang: String
)