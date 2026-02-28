package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class DiypPajakResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("nopol")
    val nopol: String,

    @SerializedName("data")
    val data: DiypPajakData?
)

data class DiypPajakData(
    @SerializedName("nopolisi")
    val nopolisi: String,

    @SerializedName("nmmerekkb")
    val nmmerekkb: String,

    @SerializedName("nmmodelkb")
    val nmmodelkb: String,

    @SerializedName("tahunkb")
    val tahunkb: String,

    @SerializedName("swdkllj")
    val swdkllj: String,

    @SerializedName("pkb")
    val pkb: String,

    @SerializedName("pkbswd")
    val pkbswd: String,

    @SerializedName("tgakhirpkb")
    val tgakhirpkb: String
)