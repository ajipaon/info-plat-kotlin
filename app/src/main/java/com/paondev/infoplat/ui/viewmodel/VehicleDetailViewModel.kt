package com.paondev.infoplat.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.google.gson.Gson
import javax.inject.Inject
import com.paondev.infoplat.data.api.*
import com.paondev.infoplat.data.repository.ProvinceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class VehicleDetailViewModel @Inject constructor(
    private val repository: ProvinceRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<VehicleDetailUiState>(VehicleDetailUiState.Loading)
    val uiState: StateFlow<VehicleDetailUiState> = _uiState.asStateFlow()
    
    fun fetchVehicleData(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String,
        noRangka: String = ""
    ) {
        _uiState.value = VehicleDetailUiState.Loading
        
        viewModelScope.launch {
            when (provinceCode) {
                "JTM" -> fetchJatimVehicleData(provinceCode, headPlat, bodyPlat, tailPlat, noRangka)
                "JBR" -> fetchJabarVehicleData(provinceCode, headPlat, bodyPlat, tailPlat)
                "DIY" -> fetchDiypVehicleData(provinceCode, headPlat, bodyPlat, tailPlat)
                "BNTN" -> fetchBantenVehicleData(provinceCode, headPlat, bodyPlat, tailPlat)
                "BALI" -> fetchBaliVehicleData(provinceCode, headPlat, bodyPlat, tailPlat, noRangka)
                "BNKLU" -> fetchBangkaBelitungVehicleData(provinceCode, headPlat, bodyPlat, tailPlat)
                "BDRLMP" -> fetchLampungVehicleData(provinceCode, headPlat, bodyPlat, tailPlat, noRangka)
                "RIAU" -> fetchRiauVehicleData(provinceCode, headPlat, bodyPlat, tailPlat, noRangka)
                "SUMBAR" -> fetchSumbarVehicleData(provinceCode, headPlat, bodyPlat, tailPlat, noRangka)
                else -> {
                    _uiState.value = VehicleDetailUiState.Error("Provinsi tidak didukung")
                }
            }
        }
    }
    
    private suspend fun fetchJatimVehicleData(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String,
        noRangka: String
    ) {
        try {
            // Get captcha
            val captchaResult = repository.getJatimCaptcha()
            if (captchaResult.isFailure) {
                _uiState.value = VehicleDetailUiState.Error("Gagal mendapatkan captcha")
                return
            }
            
            val captchaResponse = captchaResult.getOrNull()!!
            
            // Try OCR
            val ocrResult = repository.solveOcr(captchaResponse.image ?: "")
            
            if (ocrResult.isSuccess && ocrResult.getOrNull()?.success == true) {
                // OCR success - verify directly
                val ocrText = ocrResult.getOrNull()?.data?.replace(" ", "") ?: ""
                val nopol = "$headPlat$bodyPlat$tailPlat".lowercase()
                val verifyResult = repository.getJatimVehicleInfo(
                    sessionId = captchaResponse.sessionId,
                    captchaCode = ocrText,
                    nopol = nopol,
                    norang = noRangka
                )
                
                if (verifyResult.isSuccess) {
                    val jatimResponse = verifyResult.getOrNull()!!
                    val convertedData = convertJatimToJabar(jatimResponse)
                    if (convertedData.data != null) {
                        _uiState.value = VehicleDetailUiState.Success(convertedData)
                    } else {
                        _uiState.value = VehicleDetailUiState.Error(convertedData.message ?: "Data kendaraan tidak ditemukan")
                    }
                } else {
                    _uiState.value = VehicleDetailUiState.Error(verifyResult.exceptionOrNull()?.message ?: "Gagal memverifikasi captcha")
                }
            } else {
                // OCR failed - need manual captcha input
                _uiState.value = VehicleDetailUiState.NeedCaptcha(captchaResponse)
            }
        } catch (e: Exception) {
            Log.e("VehicleDetailVM", "Error fetching Jatim data", e)
            _uiState.value = VehicleDetailUiState.Error("Gagal memuat data kendaraan: ${e.message}")
        }
    }
    
    suspend fun verifyJatimCaptcha(
        sessionId: String,
        captchaCode: String,
        nopol: String,
        noRangka: String
    ) {
        _uiState.value = VehicleDetailUiState.Loading
        
        viewModelScope.launch {
            try {
                val result = repository.getJatimVehicleInfo(
                    sessionId = sessionId,
                    captchaCode = captchaCode,
                    nopol = nopol,
                    norang = noRangka
                )
                
                if (result.isSuccess) {
                    val jatimResponse = result.getOrNull()!!
                    val convertedData = convertJatimToJabar(jatimResponse)
                    if (convertedData.data != null) {
                        _uiState.value = VehicleDetailUiState.Success(convertedData)
                    } else {
                        _uiState.value = VehicleDetailUiState.Error(convertedData.message ?: "Data kendaraan tidak ditemukan")
                    }
                } else {
                    _uiState.value = VehicleDetailUiState.Error(result.exceptionOrNull()?.message ?: "Gagal memverifikasi captcha")
                }
            } catch (e: Exception) {
                Log.e("VehicleDetailVM", "Error verifying Jatim captcha", e)
                _uiState.value = VehicleDetailUiState.Error("Gagal memverifikasi captcha: ${e.message}")
            }
        }
    }
    
    private suspend fun fetchJabarVehicleData(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String
    ) {
        try {
            val result = repository.getVehicleInfo(provinceCode, headPlat, bodyPlat, tailPlat)
            if (result.isSuccess) {
                val response = result.getOrNull()!!
                if (response.data != null) {
                    _uiState.value = VehicleDetailUiState.Success(response)
                } else {
                    _uiState.value = VehicleDetailUiState.Error(response.message ?: "Data kendaraan tidak ditemukan")
                }
            } else {
                _uiState.value = VehicleDetailUiState.Error(result.exceptionOrNull()?.message ?: "Gagal memuat data")
            }
        } catch (e: Exception) {
            Log.e("VehicleDetailVM", "Error fetching Jabar data", e)
            _uiState.value = VehicleDetailUiState.Error("Gagal memuat data kendaraan")
        }
    }
    
    private suspend fun fetchDiypVehicleData(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String
    ) {
        try {
            val result = repository.getDiypVehicleInfo(provinceCode, headPlat, bodyPlat, tailPlat)
            if (result.isSuccess) {
                val convertedData = convertDiypToJabar(result.getOrNull()!!)
                if (convertedData.data != null) {
                    _uiState.value = VehicleDetailUiState.Success(convertedData)
                } else {
                    _uiState.value = VehicleDetailUiState.Error(convertedData.message ?: "Data kendaraan tidak ditemukan")
                }
            } else {
                _uiState.value = VehicleDetailUiState.Error(result.exceptionOrNull()?.message ?: "Gagal memuat data")
            }
        } catch (e: Exception) {
            Log.e("VehicleDetailVM", "Error fetching DIY data", e)
            _uiState.value = VehicleDetailUiState.Error("Gagal memuat data kendaraan")
        }
    }
    
    private suspend fun fetchBantenVehicleData(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String
    ) {
        try {
            val result = repository.getBantenVehicleInfo(provinceCode, headPlat, bodyPlat, tailPlat)
            if (result.isSuccess) {
                val convertedData = convertBantenToJabar(result.getOrNull()!!)
                if (convertedData.data != null) {
                    _uiState.value = VehicleDetailUiState.Success(convertedData)
                } else {
                    _uiState.value = VehicleDetailUiState.Error(convertedData.message ?: "Data kendaraan tidak ditemukan")
                }
            } else {
                _uiState.value = VehicleDetailUiState.Error(result.exceptionOrNull()?.message ?: "Gagal memuat data")
            }
        } catch (e: Exception) {
            Log.e("VehicleDetailVM", "Error fetching Banten data", e)
            _uiState.value = VehicleDetailUiState.Error("Gagal memuat data kendaraan")
        }
    }
    
    private suspend fun fetchBaliVehicleData(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String,
        noRangka: String
    ) {
        try {
            val result = repository.getBaliVehicleInfo(provinceCode, headPlat, bodyPlat, tailPlat, noRangka)
            if (result.isSuccess) {
                val convertedData = convertBaliToJabar(result.getOrNull()!!)
                if (convertedData.data != null) {
                    _uiState.value = VehicleDetailUiState.Success(convertedData)
                } else {
                    _uiState.value = VehicleDetailUiState.Error(convertedData.message ?: "Data kendaraan tidak ditemukan")
                }
            } else {
                _uiState.value = VehicleDetailUiState.Error(result.exceptionOrNull()?.message ?: "Gagal memuat data")
            }
        } catch (e: Exception) {
            Log.e("VehicleDetailVM", "Error fetching Bali data", e)
            _uiState.value = VehicleDetailUiState.Error("Gagal memuat data kendaraan")
        }
    }
    
    private suspend fun fetchBangkaBelitungVehicleData(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String
    ) {
        try {
            val result = repository.getBangkaBelitungVehicleInfo(provinceCode, headPlat, bodyPlat, tailPlat)
            if (result.isSuccess) {
                val convertedData = convertBangkaBelitungToJabar(result.getOrNull()!!)
                if (convertedData.data != null) {
                    _uiState.value = VehicleDetailUiState.Success(convertedData)
                } else {
                    _uiState.value = VehicleDetailUiState.Error(convertedData.message ?: "Data kendaraan tidak ditemukan")
                }
            } else {
                _uiState.value = VehicleDetailUiState.Error(result.exceptionOrNull()?.message ?: "Gagal memuat data")
            }
        } catch (e: Exception) {
            Log.e("VehicleDetailVM", "Error fetching Bangka Belitung data", e)
            _uiState.value = VehicleDetailUiState.Error("Gagal memuat data kendaraan")
        }
    }
    
    private suspend fun fetchLampungVehicleData(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String,
        noRangka: String
    ) {
        try {
            val result = repository.getLampungVehicleInfo(provinceCode, headPlat, bodyPlat, tailPlat, noRangka)
            if (result.isSuccess) {
                val convertedData = convertLampungToJabar(result.getOrNull()!!)
                if (convertedData.data != null) {
                    _uiState.value = VehicleDetailUiState.Success(convertedData)
                } else {
                    _uiState.value = VehicleDetailUiState.Error(convertedData.message ?: "Data kendaraan tidak ditemukan")
                }
            } else {
                _uiState.value = VehicleDetailUiState.Error(result.exceptionOrNull()?.message ?: "Gagal memuat data")
            }
        } catch (e: Exception) {
            Log.e("VehicleDetailVM", "Error fetching Lampung data", e)
            _uiState.value = VehicleDetailUiState.Error("Gagal memuat data kendaraan")
        }
    }
    
    private suspend fun fetchRiauVehicleData(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String,
        noRangka: String
    ) {
        try {
            val result = repository.getRiauVehicleInfo(provinceCode, headPlat, bodyPlat, tailPlat, noRangka)
            if (result.isSuccess) {
                val convertedData = convertRiauToJabar(result.getOrNull()!!)
                if (convertedData.data != null) {
                    _uiState.value = VehicleDetailUiState.Success(convertedData)
                } else {
                    _uiState.value = VehicleDetailUiState.Error(convertedData.message ?: "Data kendaraan tidak ditemukan")
                }
            } else {
                _uiState.value = VehicleDetailUiState.Error(result.exceptionOrNull()?.message ?: "Gagal memuat data")
            }
        } catch (e: Exception) {
            Log.e("VehicleDetailVM", "Error fetching Riau data", e)
            _uiState.value = VehicleDetailUiState.Error("Gagal memuat data kendaraan")
        }
    }
    
    private suspend fun fetchSumbarVehicleData(
        provinceCode: String,
        headPlat: String,
        bodyPlat: String,
        tailPlat: String,
        noRangka: String
    ) {
        try {
            val result = repository.getSumbarVehicleInfo(provinceCode, headPlat, bodyPlat, tailPlat, noRangka)
            if (result.isSuccess) {
                val convertedData = convertSumbarToJabar(result.getOrNull()!!)
                if (convertedData.data != null) {
                    _uiState.value = VehicleDetailUiState.Success(convertedData)
                } else {
                    _uiState.value = VehicleDetailUiState.Error(convertedData.message ?: "Data kendaraan tidak ditemukan")
                }
            } else {
                _uiState.value = VehicleDetailUiState.Error(result.exceptionOrNull()?.message ?: "Gagal memuat data")
            }
        } catch (e: Exception) {
            Log.e("VehicleDetailVM", "Error fetching Sumbar data", e)
            _uiState.value = VehicleDetailUiState.Error("Gagal memuat data kendaraan")
        }
    }
    
    private fun convertRawDataToJabarResponse(rawData: String, provinceCode: String): JabarPajakResponse {
        return try {
            when (provinceCode) {
                "JTM" -> {
                    val jatimResponse = Gson().fromJson(rawData, JatimPkbResponse::class.java)
                    convertJatimToJabar(jatimResponse)
                }
                "BNTN" -> {
                    val bantenResponse = Gson().fromJson(rawData, BantenPajakResponse::class.java)
                    convertBantenToJabar(bantenResponse)
                }
                "BALI" -> {
                    val baliResponse = Gson().fromJson(rawData, BaliPajakResponse::class.java)
                    convertBaliToJabar(baliResponse)
                }
                "BNKLU" -> {
                    val bangkaBelitungResponse = Gson().fromJson(rawData, BangkaBelitungPajakResponse::class.java)
                    convertBangkaBelitungToJabar(bangkaBelitungResponse)
                }
                "BDRLMP" -> {
                    val lampungResponse = Gson().fromJson(rawData, LampungPajakResponse::class.java)
                    convertLampungToJabar(lampungResponse)
                }
                "RIAU" -> {
                    val riauResponse = Gson().fromJson(rawData, RiauPajakResponse::class.java)
                    convertRiauToJabar(riauResponse)
                }
                "SUMBAR" -> {
                    val sumbarResponse = Gson().fromJson(rawData, SumbarPajakResponse::class.java)
                    convertSumbarToJabar(sumbarResponse)
                }
                "DIY" -> {
                    val diypResponse = Gson().fromJson(rawData, DiypPajakResponse::class.java)
                    convertDiypToJabar(diypResponse)
                }
                else -> {
                    JabarPajakResponse(
                        status = false,
                        message = "Provinsi tidak dikenal",
                        code = "400",
                        data = null
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("VehicleDetailVM", "Conversion error for $provinceCode", e)
            JabarPajakResponse(
                status = false,
                message = "Gagal mengkonversi data",
                code = "500",
                data = null
            )
        }
    }
    
    // Conversion functions (moved from PlateCheckScreen)
    private fun convertBantenToJabar(bantenResponse: BantenPajakResponse): JabarPajakResponse {
        val data = bantenResponse.data
        return if (bantenResponse.success && data != null) {
            val kendaraan = data.kendaraan
            val pajak = data.pajak
            val totalValue = pajak.jumlahInt
            
            JabarPajakResponse(
                status = true,
                message = pajak.keterangan,
                code = "200",
                data = JabarPajakData(
                    namaMerk = kendaraan.merek,
                    jenis = kendaraan.jenis,
                    tahunBuatan = kendaraan.tahun,
                    milikKe = "1",
                    namaModel = kendaraan.tipeModel,
                    warna = kendaraan.warna,
                    noPolisi = kendaraan.noPolisi,
                    infoPkbPnpb = InfoPkbPnpb(
                        tanggalPajak = pajak.tglAkhirPkbYad,
                        tanggalStnk = pajak.tglAkhirStnkLalu,
                        wilayah = pajak.kabKota
                    ),
                    infoPembayaran = InfoPembayaran(
                        pkb = TaxDetail(pokok = pajak.pkbPokok, denda = pajak.pkbDenda),
                        opsen = TaxDetail(pokok = pajak.opsenPkbPokok, denda = pajak.opsenPkbDenda),
                        swdkllj = TaxDetail(pokok = pajak.swdklljPokok, denda = pajak.swdklljDenda),
                        pnpb = PnpbDetail(stnk = pajak.stnk, tnkb = pajak.tnkb),
                        jumlah = pajak.jumlah
                    ),
                    infoKendaraan = mapOf(
                        "merk" to kendaraan.merek,
                        "model" to kendaraan.tipeModel,
                        "tahun" to kendaraan.tahun,
                        "cc" to kendaraan.cc,
                        "bbm" to kendaraan.bbm,
                        "warna" to kendaraan.warna,
                        "warnaPlat" to kendaraan.warnaPlat,
                        "pemilik" to kendaraan.namaPemilik,
                        "alamat" to kendaraan.alamat,
                        "noRangkaMesin" to kendaraan.noRangkaMesin
                    ),
                    waktuProses = data.diproses,
                    keterangan = pajak.keterangan,
                    isFiveYear = false,
                    isBlocked = false,
                    blockedDescription = "",
                    isCompany = false,
                    canBePaid = true,
                    infoTransaksi = InfoTransaksi(
                        kendaraanMilik = "1",
                        waktuTransaksi = "",
                        waktuKadaluarsa = "",
                        durasiKadaluarsa = 0,
                        waktuTunggu = "",
                        durasiTunggu = 0,
                        waktuTungguPembayaran = "",
                        durasiTungguPembayaran = 0,
                        expiredVerificationTime = null,
                        kodeBayar = "",
                        nominalPembayaran = totalValue.toString(),
                        status = "success",
                        ableToPaymentChecking = true,
                        institution = "SAMSAT BANTEN",
                        institutionGateway = "SAMSAT BANTEN"
                    ),
                    isCutOff = false,
                    availablePaymentMethods = AvailablePaymentMethods(
                        kodeBayar = false,
                        qris = true,
                        va = true,
                        finpay = false
                    ),
                    masaPajak = MasaPajak(
                        tanggalJatuhTempoSebelumnya = pajak.tglAkhirPkbLalu,
                        tanggalBerlakuSampai = pajak.tglAkhirPkbYad
                    )
                )
            )
        } else {
            JabarPajakResponse(
                status = false,
                message = bantenResponse.message ?: "Data tidak ditemukan",
                code = "400",
                data = null
            )
        }
    }
    
    private fun convertDiypToJabar(diypResponse: DiypPajakResponse): JabarPajakResponse {
        val data = diypResponse.data
        return if (data != null) {
            val pkbValue = data.pkb.trim()
            val swdklljValue = data.swdkllj.trim()
            val totalValue = (data.pkb.toDoubleOrNull() ?: 0.0) + (data.swdkllj.toDoubleOrNull() ?: 0.0)
            
            JabarPajakResponse(
                status = true,
                message = "Data ditemukan",
                code = "200",
                data = JabarPajakData(
                    namaMerk = data.nmmerekkb.trim(),
                    jenis = "Kendaraan",
                    tahunBuatan = data.tahunkb.trim(),
                    milikKe = "1",
                    namaModel = data.nmmodelkb.trim(),
                    warna = "-",
                    noPolisi = data.nopolisi.trim(),
                    infoPkbPnpb = InfoPkbPnpb(
                        tanggalPajak = data.tgakhirpkb.trim(),
                        tanggalStnk = data.tgakhirpkb.trim(),
                        wilayah = "DIY"
                    ),
                    infoPembayaran = InfoPembayaran(
                        pkb = TaxDetail(pokok = pkbValue, denda = "0"),
                        opsen = TaxDetail(pokok = "0", denda = "0"),
                        swdkllj = TaxDetail(pokok = swdklljValue, denda = "0"),
                        pnpb = PnpbDetail(stnk = "100000", tnkb = "60000"),
                        jumlah = totalValue.toString()
                    ),
                    infoKendaraan = mapOf(
                        "merk" to data.nmmerekkb.trim(),
                        "model" to data.nmmodelkb.trim(),
                        "tahun" to data.tahunkb.trim()
                    ),
                    waktuProses = "",
                    keterangan = "Data ditemukan",
                    isFiveYear = false,
                    isBlocked = false,
                    blockedDescription = "",
                    isCompany = false,
                    canBePaid = true,
                    infoTransaksi = InfoTransaksi(
                        kendaraanMilik = "1",
                        waktuTransaksi = "",
                        waktuKadaluarsa = "",
                        durasiKadaluarsa = 0,
                        waktuTunggu = "",
                        durasiTunggu = 0,
                        waktuTungguPembayaran = "",
                        durasiTungguPembayaran = 0,
                        expiredVerificationTime = null,
                        kodeBayar = "",
                        nominalPembayaran = totalValue.toString(),
                        status = "success",
                        ableToPaymentChecking = true,
                        institution = "SAMSAT DIY",
                        institutionGateway = "SAMSAT DIY"
                    ),
                    isCutOff = false,
                    availablePaymentMethods = AvailablePaymentMethods(
                        kodeBayar = false,
                        qris = true,
                        va = true,
                        finpay = false
                    ),
                    masaPajak = MasaPajak(
                        tanggalJatuhTempoSebelumnya = data.tgakhirpkb.trim(),
                        tanggalBerlakuSampai = data.tgakhirpkb.trim()
                    )
                )
            )
        } else {
            JabarPajakResponse(
                status = false,
                message = "Data tidak ditemukan",
                code = "404",
                data = null
            )
        }
    }
    
    private fun convertBaliToJabar(baliResponse: BaliPajakResponse): JabarPajakResponse {
        val data = baliResponse.data
        return if (baliResponse.success && data != null) {
            val detail = data.detail
            val pembayaranList = data.pembayaran
            
            val pkbItem = pembayaranList.find { it.jenis == "PKB" }
            val swdklljItem = pembayaranList.find { it.jenis == "SWDKLLJ" }
            val totalItem = pembayaranList.find { it.jenis == "TOTAL" }
            
            val pkbValue = pkbItem?.jumlah ?: "0"
            val swdklljValue = swdklljItem?.jumlah ?: "0"
            val totalValue = totalItem?.jumlah ?: "0"
            
            val masaBerlakuParts = detail.masaBerlaku.split(" / ")
            val tanggalPajak = if (masaBerlakuParts.isNotEmpty()) masaBerlakuParts[0].trim() else ""
            val tanggalStnk = if (masaBerlakuParts.size > 1) masaBerlakuParts[1].trim() else ""
            
            JabarPajakResponse(
                status = true,
                message = detail.status,
                code = "200",
                data = JabarPajakData(
                    namaMerk = detail.merk,
                    jenis = detail.model,
                    tahunBuatan = detail.milikKeTahun.split(" / ").getOrNull(1)?.trim() ?: "",
                    milikKe = detail.milikKeTahun.split(" / ").getOrNull(0)?.trim() ?: "1",
                    namaModel = "${detail.merk} ${detail.tipe}",
                    warna = "-",
                    noPolisi = detail.nomorPolisi.split(" (").getOrNull(0)?.trim() ?: detail.nomorPolisi,
                    infoPkbPnpb = InfoPkbPnpb(
                        tanggalPajak = tanggalPajak,
                        tanggalStnk = tanggalStnk,
                        wilayah = "BALI"
                    ),
                    infoPembayaran = InfoPembayaran(
                        pkb = TaxDetail(
                            pokok = pkbItem?.pokok ?: "0",
                            denda = pkbItem?.denda ?: "0"
                        ),
                        opsen = TaxDetail(pokok = "0", denda = "0"),
                        swdkllj = TaxDetail(
                            pokok = swdklljItem?.pokok ?: "0",
                            denda = swdklljItem?.denda ?: "0"
                        ),
                        pnpb = PnpbDetail(stnk = "0", tnkb = "0"),
                        jumlah = totalValue
                    ),
                    infoKendaraan = mapOf(
                        "merk" to detail.merk,
                        "tipe" to detail.tipe,
                        "model" to detail.model,
                        "nama" to detail.nama,
                        "alamat" to detail.alamat,
                        "milikKeTahun" to detail.milikKeTahun,
                        "bahanBakar" to detail.bahanBakar,
                        "jenisTransaksi" to detail.jenisTransaksi,
                        "masaBerlaku" to detail.masaBerlaku,
                        "njkbDppkb" to detail.njkbDppkb,
                        "tarifPengenaan" to detail.tarifPengenaan,
                        "masaPajak" to detail.masaPajak
                    ),
                    waktuProses = "",
                    keterangan = detail.status,
                    isFiveYear = false,
                    isBlocked = false,
                    blockedDescription = "",
                    isCompany = false,
                    canBePaid = true,
                    infoTransaksi = InfoTransaksi(
                        kendaraanMilik = detail.milikKeTahun,
                        waktuTransaksi = "",
                        waktuKadaluarsa = "",
                        durasiKadaluarsa = 0,
                        waktuTunggu = "",
                        durasiTunggu = 0,
                        waktuTungguPembayaran = "",
                        durasiTungguPembayaran = 0,
                        expiredVerificationTime = null,
                        kodeBayar = "",
                        nominalPembayaran = totalValue,
                        status = "success",
                        ableToPaymentChecking = true,
                        institution = "SAMSAT BALI",
                        institutionGateway = "SAMSAT BALI"
                    ),
                    isCutOff = false,
                    availablePaymentMethods = AvailablePaymentMethods(
                        kodeBayar = false,
                        qris = true,
                        va = true,
                        finpay = false
                    ),
                    masaPajak = MasaPajak(
                        tanggalJatuhTempoSebelumnya = tanggalPajak,
                        tanggalBerlakuSampai = tanggalStnk
                    )
                )
            )
        } else {
            JabarPajakResponse(
                status = false,
                message = baliResponse.message ?: "Data tidak ditemukan",
                code = "400",
                data = null
            )
        }
    }
    
    private fun convertBangkaBelitungToJabar(bangkaBelitungResponse: BangkaBelitungPajakResponse): JabarPajakResponse {
        val data = bangkaBelitungResponse.data
        return if (bangkaBelitungResponse.success && data != null) {
            val totalValue = data.jumlahTotal
            
            JabarPajakResponse(
                status = true,
                message = "Data ditemukan",
                code = "200",
                data = JabarPajakData(
                    namaMerk = data.merek,
                    jenis = "Kendaraan",
                    tahunBuatan = data.thBuatan,
                    milikKe = "1",
                    namaModel = data.model,
                    warna = data.warna,
                    noPolisi = data.nopol.trim(),
                    infoPkbPnpb = InfoPkbPnpb(
                        tanggalPajak = data.akhirPkb,
                        tanggalStnk = data.akhirStnkb,
                        wilayah = "BANGKA BELITUNG"
                    ),
                    infoPembayaran = InfoPembayaran(
                        pkb = TaxDetail(pokok = data.beaPkb, denda = data.dendaBeaPkb),
                        opsen = TaxDetail(pokok = data.opsenPkb, denda = data.dendaOpsenPkb),
                        swdkllj = TaxDetail(pokok = data.pokokSw, denda = data.totalDendaSw),
                        pnpb = PnpbDetail(stnk = data.pnbpStnk, tnkb = data.pnbpPlat),
                        jumlah = data.jumlahTotal
                    ),
                    infoKendaraan = mapOf(
                        "merk" to data.merek,
                        "model" to data.model,
                        "tahun" to data.thBuatan,
                        "warna" to data.warna,
                        "warnaPlat" to data.warnaPlat,
                        "jumlahCc" to data.jumlahCc,
                        "bbm" to data.bbm,
                        "noRangka" to data.noRangka,
                        "noMesin" to data.noMesin,
                        "nama" to data.nama
                    ),
                    waktuProses = "",
                    keterangan = "Data ditemukan",
                    isFiveYear = false,
                    isBlocked = false,
                    blockedDescription = "",
                    isCompany = false,
                    canBePaid = true,
                    infoTransaksi = InfoTransaksi(
                        kendaraanMilik = "1",
                        waktuTransaksi = "",
                        waktuKadaluarsa = "",
                        durasiKadaluarsa = 0,
                        waktuTunggu = "",
                        durasiTunggu = 0,
                        waktuTungguPembayaran = "",
                        durasiTungguPembayaran = 0,
                        expiredVerificationTime = null,
                        kodeBayar = "",
                        nominalPembayaran = totalValue,
                        status = "success",
                        ableToPaymentChecking = true,
                        institution = "SAMSAT BANGKA BELITUNG",
                        institutionGateway = "SAMSAT BANGKA BELITUNG"
                    ),
                    isCutOff = false,
                    availablePaymentMethods = AvailablePaymentMethods(
                        kodeBayar = false,
                        qris = true,
                        va = true,
                        finpay = false
                    ),
                    masaPajak = MasaPajak(
                        tanggalJatuhTempoSebelumnya = data.akhirPkb,
                        tanggalBerlakuSampai = data.akhirStnkb
                    )
                )
            )
        } else {
            JabarPajakResponse(
                status = false,
                message = bangkaBelitungResponse.message ?: "Data tidak ditemukan",
                code = "400",
                data = null
            )
        }
    }
    
    private fun convertLampungToJabar(lampungResponse: LampungPajakResponse): JabarPajakResponse {
        val data = lampungResponse.data
        return if (lampungResponse.success && data != null) {
            val totalValue = data.jumlahBayar.angka
            
            JabarPajakResponse(
                status = true,
                message = "Data ditemukan",
                code = "200",
                data = JabarPajakData(
                    namaMerk = data.merek,
                    jenis = data.jenisKendaraan,
                    tahunBuatan = data.tahun.toString(),
                    milikKe = "${data.kendaraanKe}",
                    namaModel = data.type,
                    warna = "-",
                    noPolisi = "BE${data.kendaraanKe}XX",
                    infoPkbPnpb = InfoPkbPnpb(
                        tanggalPajak = data.jatuhTempoPkb,
                        tanggalStnk = data.stnkBerlakuSampai,
                        wilayah = "LAMPUNG"
                    ),
                    infoPembayaran = InfoPembayaran(
                        pkb = TaxDetail(
                            pokok = data.pokokPkb.raw,
                            denda = data.dendaPkb.raw
                        ),
                        opsen = TaxDetail(
                            pokok = data.pokokOpsenPkb.raw,
                            denda = data.dendaOpsenPkb.raw
                        ),
                        swdkllj = TaxDetail(
                            pokok = data.pokokSwdkllj.raw,
                            denda = data.dendaSwdkllj.raw
                        ),
                        pnpb = PnpbDetail(
                            stnk = "100000",
                            tnkb = "60000"
                        ),
                        jumlah = data.jumlahBayar.raw
                    ),
                    infoKendaraan = mapOf(
                        "merk" to data.merek,
                        "type" to data.type,
                        "jenisKendaraan" to data.jenisKendaraan,
                        "tahun" to data.tahun.toString(),
                        "isiSilinder" to data.isiSilinder.toString(),
                        "warnaTnkb" to data.warnaTnkb,
                        "njkb" to data.njkb.raw,
                        "bobot" to data.bobot.toString(),
                        "dasarPkb" to data.dasarPkb.raw,
                        "nilaiPajakPertahun" to data.nilaiPajakPertahun.raw,
                        "keterlambatan" to data.keterlambatan,
                        "catatan" to data.catatan
                    ) as Map<String, Any>,
                    waktuProses = data.tglBayarTerakhir,
                    keterangan = data.keterlambatan,
                    isFiveYear = false,
                    isBlocked = false,
                    blockedDescription = "",
                    isCompany = false,
                    canBePaid = true,
                    infoTransaksi = InfoTransaksi(
                        kendaraanMilik = "${data.kendaraanDari}",
                        waktuTransaksi = data.tglBayarTerakhir,
                        waktuKadaluarsa = "",
                        durasiKadaluarsa = 0,
                        waktuTunggu = "",
                        durasiTunggu = 0,
                        waktuTungguPembayaran = "",
                        durasiTungguPembayaran = 0,
                        expiredVerificationTime = null,
                        kodeBayar = "",
                        nominalPembayaran = totalValue.toString(),
                        status = "success",
                        ableToPaymentChecking = true,
                        institution = "SAMSAT LAMPUNG",
                        institutionGateway = "SAMSAT LAMPUNG"
                    ),
                    isCutOff = false,
                    availablePaymentMethods = AvailablePaymentMethods(
                        kodeBayar = false,
                        qris = true,
                        va = true,
                        finpay = false
                    ),
                    masaPajak = MasaPajak(
                        tanggalJatuhTempoSebelumnya = data.jatuhTempoPkb,
                        tanggalBerlakuSampai = data.stnkBerlakuSampai
                    )
                )
            )
        } else {
            JabarPajakResponse(
                status = false,
                message = lampungResponse.message ?: "Data tidak ditemukan",
                code = "400",
                data = null
            )
        }
    }
    
    private fun convertSumbarToJabar(sumbarResponse: SumbarPajakResponse): JabarPajakResponse {
        val data = sumbarResponse.data
        return if (sumbarResponse.success && data != null) {
            val totalValue = data.jumlah
            
            JabarPajakResponse(
                status = true,
                message = "Data ditemukan",
                code = "200",
                data = JabarPajakData(
                    namaMerk = data.merek,
                    jenis = "Kendaraan",
                    tahunBuatan = data.tahun,
                    milikKe = "1",
                    namaModel = data.tipe,
                    warna = data.warna,
                    noPolisi = data.tnkb,
                    infoPkbPnpb = InfoPkbPnpb(
                        tanggalPajak = data.tglPajak,
                        tanggalStnk = data.tglStnk,
                        wilayah = "SUMBAR"
                    ),
                    infoPembayaran = InfoPembayaran(
                        pkb = TaxDetail(pokok = data.pkbPokok, denda = data.pkbDenda),
                        opsen = TaxDetail(pokok = data.opsPkbPokok, denda = data.opsPkbDenda),
                        swdkllj = TaxDetail(pokok = data.swdklljPokok, denda = data.swdklljDenda),
                        pnpb = PnpbDetail(stnk = data.admStnk, tnkb = data.admTnkb),
                        jumlah = data.jumlah
                    ),
                    infoKendaraan = mapOf(
                        "tnkb" to data.tnkb,
                        "merek" to data.merek,
                        "tipe" to data.tipe,
                        "tahun" to data.tahun,
                        "warna" to data.warna,
                        "statusBlokir" to data.statusBlokir,
                        "keterangan" to data.keterangan
                    ),
                    waktuProses = "",
                    keterangan = data.keterangan,
                    isFiveYear = false,
                    isBlocked = data.statusBlokir != "-",
                    blockedDescription = if (data.statusBlokir != "-") "Kendaraan dalam status blokir" else "",
                    isCompany = false,
                    canBePaid = data.statusBlokir == "-",
                    infoTransaksi = InfoTransaksi(
                        kendaraanMilik = "1",
                        waktuTransaksi = "",
                        waktuKadaluarsa = "",
                        durasiKadaluarsa = 0,
                        waktuTunggu = "",
                        durasiTunggu = 0,
                        waktuTungguPembayaran = "",
                        durasiTungguPembayaran = 0,
                        expiredVerificationTime = null,
                        kodeBayar = "",
                        nominalPembayaran = totalValue,
                        status = "success",
                        ableToPaymentChecking = data.statusBlokir == "-",
                        institution = "SAMSAT SUMBAR",
                        institutionGateway = "SAMSAT SUMBAR"
                    ),
                    isCutOff = false,
                    availablePaymentMethods = AvailablePaymentMethods(
                        kodeBayar = false,
                        qris = true,
                        va = true,
                        finpay = false
                    ),
                    masaPajak = MasaPajak(
                        tanggalJatuhTempoSebelumnya = data.tglPajak,
                        tanggalBerlakuSampai = data.tglStnk
                    )
                )
            )
        } else {
            JabarPajakResponse(
                status = false,
                message = sumbarResponse.message ?: "Data tidak ditemukan",
                code = "400",
                data = null
            )
        }
    }
    
    private fun convertRiauToJabar(riauResponse: RiauPajakResponse): JabarPajakResponse {
        val data = riauResponse.data
        return if (riauResponse.success && data != null) {
            val totalValue = data.totalPajakKendaraan
            
            JabarPajakResponse(
                status = true,
                message = "Data ditemukan",
                code = "200",
                data = JabarPajakData(
                    namaMerk = data.merekKendaraan,
                    jenis = data.namaJenis,
                    tahunBuatan = data.tahunPembuatan,
                    milikKe = "1",
                    namaModel = data.namaModel,
                    warna = data.warnaKendaraan,
                    noPolisi = data.nopol,
                    infoPkbPnpb = InfoPkbPnpb(
                        tanggalPajak = data.tanggalJatuhTempo,
                        tanggalStnk = data.tanggalStnk,
                        wilayah = "RIAU"
                    ),
                    infoPembayaran = InfoPembayaran(
                        pkb = TaxDetail(
                            pokok = data.totalPokokPkb,
                            denda = data.totalDendaPkb
                        ),
                        opsen = TaxDetail(
                            pokok = data.totalPokokPkbOpsen,
                            denda = data.totalDendaPkbOpsen
                        ),
                        swdkllj = TaxDetail(
                            pokok = data.totalPokokSwdk,
                            denda = data.totalDendaSwdk
                        ),
                        pnpb = PnpbDetail(stnk = data.stnk, tnkb = data.tnkb),
                        jumlah = data.totalPajakKendaraan
                    ),
                    infoKendaraan = mapOf(
                        "nopol" to data.nopol,
                        "namaPemilik" to data.namaPemilik,
                        "alamat" to data.alamat,
                        "merekKendaraan" to data.merekKendaraan,
                        "typeKendaraan" to data.typeKendaraan,
                        "golonganKendaraan" to data.golonganKendaraan,
                        "tahunPembuatan" to data.tahunPembuatan,
                        "warnaKendaraan" to data.warnaKendaraan,
                        "namaModel" to data.namaModel,
                        "namaJenis" to data.namaJenis,
                        "warnaTnkb" to data.warnaTnkb,
                        "njkb" to data.njkb,
                        "bobot" to data.bobot,
                        "dasarPkb" to data.dasarPkb,
                        "lamaTunggakan" to data.lamaTunggakan,
                        "totalPajakKendaraan" to data.totalPajakKendaraan,
                        "pembayaranSebelumnya" to mapOf(
                            "pokokBbnkb" to data.pokokBbnkbSebelumnya,
                            "pokokBbnkbOpsen" to data.pokokBbnkbOpsenSebelumnya,
                            "dendaBbnkb" to data.dendaBbnkbSebelumnya,
                            "dendaBbnkbOpsen" to data.dendaBbnkbOpsenSebelumnya,
                            "pokokPkb" to data.pokokPkbSebelumnya,
                            "pokokPkbOpsen" to data.pokokPkbOpsenSebelumnya,
                            "dendaPkb" to data.dendaPkbSebelumnya,
                            "dendaPkbOpsen" to data.dendaPkbOpsenSebelumnya,
                            "pokokSwdk" to data.pokokSwdkSebelumnya,
                            "dendaSwdk" to data.dendaSwdkSebelumnya,
                            "stnk" to data.stnkSebelumnya,
                            "tnkb" to data.tnkbSebelumnya,
                            "total" to data.totalSebelumnya,
                            "tanggalPembayaran" to data.tanggalPembayaranSebelumnya
                        )
                    ),
                    waktuProses = data.tanggalPembayaranSebelumnya,
                    keterangan = data.lamaTunggakan,
                    isFiveYear = false,
                    isBlocked = false,
                    blockedDescription = "",
                    isCompany = false,
                    canBePaid = true,
                    infoTransaksi = InfoTransaksi(
                        kendaraanMilik = "1",
                        waktuTransaksi = data.tanggalPembayaranSebelumnya,
                        waktuKadaluarsa = "",
                        durasiKadaluarsa = 0,
                        waktuTunggu = "",
                        durasiTunggu = 0,
                        waktuTungguPembayaran = "",
                        durasiTungguPembayaran = 0,
                        expiredVerificationTime = null,
                        kodeBayar = "",
                        nominalPembayaran = totalValue,
                        status = "success",
                        ableToPaymentChecking = true,
                        institution = "SAMSAT RIAU",
                        institutionGateway = "SAMSAT RIAU"
                    ),
                    isCutOff = false,
                    availablePaymentMethods = AvailablePaymentMethods(
                        kodeBayar = false,
                        qris = true,
                        va = true,
                        finpay = false
                    ),
                    masaPajak = MasaPajak(
                        tanggalJatuhTempoSebelumnya = data.tanggalJatuhTempo,
                        tanggalBerlakuSampai = data.tanggalStnk
                    )
                )
            )
        } else {
            JabarPajakResponse(
                status = false,
                message = riauResponse.message ?: "Data tidak ditemukan",
                code = "400",
                data = null
            )
        }
    }
    
    private fun convertJatimToJabar(jatimResponse: com.paondev.infoplat.data.api.JatimPkbResponse): JabarPajakResponse {
        val data = jatimResponse.data
        return if (data?.identitas != null) {
            val pkbValue = data.biayaPenul1Tahunan?.pkb ?: "0"
            val opsenValue = data.biayaPenul1Tahunan?.opsenPkb ?: "0"
            val swdklljValue = data.biayaPenul1Tahunan?.swdkllj ?: "0"
            val totalValue = data.biayaPenul1Tahunan?.total ?: 0
            
            JabarPajakResponse(
                status = true,
                message = data.keterangan?.ket ?: "Data ditemukan",
                code = "200",
                data = JabarPajakData(
                    namaMerk = data.identitas.merk,
                    jenis = data.identitas.model,
                    tahunBuatan = data.identitas.tahunBuat,
                    milikKe = "1",
                    namaModel = data.identitas.type,
                    warna = data.identitas.warna,
                    noPolisi = data.identitas.nopol,
                    infoPkbPnpb = InfoPkbPnpb(
                        tanggalPajak = data.identitas.tglMasaPajak,
                        tanggalStnk = data.identitas.tglMasaPajak,
                        wilayah = "JATIM"
                    ),
                    infoPembayaran = InfoPembayaran(
                        pkb = TaxDetail(pokok = pkbValue, denda = "0"),
                        opsen = TaxDetail(pokok = opsenValue, denda = "0"),
                        swdkllj = TaxDetail(pokok = swdklljValue, denda = "0"),
                        pnpb = PnpbDetail(stnk = "100000", tnkb = "60000"),
                        jumlah = totalValue.toString()
                    ),
                    infoKendaraan = mapOf(
                        "merk" to data.identitas.merk,
                        "type" to data.identitas.type,
                        "tahun" to data.identitas.tahunBuat
                    ),
                    waktuProses = "",
                    keterangan = data.keterangan?.ket ?: "Data ditemukan",
                    isFiveYear = false,
                    isBlocked = false,
                    blockedDescription = "",
                    isCompany = false,
                    canBePaid = true,
                    infoTransaksi = InfoTransaksi(
                        kendaraanMilik = "1",
                        waktuTransaksi = "",
                        waktuKadaluarsa = "",
                        durasiKadaluarsa = 0,
                        waktuTunggu = "",
                        durasiTunggu = 0,
                        waktuTungguPembayaran = "",
                        durasiTungguPembayaran = 0,
                        expiredVerificationTime = null,
                        kodeBayar = "",
                        nominalPembayaran = totalValue.toString(),
                        status = "success",
                        ableToPaymentChecking = true,
                        institution = "BAPENDA JATIM",
                        institutionGateway = "BAPENDA JATIM"
                    ),
                    isCutOff = false,
                    availablePaymentMethods = AvailablePaymentMethods(
                        kodeBayar = false,
                        qris = true,
                        va = true,
                        finpay = false
                    ),
                    masaPajak = MasaPajak(
                        tanggalJatuhTempoSebelumnya = data.identitas.tglMasaPajak,
                        tanggalBerlakuSampai = data.identitas.tglMasaPajak
                    )
                )
            )
        } else {
            JabarPajakResponse(
                status = false,
                message = jatimResponse.message ?: "Data tidak ditemukan",
                code = "404",
                data = null
            )
        }
    }
}

sealed class VehicleDetailUiState {
    object Loading : VehicleDetailUiState()
    data class NeedCaptcha(val captchaData: com.paondev.infoplat.data.api.JatimCaptchaResponse) : VehicleDetailUiState()
    data class Success(val data: JabarPajakResponse) : VehicleDetailUiState()
    data class Error(val message: String) : VehicleDetailUiState()
}
