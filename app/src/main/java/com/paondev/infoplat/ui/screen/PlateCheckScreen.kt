package com.paondev.infoplat.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.NoPhotography
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.paondev.infoplat.data.api.AvailablePaymentMethods
import com.paondev.infoplat.data.api.BaliPajakResponse
import com.paondev.infoplat.data.api.BangkaBelitungPajakResponse
import com.paondev.infoplat.data.api.BantenPajakResponse
import com.paondev.infoplat.data.api.DiypPajakResponse
import com.paondev.infoplat.data.api.InfoPembayaran
import com.paondev.infoplat.data.api.InfoPkbPnpb
import com.paondev.infoplat.data.api.InfoTransaksi
import com.paondev.infoplat.data.api.JabarPajakData
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.data.api.LampungPajakResponse
import com.paondev.infoplat.data.api.MasaPajak
import com.paondev.infoplat.data.api.PnpbDetail
import com.paondev.infoplat.data.api.RiauPajakResponse
import com.paondev.infoplat.data.api.SumbarPajakResponse
import com.paondev.infoplat.data.api.TaxDetail
import com.paondev.infoplat.navigation.SearchHistoryDestination
import com.paondev.infoplat.navigation.VehicleDetailDestination
import com.paondev.infoplat.ui.components.LicensePlateInput
import com.paondev.infoplat.ui.components.ProvinceSelectorCard
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
    selectedProvince: com.paondev.infoplat.data.Province?,
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

//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    HorizontalDivider(
//                        modifier = Modifier.weight(1f),
//                        thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
//                    )
//                    Text(
//                        text = "OR USE CAMERA",
//                        style = TextStyle(
//                            fontSize = 10.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
//                            letterSpacing = 1.sp
//                        ),
//                        modifier = Modifier.padding(horizontal = 16.dp)
//                    )
//                    HorizontalDivider(
//                        modifier = Modifier.weight(1f),
//                        thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
//                    )
//                }

//                OutlinedButton(
//                    onClick = { /* TODO */ },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = ButtonDefaults.outlinedButtonColors(
//                        contentColor = MaterialTheme.colorScheme.tertiary
//                    ),
//                    border = ButtonDefaults.outlinedButtonBorder(
//                        enabled = true,
////                        shape = RoundedCornerShape(12.dp)
//                    ).copy(
//                        brush = SolidColor(MaterialTheme.colorScheme.tertiary)
//                    )
//                ) {
//                    Icon(
//                        Icons.Default.PhotoCamera,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.tertiary
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        "Scan Plate",
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.tertiary
//                    )
//                }
            }
        }
    }
}


@Composable
fun Bolt(modifier: Modifier) {
    Box(
        modifier = modifier
            .size(6.dp)
            .background(MaterialTheme.colorScheme.tertiary, CircleShape)
            .shadow(1.dp, CircleShape)
    )
}

@Composable
fun NoRangkaInput(
    noRangka: String,
    onNoRangkaChange: (String) -> Unit
) {
    Column {
        Text(
            text = "No Rangka (5 digit terakhir)",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = noRangka,
            onValueChange = { 
                if (it.length <= 5 && it.all { char -> char.isDigit() }) {
                    onNoRangkaChange(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (noRangka.isEmpty()) {
                        Text(
                            "12345",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun NoNikInput(
    noNik: String,
    onNoNikChange: (String) -> Unit
) {
    Column {
        Text(
            text = "NIK (16 digit)",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = noNik,
            onValueChange = { 
                if (it.length <= 16 && it.all { char -> char.isDigit() }) {
                    onNoNikChange(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (noNik.isEmpty()) {
                        Text(
                            "Masukkan 16 digit NIK",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun CaptchaSection(
    captchaImage: String?,
    captchaCode: String,
    onCaptchaCodeChange: (String) -> Unit,
    isLoading: Boolean,
    onRefreshCaptcha: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // CAPTCHA Image
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else if (captchaImage != null) {
                    val bitmap = decodeBase64ToImageBitmap(captchaImage)
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = "CAPTCHA",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text(
                            "Gagal memuat gambar CAPTCHA",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Text(
                        "Gagal memuat CAPTCHA",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Refresh CAPTCHA button
        TextButton(
            onClick = onRefreshCaptcha,
            enabled = !isLoading
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Refresh CAPTCHA",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Refresh CAPTCHA",
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CAPTCHA Input
        Column {
            Text(
                text = "Kode CAPTCHA",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = captchaCode,
                onValueChange = onCaptchaCodeChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    letterSpacing = 2.sp
                ),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (captchaCode.isEmpty()) {
                            Text(
                                "Masukkan kode",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

// Helper function to convert Banten response to Jabar response format
fun convertBantenToJabar(bantenResponse: BantenPajakResponse): JabarPajakResponse {
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

// Helper function to convert DIY response to Jabar response format
fun convertDiypToJabar(diypResponse: DiypPajakResponse): JabarPajakResponse {
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

// Helper function to convert Bali response to Jabar response format
fun convertBaliToJabar(baliResponse: BaliPajakResponse): JabarPajakResponse {
    val data = baliResponse.data
    return if (baliResponse.success && data != null) {
        val detail = data.detail
        val pembayaranList = data.pembayaran

        // Extract payment details from the list
        val pkbItem = pembayaranList.find { it.jenis == "PKB" }
        val swdklljItem = pembayaranList.find { it.jenis == "SWDKLLJ" }
        val totalItem = pembayaranList.find { it.jenis == "TOTAL" }

        val pkbValue = pkbItem?.jumlah ?: "0"
        val swdklljValue = swdklljItem?.jumlah ?: "0"
        val totalValue = totalItem?.jumlah ?: "0"

        // Parse masaBerlaku to extract dates (format: "DD Month YYYY / DD Month YYYY")
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

// Helper function to convert Bangka Belitung response to Jabar response format
fun convertBangkaBelitungToJabar(bangkaBelitungResponse: BangkaBelitungPajakResponse): JabarPajakResponse {
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

// Helper function to convert Lampung response to Jabar response format
fun convertLampungToJabar(lampungResponse: LampungPajakResponse): JabarPajakResponse {
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
                noPolisi = "BE${data.kendaraanKe}XX", // Will be replaced with actual plate number
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

// Helper function to convert Sumbar response to Jabar response format
fun convertSumbarToJabar(sumbarResponse: SumbarPajakResponse): JabarPajakResponse {
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

// Helper function to convert Riau response to Jabar response format
fun convertRiauToJabar(riauResponse: RiauPajakResponse): JabarPajakResponse {
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

@Composable
fun RecentSearchesHeader(
    toHistory: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Recent Searches",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Text(
            text = "View All",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.clickable { toHistory() }
        )
    }
}

@Composable
fun RecentSearchCard(
    search: RecentSearch,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Car Image
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (search.imageUrl != null) {
                    AsyncImage(
                        model = search.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.NoPhotography,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = search.plate,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.background,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(search.statusLabel, search.status)
                }
                Text(
                    text = search.carModel,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun StatusBadge(label: String, status: VehicleStatus) {
    val (bgColor, textColor) = when (status) {
        VehicleStatus.CLEAN -> MaterialTheme.colorScheme.tertiary to MaterialTheme.colorScheme.onPrimary
        VehicleStatus.STOLEN -> Color.Red to Color.White
        VehicleStatus.TAX_DUE -> Color(0xFFFFA500) to Color.White
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = bgColor,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                letterSpacing = 0.5.sp
            ),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun NavItemPlateCheck(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected)
                MaterialTheme.colorScheme.tertiary
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected)
                    MaterialTheme.colorScheme.tertiary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )
    }
}

// Data Models
enum class VehicleStatus { CLEAN, STOLEN, TAX_DUE }

data class RecentSearch(
    val plate: String,
    val carModel: String,
    val status: VehicleStatus,
    val statusLabel: String,
    val data: String = "",
    val imageUrl: String? = null
)

//@Preview(showBackground = false)
//@Composable
//fun PlateCheckScreenPreview() {
//    InfoPlatTheme {
//        PlateCheckScreen()
//    }
//}