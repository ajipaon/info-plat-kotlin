package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class BantenPajakResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: BantenPajakData?,

    @SerializedName("message")
    val message: String? = null
)

data class BantenPajakData(
    @SerializedName("kendaraan")
    val kendaraan: BantenKendaraan,

    @SerializedName("pajak")
    val pajak: BantenPajak,

    @SerializedName("diproses")
    val diproses: String
)

data class BantenKendaraan(
    @SerializedName("no_polisi")
    val noPolisi: String,

    @SerializedName("nama_pemilik")
    val namaPemilik: String,

    @SerializedName("alamat")
    val alamat: String,

    @SerializedName("merek")
    val merek: String,

    @SerializedName("tipe_model")
    val tipeModel: String,

    @SerializedName("jenis")
    val jenis: String,

    @SerializedName("no_rangka_mesin")
    val noRangkaMesin: String,

    @SerializedName("tahun")
    val tahun: String,

    @SerializedName("cc")
    val cc: String,

    @SerializedName("bbm")
    val bbm: String,

    @SerializedName("warna")
    val warna: String,

    @SerializedName("warna_plat")
    val warnaPlat: String
)

data class BantenPajak(
    @SerializedName("tgl_akhir_pkb_lalu")
    val tglAkhirPkbLalu: String,

    @SerializedName("tgl_akhir_stnk_lalu")
    val tglAkhirStnkLalu: String,

    @SerializedName("tgl_daftar")
    val tglDaftar: String,

    @SerializedName("keterangan")
    val keterangan: String,

    @SerializedName("kab_kota")
    val kabKota: String,

    @SerializedName("tgl_akhir_pkb_yad")
    val tglAkhirPkbYad: String,

    @SerializedName("pkb_pokok")
    val pkbPokok: String,

    @SerializedName("pkb_denda")
    val pkbDenda: String,

    @SerializedName("opsen_pkb_pokok")
    val opsenPkbPokok: String,

    @SerializedName("opsen_pkb_denda")
    val opsenPkbDenda: String,

    @SerializedName("swdkllj_pokok")
    val swdklljPokok: String,

    @SerializedName("swdkllj_denda")
    val swdklljDenda: String,

    @SerializedName("stnk")
    val stnk: String,

    @SerializedName("tnkb")
    val tnkb: String,

    @SerializedName("pnbp_nopil")
    val pnbpNopil: String,

    @SerializedName("jumlah")
    val jumlah: String,

    @SerializedName("pkb_pokok_int")
    val pkbPokokInt: Int,

    @SerializedName("pkb_denda_int")
    val pkbDendaInt: Int,

    @SerializedName("opsen_pkb_pokok_int")
    val opsenPkbPokokInt: Int,

    @SerializedName("opsen_pkb_denda_int")
    val opsenPkbDendaInt: Int,

    @SerializedName("swdkllj_pokok_int")
    val swdklljPokokInt: Int,

    @SerializedName("swdkllj_denda_int")
    val swdklljDendaInt: Int,

    @SerializedName("stnk_int")
    val stnkInt: Int,

    @SerializedName("tnkb_int")
    val tnkbInt: Int,

    @SerializedName("pnbp_nopil_int")
    val pnbpNopilInt: Int,

    @SerializedName("jumlah_int")
    val jumlahInt: Int
)