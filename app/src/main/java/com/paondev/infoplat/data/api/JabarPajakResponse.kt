package com.paondev.infoplat.data.api

import com.google.gson.annotations.SerializedName

data class JabarPajakResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val data: JabarPajakData?
)

data class JabarPajakData(
    @SerializedName("namaMerk")
    val namaMerk: String,
    @SerializedName("jenis")
    val jenis: String,
    @SerializedName("tahunBuatan")
    val tahunBuatan: String,
    @SerializedName("milikKe")
    val milikKe: String,
    @SerializedName("namaModel")
    val namaModel: String,
    @SerializedName("warna")
    val warna: String,
    @SerializedName("noPolisi")
    val noPolisi: String,
    @SerializedName("infoPkbPnpb")
    val infoPkbPnpb: InfoPkbPnpb,
    @SerializedName("infoPembayaran")
    val infoPembayaran: InfoPembayaran,
    @SerializedName("infoKendaraan")
    val infoKendaraan: Map<String, Any>,
    @SerializedName("waktuProses")
    val waktuProses: String,
    @SerializedName("keterangan")
    val keterangan: String,
    @SerializedName("isFiveYear")
    val isFiveYear: Boolean,
    @SerializedName("isBlocked")
    val isBlocked: Boolean,
    @SerializedName("blockedDescription")
    val blockedDescription: String,
    @SerializedName("isCompany")
    val isCompany: Boolean,
    @SerializedName("canBePaid")
    val canBePaid: Boolean,
    @SerializedName("infoTransaksi")
    val infoTransaksi: InfoTransaksi,
    @SerializedName("isCutOff")
    val isCutOff: Boolean,
    @SerializedName("availablePaymentMethods")
    val availablePaymentMethods: AvailablePaymentMethods,
    @SerializedName("masaPajak")
    val masaPajak: MasaPajak
)

data class InfoPkbPnpb(
    @SerializedName("tanggalPajak")
    val tanggalPajak: String,
    @SerializedName("tanggalStnk")
    val tanggalStnk: String,
    @SerializedName("wilayah")
    val wilayah: String
)

data class InfoPembayaran(
    @SerializedName("pkb")
    val pkb: TaxDetail,
    @SerializedName("opsen")
    val opsen: TaxDetail,
    @SerializedName("swdkllj")
    val swdkllj: TaxDetail,
    @SerializedName("pnpb")
    val pnpb: PnpbDetail,
    @SerializedName("jumlah")
    val jumlah: String
)

data class TaxDetail(
    @SerializedName("pokok")
    val pokok: String,
    @SerializedName("denda")
    val denda: String
)

data class PnpbDetail(
    @SerializedName("stnk")
    val stnk: String,
    @SerializedName("tnkb")
    val tnkb: String
)

data class InfoTransaksi(
    @SerializedName("kendaraanMilik")
    val kendaraanMilik: String,
    @SerializedName("waktuTransaksi")
    val waktuTransaksi: String,
    @SerializedName("waktuKadaluarsa")
    val waktuKadaluarsa: String,
    @SerializedName("durasiKadaluarsa")
    val durasiKadaluarsa: Int,
    @SerializedName("waktuTunggu")
    val waktuTunggu: String,
    @SerializedName("durasiTunggu")
    val durasiTunggu: Int,
    @SerializedName("waktuTungguPembayaran")
    val waktuTungguPembayaran: String,
    @SerializedName("durasiTungguPembayaran")
    val durasiTungguPembayaran: Int,
    @SerializedName("expired_verification_time")
    val expiredVerificationTime: String?,
    @SerializedName("kodeBayar")
    val kodeBayar: String,
    @SerializedName("nominalPembayaran")
    val nominalPembayaran: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("ableToPaymentChecking")
    val ableToPaymentChecking: Boolean,
    @SerializedName("institution")
    val institution: String,
    @SerializedName("institutionGateway")
    val institutionGateway: String
)

data class AvailablePaymentMethods(
    @SerializedName("KodeBayar")
    val kodeBayar: Boolean,
    @SerializedName("QRIS")
    val qris: Boolean,
    @SerializedName("VA")
    val va: Boolean,
    @SerializedName("FINPAY")
    val finpay: Boolean
)

data class MasaPajak(
    @SerializedName("tanggalJatuhTempoSebelumnya")
    val tanggalJatuhTempoSebelumnya: String,
    @SerializedName("tanggalBerlakuSampai")
    val tanggalBerlakuSampai: String
)