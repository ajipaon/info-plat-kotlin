package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class SumbarPajakResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: SumbarPajakData?
)

data class SumbarPajakData(
    @SerializedName("TNKB")
    val tnkb: String,

    @SerializedName("MEREK")
    val merek: String,

    @SerializedName("TIPE")
    val tipe: String,

    @SerializedName("TAHUN")
    val tahun: String,

    @SerializedName("WARNA")
    val warna: String,

    @SerializedName("TGL PAJAK")
    val tglPajak: String,

    @SerializedName("TGL STNK")
    val tglStnk: String,

    @SerializedName("STATUS BLOKIR")
    val statusBlokir: String,

    @SerializedName("PKB POKOK")
    val pkbPokok: String,

    @SerializedName("PKB DENDA")
    val pkbDenda: String,

    @SerializedName("OPS PKB POKOK")
    val opsPkbPokok: String,

    @SerializedName("OPS PKB DENDA")
    val opsPkbDenda: String,

    @SerializedName("SWDKLLJ POKOK")
    val swdklljPokok: String,

    @SerializedName("SWDKLLJ DENDA")
    val swdklljDenda: String,

    @SerializedName("ADM STNK")
    val admStnk: String,

    @SerializedName("ADM TNKB")
    val admTnkb: String,

    @SerializedName("JUMLAH")
    val jumlah: String,

    @SerializedName("KETERANGAN")
    val keterangan: String
)