package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class LampungPajakResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: LampungPajakData?
)

data class LampungPajakData(
    @SerializedName("kendaraan_ke")
    val kendaraanKe: Int,

    @SerializedName("kendaraan_dari")
    val kendaraanDari: Int,

    @SerializedName("jenis_kendaraan")
    val jenisKendaraan: String,

    @SerializedName("merk")
    val merek: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("tahun")
    val tahun: Int,

    @SerializedName("isi_silinder")
    val isiSilinder: Int,

    @SerializedName("warna_tnkb")
    val warnaTnkb: String,

    @SerializedName("jatuh_tempo_pkb")
    val jatuhTempoPkb: String,

    @SerializedName("tgl_bayar_terakhir")
    val tglBayarTerakhir: String,

    @SerializedName("stnk_berlaku_sampai")
    val stnkBerlakuSampai: String,

    @SerializedName("njkb")
    val njkb: MonetaryValue,

    @SerializedName("bobot")
    val bobot: Int,

    @SerializedName("dasar_pkb")
    val dasarPkb: MonetaryValue,

    @SerializedName("nilai_pajak_pertahun")
    val nilaiPajakPertahun: MonetaryValue,

    @SerializedName("keterlambatan")
    val keterlambatan: String,

    @SerializedName("pokok_pkb")
    val pokokPkb: MonetaryValue,

    @SerializedName("denda_pkb")
    val dendaPkb: MonetaryValue,

    @SerializedName("pokok_opsen_pkb")
    val pokokOpsenPkb: MonetaryValue,

    @SerializedName("denda_opsen_pkb")
    val dendaOpsenPkb: MonetaryValue,

    @SerializedName("pokok_swdkllj")
    val pokokSwdkllj: MonetaryValue,

    @SerializedName("denda_swdkllj")
    val dendaSwdkllj: MonetaryValue,

    @SerializedName("jumlah_bayar")
    val jumlahBayar: MonetaryValue,

    @SerializedName("catatan")
    val catatan: String?
)

data class MonetaryValue(
    @SerializedName("raw")
    val raw: String,

    @SerializedName("angka")
    val angka: Int
)