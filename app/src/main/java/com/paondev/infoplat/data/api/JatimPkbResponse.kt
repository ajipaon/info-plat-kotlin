package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class JatimPkbResponse(
    @SerializedName("status")
    val status: String,
    
    @SerializedName("data")
    val data: JatimPkbData?,
    
    @SerializedName("message")
    val message: String? = null
)

data class JatimPkbData(
    @SerializedName("identitas")
    val identitas: VehicleIdentity?,
    
    @SerializedName("biaya_penul_1_tahunan")
    val biayaPenul1Tahunan: BiayaPenul1Tahunan?,
    
    @SerializedName("biaya_penul_5_tahunan")
    val biayaPenul5Tahunan: BiayaPenul5Tahunan?,
    
    @SerializedName("keterangan")
    val keterangan: Keterangan?,

    // For error responses
    @SerializedName("norang")
    val norang: List<String>? = null,
    
    @SerializedName("nopol")
    val nopol: List<String>? = null
)

data class VehicleIdentity(
    @SerializedName("nopol")
    val nopol: String,
    
    @SerializedName("warna")
    val warna: String,
    
    @SerializedName("model")
    val model: String,
    
    @SerializedName("merk")
    val merk: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("tahun_buat")
    val tahunBuat: String,
    
    @SerializedName("tgl_masa_pajak")
    val tglMasaPajak: String
)

data class BiayaPenul1Tahunan(
    @SerializedName("pkb")
    val pkb: String,
    
    @SerializedName("opsen_pkb")
    val opsenPkb: String,
    
    @SerializedName("pkb_prog")
    val pkbProg: String,
    
    @SerializedName("opsen_pkb_prog")
    val opsenPkbProg: String,
    
    @SerializedName("swdkllj")
    val swdkllj: String,
    
    @SerializedName("swdkllj_denda")
    val swdklljDenda: String,
    
    @SerializedName("parkir_langganan")
    val parkirLangganan: String,
    
    @SerializedName("sahstnk")
    val sahstnk: String,
    
    @SerializedName("total")
    val total: Int
)

data class BiayaPenul5Tahunan(
    @SerializedName("cetak_stnk")
    val cetakStnk: String,
    
    @SerializedName("cetak_tnkb")
    val cetakTnkb: String
)

data class Keterangan(
    @SerializedName("ket")
    val ket: String,
    
    @SerializedName("short")
    val short: String
)