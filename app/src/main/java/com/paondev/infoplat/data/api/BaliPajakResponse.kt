package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class BaliPajakResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: BaliPajakData?,

    @SerializedName("message")
    val message: String? = null
)

data class BaliPajakData(
    @SerializedName("detail")
    val detail: BaliDetail,

    @SerializedName("pembayaran")
    val pembayaran: List<BaliPembayaran>
)

data class BaliDetail(
    @SerializedName("status")
    val status: String,

    @SerializedName("nomorPolisi")
    val nomorPolisi: String,

    @SerializedName("nama")
    val nama: String,

    @SerializedName("alamat")
    val alamat: String,

    @SerializedName("milikKeTahun")
    val milikKeTahun: String,

    @SerializedName("merk")
    val merk: String,

    @SerializedName("tipe")
    val tipe: String,

    @SerializedName("model")
    val model: String,

    @SerializedName("bahanBakar")
    val bahanBakar: String,

    @SerializedName("jenisTransaksi")
    val jenisTransaksi: String,

    @SerializedName("masaBerlaku")
    val masaBerlaku: String,

    @SerializedName("njkbDppkb")
    val njkbDppkb: String,

    @SerializedName("tarifPengenaan")
    val tarifPengenaan: String,

    @SerializedName("masaPajak")
    val masaPajak: String
)

data class BaliPembayaran(
    @SerializedName("jenis")
    val jenis: String,

    @SerializedName("pokok")
    val pokok: String,

    @SerializedName("denda")
    val denda: String,

    @SerializedName("jumlah")
    val jumlah: String
)