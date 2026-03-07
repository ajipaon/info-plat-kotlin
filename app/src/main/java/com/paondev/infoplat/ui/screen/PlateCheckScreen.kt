package com.paondev.infoplat.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.paondev.infoplat.data.Province
import com.paondev.infoplat.data.api.AvailablePaymentMethods
import com.paondev.infoplat.data.api.InfoPembayaran
import com.paondev.infoplat.data.api.InfoPkbPnpb
import com.paondev.infoplat.data.api.InfoTransaksi
import com.paondev.infoplat.data.api.JabarPajakData
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.data.api.MasaPajak
import com.paondev.infoplat.data.api.PnpbDetail
import com.paondev.infoplat.data.api.TaxDetail
import com.paondev.infoplat.navigation.SearchHistoryDestination
import com.paondev.infoplat.navigation.VehicleDetailDestination
import com.paondev.infoplat.ui.components.CaptchaSection
import com.paondev.infoplat.ui.components.LicensePlateInput
import com.paondev.infoplat.ui.components.NoNikInput
import com.paondev.infoplat.ui.components.NoRangkaInput
import com.paondev.infoplat.ui.components.ProvinceSelectorCard
import com.paondev.infoplat.ui.components.RecentSearchCard
import com.paondev.infoplat.ui.components.RecentSearchesHeader
import com.paondev.infoplat.ui.viewmodel.ProvinceViewModel
import com.paondev.infoplat.ui.viewmodel.SearchHistoryViewModel
import kotlinx.coroutines.launch

@Composable
fun PlateCheckScreen(
    navController: NavController,
    padding: PaddingValues = PaddingValues(0.dp),
    viewModel: ProvinceViewModel = hiltViewModel(),
    searchHistoryViewModel: SearchHistoryViewModel = hiltViewModel()
) {
    val selectedProvince by viewModel.selectedProvince.collectAsState()
    val recentHistory by searchHistoryViewModel.recentHistory.collectAsState()
    val captchaData by viewModel.captchaData.collectAsState()
    val captchaError by viewModel.captchaError.collectAsState()
    val isCaptchaLoading by viewModel.isCaptchaLoading.collectAsState()
    
    var headPlat by remember { mutableStateOf("") }
    var bodyPlat by remember { mutableStateOf("") }
    var tailPlat by remember { mutableStateOf("") }
    var noRangka by remember { mutableStateOf("") }
    var noNik by remember { mutableStateOf("") }
    var captchaCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedPlateCode by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Reset states when province changes
    LaunchedEffect(selectedProvince) {
        captchaCode = ""
        noRangka = ""
        noNik = ""
        errorMessage = null
        headPlat = ""
        selectedPlateCode = ""

        // Auto-fill headPlat for single plate provinces
        if (selectedProvince?.plateCodes?.size == 1) {
            headPlat = selectedProvince!!.plateCodes[0]
            selectedPlateCode = selectedProvince!!.plateCodes[0]
        }
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            ProvinceSelectorCard()
        }

        item {
            PlateCheckHeroSection(
                selectedProvince = selectedProvince,
                headPlat = headPlat ,
                onHeadPlatChange = { headPlat = it },
                selectedPlateCode = selectedPlateCode,
                onSelectedPlateCodeChange = { selectedPlateCode = it },
                bodyPlat = bodyPlat,
                onBodyPlatChange = { bodyPlat = it },
                tailPlat = tailPlat,
                onTailPlatChange = { tailPlat = it },
                noRangka = noRangka,
                onNoRangkaChange = { noRangka = it },
                noNik = noNik,
                onNoNikChange = { noNik = it },
                captchaData = captchaData,
                captchaCode = captchaCode,
                onCaptchaCodeChange = { captchaCode = it },
                isLoading = isLoading,
                isCaptchaLoading = isCaptchaLoading,
                errorMessage = errorMessage,
                captchaError = captchaError,
                onClearError = { 
                    errorMessage = null
                    viewModel.clearCaptchaError()
                    viewModel.clearCaptchaData()
                },
                onCheckClick = {
                    // Navigate directly to VehicleDetail with plate parameters
                    // Data fetching will be done in VehicleDetailViewModel
                    val provinceCode = selectedProvince?.kode
                    
                    if (provinceCode == null) {
                        errorMessage = "Silakan pilih provinsi terlebih dahulu"
                        return@PlateCheckHeroSection
                    }
                    
                    if (headPlat.isEmpty() || bodyPlat.isEmpty() || tailPlat.isEmpty()) {
                        errorMessage = "Semua field plat harus diisi"
                        return@PlateCheckHeroSection
                    }
                    
//                    val isJatim = provinceCode == "JTM"
                    val needsNoRangka = selectedProvince?.withNoRangka == true
                    val needsNoNik = selectedProvince?.withNik == true
                    
                    if (needsNoRangka && noRangka.length != 5) {
                        errorMessage = "No Rangka harus terdiri dari 5 digit"
                        return@PlateCheckHeroSection
                    }
                    
                    if (needsNoNik && noNik.length != 16) {
                        errorMessage = "NIK harus terdiri dari 16 digit"
                        return@PlateCheckHeroSection
                    }
                    
                    // Navigate to VehicleDetail with parameters
                    navController.navigate(
                        VehicleDetailDestination.createRoute(
                            provinceCode = provinceCode,
                            headPlat = headPlat,
                            bodyPlat = bodyPlat,
                            tailPlat = tailPlat,
                            noRangka = noRangka,
                            noNik = noNik
                        )
                    )
                },
                onVerifyCaptchaClick = {
                    if (captchaCode.isNotEmpty()) {
                        isLoading = true
                        coroutineScope.launch {
                            val nopol = "$headPlat$bodyPlat$tailPlat".lowercase()
                            val result = viewModel.getJatimVehicleInfo(
                                sessionId = captchaData?.sessionId ?: "",
                                captchaCode = captchaCode,
                                nopol = nopol,
                                norang = noRangka
                            )
                            result.onSuccess { response ->
                                if (response.status == "success") {
                                    // Convert data here to avoid delay
                                    val convertedData = convertJatimToJabar(response)
                                    navController.navigate(VehicleDetailDestination.createRoute(convertedData))
                                } else {
                                    errorMessage = response.message ?: "Gagal memverifikasi captcha"
                                }
                                isLoading = false
                            }.onFailure {
                                errorMessage = it.message
                                isLoading = false
                            }
                        }
                    } else {
                        errorMessage = "Kode captcha harus diisi"
                    }
                },
                onRefreshCaptcha = {
                    coroutineScope.launch {
                        viewModel.getJatimCaptcha()
                    }
                }
            )
        }

        if (recentHistory.isNotEmpty()) {
            item {
                RecentSearchesHeader(
                    toHistory = {
                        navController.navigate(SearchHistoryDestination.route)
                    }
                )
            }

            items(recentHistory) { search ->
                RecentSearchCard(
                    search = search,
                    onClick = {
                        try {
                            if (search.data.isNotEmpty()) {
                                val response = Gson().fromJson(search.data, JabarPajakResponse::class.java)
                                navController.navigate(VehicleDetailDestination.createRoute(response))
                            }
                        } catch (e: Exception) {
                            // Handle error silently
                        }
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun PlateCheckHeroSection(
    selectedProvince: Province?,
    headPlat: String,
    onHeadPlatChange: (String) -> Unit,
    selectedPlateCode: String,
    onSelectedPlateCodeChange: (String) -> Unit,
    bodyPlat: String,
    onBodyPlatChange: (String) -> Unit,
    tailPlat: String,
    onTailPlatChange: (String) -> Unit,
    noRangka: String,
    onNoRangkaChange: (String) -> Unit,
    noNik: String,
    onNoNikChange: (String) -> Unit,
    captchaData: com.paondev.infoplat.data.api.JatimCaptchaResponse?,
    captchaCode: String,
    onCaptchaCodeChange: (String) -> Unit,
    isLoading: Boolean,
    isCaptchaLoading: Boolean,
    errorMessage: String?,
    captchaError: String?,
    onClearError: () -> Unit,
    onCheckClick: () -> Unit,
    onVerifyCaptchaClick: () -> Unit,
    onRefreshCaptcha: () -> Unit
) {
    val isJatim = selectedProvince?.kode == "JTM"
    val showCaptcha = isJatim && captchaData != null
    val showNoRangka = selectedProvince?.withNoRangka == true
    val showNoNik = selectedProvince?.withNik == true
    val isSinglePlate = selectedProvince?.plateCodes?.size == 1
    val isMultiplePlate = selectedProvince?.plateCodes?.size ?: 0 > 1

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = if (showCaptcha) "Verifikasi CAPTCHA" else "Cek pajak kendaraan",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = if (showCaptcha) "Masukkan kode yang terlihat pada gambar" else "Masukkan plat nomor kendaraan",
            style = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp, start = 16.dp, end = 16.dp)
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                if (showCaptcha) {
                    // Show CAPTCHA for Jatim
                    CaptchaSection(
                        captchaImage = captchaData?.image,
                        captchaCode = captchaCode,
                        onCaptchaCodeChange = onCaptchaCodeChange,
                        isLoading = isCaptchaLoading,
                        onRefreshCaptcha = onRefreshCaptcha
                    )
                } else {
                    // Show license plate input
                    LicensePlateInput(
                        isSinglePlate = isSinglePlate,
                        isMultiplePlate = isMultiplePlate,
                        headPlat = headPlat,
                        onHeadPlatChange = onHeadPlatChange,
                        plateCodes = selectedProvince?.plateCodes ?: emptyList(),
                        selectedPlateCode = selectedPlateCode,
                        onSelectedPlateCodeChange = onSelectedPlateCodeChange,
                        bodyPlat = bodyPlat,
                        onBodyPlatChange = onBodyPlatChange,
                        tailPlat = tailPlat,
                        onTailPlatChange = onTailPlatChange
                    )

                    // Show No Rangka input if required
                    if (showNoRangka) {
                        Spacer(modifier = Modifier.height(16.dp))
                        NoRangkaInput(
                            noRangka = noRangka,
                            onNoRangkaChange = onNoRangkaChange
                        )
                    }

                    // Show NIK input if required
                    if (showNoNik) {
                        Spacer(modifier = Modifier.height(16.dp))
                        NoNikInput(
                            noNik = noNik,
                            onNoNikChange = onNoNikChange
                        )
                    }
                }

                // Error message
                if (errorMessage != null || captchaError != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = errorMessage ?: captchaError ?: "",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = if (showCaptcha) onVerifyCaptchaClick else onCheckClick,
                    enabled = !isLoading && !isCaptchaLoading && (
                        if (showCaptcha) {
                            captchaCode.isNotEmpty()
                        } else {
                            headPlat.isNotEmpty() && bodyPlat.isNotEmpty() && tailPlat.isNotEmpty() &&
                            if (showNoRangka) noRangka.length == 5 else true &&
                            if (showNoNik) noNik.length == 16 else true
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            4.dp,
                            RoundedCornerShape(12.dp),
                            spotColor = MaterialTheme.colorScheme.tertiary
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (isLoading || isCaptchaLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            if (showCaptcha) Icons.Default.Check else Icons.Default.Search,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (showCaptcha) "Verifikasi" else "Cek Pajak",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // Back button when showing captcha
                if (showCaptcha) {
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(
                        onClick = onClearError,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Kembali")
                    }
                }
                // component button scam camera
//                ScanPlate()
            }
        }
    }
}
// Helper function to convert Jatim response to Jabar response format
fun convertJatimToJabar(jatimResponse: com.paondev.infoplat.data.api.JatimPkbResponse): JabarPajakResponse {
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

//@Preview(showBackground = false)
//@Composable
//fun PlateCheckScreenPreview() {
//    InfoPlatTheme {
//        PlateCheckScreen()
//    }
//}