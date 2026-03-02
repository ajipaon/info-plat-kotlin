package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class RiauPajakResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val data: RiauPajakData?
)

data class RiauPajakData(
    @SerializedName("Nopol")
    val nopol: String,

    @SerializedName("Nama_Pemilik")
    val namaPemilik: String,

    @SerializedName("Alamat")
    val alamat: String,

    @SerializedName("Merk_kendaraan")
    val merekKendaraan: String,

    @SerializedName("Type_kendaraan")
    val typeKendaraan: String,

    @SerializedName("Golongan_Kendaraan")
    val golonganKendaraan: String,

    @SerializedName("Tahun_Pembuatan")
    val tahunPembuatan: String,

    @SerializedName("Warna_kendaraan")
    val warnaKendaraan: String,

    @SerializedName("Nama_Model")
    val namaModel: String,

    @SerializedName("Nama_Jenis")
    val namaJenis: String,

    @SerializedName("Warna_TNKB")
    val warnaTnkb: String,

    @SerializedName("Lokasi_UPT_Pembayaran_Sebelumya")
    val lokasiUptPembayaranSebelumnya: String,

    @SerializedName("Pokok_BBNKB_Sebelumnya")
    val pokokBbnkbSebelumnya: String,

    @SerializedName("Pokok_BBNKB_OPSEN_Sebelumnya")
    val pokokBbnkbOpsenSebelumnya: String,

    @SerializedName("Denda_BBNKB_Sebelumnya")
    val dendaBbnkbSebelumnya: String,

    @SerializedName("Denda_BBNKB_OPSEN_Sebelumnya")
    val dendaBbnkbOpsenSebelumnya: String,

    @SerializedName("Pokok_PKB_Sebelumnya")
    val pokokPkbSebelumnya: String,

    @SerializedName("Pokok_PKB_OPSEN_Sebelumnya")
    val pokokPkbOpsenSebelumnya: String,

    @SerializedName("Denda_PKB_Sebelumnya")
    val dendaPkbSebelumnya: String,

    @SerializedName("Denda_PKB_OPSEN_Sebelumnya")
    val dendaPkbOpsenSebelumnya: String,

    @SerializedName("Pokok_SWDK_Sebelumnya")
    val pokokSwdkSebelumnya: String,

    @SerializedName("Denda_SWDK_Sebelumnya")
    val dendaSwdkSebelumnya: String,

    @SerializedName("STNK_Sebelumnya")
    val stnkSebelumnya: String,

    @SerializedName("TNKB_Sebelumnya")
    val tnkbSebelumnya: String,

    @SerializedName("Total_Sebelumnya")
    val totalSebelumnya: String,

    @SerializedName("Tanggal_Pembayaran_Sebelumnya")
    val tanggalPembayaranSebelumnya: String,

    @SerializedName("KodeKabKota")
    val kodeKabKota: String,

    @SerializedName("Tanggal_Jatuh_Tempo")
    val tanggalJatuhTempo: String,

    @SerializedName("Tanggal_STNK")
    val tanggalStnk: String,

    @SerializedName("NJKB")
    val njkb: String,

    @SerializedName("Bobot")
    val bobot: String,

    @SerializedName("Dasar_PKB")
    val dasarPkb: String,

    @SerializedName("Lama_Tunggakan")
    val lamaTunggakan: String,

    @SerializedName("Tarif_BBNKB")
    val tarifBbnkb: String,

    @SerializedName("Tarif_BBNKB_Opsen")
    val tarifBbnkbOpsen: String,

    @SerializedName("Tarif_PKB")
    val tarifPkb: String,

    @SerializedName("Tarif_PKB_Opsen")
    val tarifPkbOpsen: String,

    @SerializedName("Tarif_SWDK")
    val tarifSwdk: String,

    @SerializedName("Tarif_Denda_SWDK")
    val tarifDendaSwdk: String,

    @SerializedName("Tarif_STNK")
    val tarifStnk: String,

    @SerializedName("Tarif_TNKB")
    val tarifTnkb: String,

    @SerializedName("Pokok_PKB_T0")
    val pokokPkbT0: String,

    @SerializedName("Pokok_PKB_T1")
    val pokokPkbT1: String,

    @SerializedName("Pokok_PKB_T2")
    val pokokPkbT2: String,

    @SerializedName("Pokok_PKB_T3")
    val pokokPkbT3: String,

    @SerializedName("Pokok_PKB_T4")
    val pokokPkbT4: String,

    @SerializedName("Pokok_PKB_T5")
    val pokokPkbT5: String,

    @SerializedName("Total_Pokok_PKB")
    val totalPokokPkb: String,

    @SerializedName("Pokok_PKB_Opsen_T0")
    val pokokPkbOpsenT0: String,

    @SerializedName("Pokok_PKB_Opsen_T1")
    val pokokPkbOpsenT1: String,

    @SerializedName("Pokok_PKB_Opsen_T2")
    val pokokPkbOpsenT2: String,

    @SerializedName("Pokok_PKB_Opsen_T3")
    val pokokPkbOpsenT3: String,

    @SerializedName("Pokok_PKB_Opsen_T4")
    val pokokPkbOpsenT4: String,

    @SerializedName("Pokok_PKB_Opsen_T5")
    val pokokPkbOpsenT5: String,

    @SerializedName("Total_Pokok_PKB_Opsen")
    val totalPokokPkbOpsen: String,

    @SerializedName("Denda_PKB_T0")
    val dendaPkbT0: String,

    @SerializedName("Denda_PKB_T1")
    val dendaPkbT1: String,

    @SerializedName("Denda_PKB_T2")
    val dendaPkbT2: String,

    @SerializedName("Denda_PKB_T3")
    val dendaPkbT3: String,

    @SerializedName("Denda_PKB_T4")
    val dendaPkbT4: String,

    @SerializedName("Denda_PKB_T5")
    val dendaPkbT5: String,

    @SerializedName("Total_Denda_PKB")
    val totalDendaPkb: String,

    @SerializedName("Denda_PKB_Opsen_T0")
    val dendaPkbOpsenT0: String,

    @SerializedName("Denda_PKB_Opsen_T1")
    val dendaPkbOpsenT1: String,

    @SerializedName("Denda_PKB_Opsen_T2")
    val dendaPkbOpsenT2: String,

    @SerializedName("Denda_PKB_Opsen_T3")
    val dendaPkbOpsenT3: String,

    @SerializedName("Denda_PKB_Opsen_T4")
    val dendaPkbOpsenT4: String,

    @SerializedName("Denda_PKB_Opsen_T5")
    val dendaPkbOpsenT5: String,

    @SerializedName("Total_Denda_PKB_Opsen")
    val totalDendaPkbOpsen: String,

    @SerializedName("Pokok_SWDK_T0")
    val pokokSwdkT0: String,

    @SerializedName("Pokok_SWDK_T1")
    val pokokSwdkT1: String,

    @SerializedName("Pokok_SWDK_T2")
    val pokokSwdkT2: String,

    @SerializedName("Pokok_SWDK_T3")
    val pokokSwdkT3: String,

    @SerializedName("Pokok_SWDK_T4")
    val pokokSwdkT4: String,

    @SerializedName("Total_Pokok_SWDK")
    val totalPokokSwdk: String,

    @SerializedName("Denda_SWDK_T0")
    val dendaSwdkT0: String,

    @SerializedName("Denda_SWDK_T1")
    val dendaSwdkT1: String,

    @SerializedName("Denda_SWDK_T2")
    val dendaSwdkT2: String,

    @SerializedName("Denda_SWDK_T3")
    val dendaSwdkT3: String,

    @SerializedName("Denda_SWDK_T4")
    val dendaSwdkT4: String,

    @SerializedName("Total_Denda_SWDK")
    val totalDendaSwdk: String,

    @SerializedName("STNK")
    val stnk: String,

    @SerializedName("TNKB")
    val tnkb: String,

    @SerializedName("Total_Pajak_Kendaraan")
    val totalPajakKendaraan: String
)