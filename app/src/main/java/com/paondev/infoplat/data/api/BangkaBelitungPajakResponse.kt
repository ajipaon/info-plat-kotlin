package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class BangkaBelitungPajakResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: BangkaBelitungPajakData?
)

data class BangkaBelitungPajakData(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("nopol")
    val nopol: String,

    @SerializedName("nama")
    val nama: String,

    @SerializedName("merek")
    val merek: String,

    @SerializedName("model")
    val model: String,

    @SerializedName("warna")
    val warna: String,

    @SerializedName("warna_plat")
    val warnaPlat: String,

    @SerializedName("th_buatan")
    val thBuatan: String,

    @SerializedName("jumlah_cc")
    val jumlahCc: String,

    @SerializedName("bbm")
    val bbm: String,

    @SerializedName("no_rangka")
    val noRangka: String,

    @SerializedName("no_mesin")
    val noMesin: String,

    @SerializedName("akhir_pkb")
    val akhirPkb: String,

    @SerializedName("akhir_stnkb")
    val akhirStnkb: String,

    @SerializedName("bbn_pok")
    val bbnPok: String,

    @SerializedName("pkb_lama")
    val pkbLama: String,

    @SerializedName("denda_pkb_lama")
    val dendaPkbLama: String,

    @SerializedName("bea_pkb")
    val beaPkb: String,

    @SerializedName("denda_bea_pkb")
    val dendaBeaPkb: String,

    @SerializedName("opsen_pkb")
    val opsenPkb: String,

    @SerializedName("denda_opsen_pkb")
    val dendaOpsenPkb: String,

    @SerializedName("pokok_sw")
    val pokokSw: String,

    @SerializedName("total_tgk_sw")
    val totalTgkSw: String,

    @SerializedName("total_denda_sw")
    val totalDendaSw: String,

    @SerializedName("pnbp_bpkb")
    val pnbpBpkb: String,

    @SerializedName("pnbp_stnk")
    val pnbpStnk: String,

    @SerializedName("pnbp_plat")
    val pnbpPlat: String,

    @SerializedName("jumlah_total")
    val jumlahTotal: String
)