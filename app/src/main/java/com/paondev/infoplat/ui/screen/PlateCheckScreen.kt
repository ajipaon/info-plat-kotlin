package com.paondev.infoplat.ui.screen

import android.util.Base64
import android.graphics.BitmapFactory
import androidx.compose.foundation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.hilt.navigation.compose.hiltViewModel
import com.paondev.infoplat.data.api.DiypPajakData
import com.paondev.infoplat.data.api.DiypPajakResponse
import com.paondev.infoplat.data.api.JabarPajakData
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.data.api.InfoPkbPnpb
import com.paondev.infoplat.data.api.InfoPembayaran
import com.paondev.infoplat.data.api.TaxDetail
import com.paondev.infoplat.data.api.PnpbDetail
import com.paondev.infoplat.data.api.InfoTransaksi
import com.paondev.infoplat.data.api.AvailablePaymentMethods
import com.paondev.infoplat.data.api.MasaPajak
import com.paondev.infoplat.data.api.JatimPkbResponse
import com.paondev.infoplat.data.repository.ProvinceRepository
import com.paondev.infoplat.ui.viewmodel.ProvinceViewModel
import com.paondev.infoplat.ui.viewmodel.SearchHistoryViewModel
import com.paondev.infoplat.navigation.VehicleDetailDestination
import kotlinx.coroutines.launch
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.gson.Gson
import com.paondev.infoplat.navigation.SearchHistoryDestination
import com.paondev.infoplat.ui.components.ProvinceSelectorCard
import com.paondev.infoplat.ui.theme.*

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
    var captchaCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    
    // Reset states when province changes
    LaunchedEffect(selectedProvince?.kode) {
        captchaCode = ""
        noRangka = ""
        errorMessage = null
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
                headPlat = headPlat,
                onHeadPlatChange = { headPlat = it },
                bodyPlat = bodyPlat,
                onBodyPlatChange = { bodyPlat = it },
                tailPlat = tailPlat,
                onTailPlatChange = { tailPlat = it },
                noRangka = noRangka,
                onNoRangkaChange = { noRangka = it },
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
                },
                onCheckClick = {
                    when (selectedProvince?.kode) {
                        "JTM" -> {
                            // Jatim: First generate captcha
                            if (headPlat.isNotEmpty() && bodyPlat.isNotEmpty() && tailPlat.isNotEmpty() && noRangka.length == 5) {
                                isLoading = true
                                coroutineScope.launch {
                                    viewModel.getJatimCaptcha()
                                    isLoading = false
                                }
                            } else {
                                errorMessage = if (noRangka.length != 5) {
                                    "No Rangka harus terdiri dari 5 digit"
                                } else {
                                    "Semua field harus diisi"
                                }
                            }
                        }
                        "JBR" -> {
                            // Jabar: Direct check
                            if (headPlat.isNotEmpty() && bodyPlat.isNotEmpty() && tailPlat.isNotEmpty()) {
                                isLoading = true
                                coroutineScope.launch {
                                    val result = viewModel.getVehicleInfo(
                                        provinceCode = "JBR",
                                        headPlat = headPlat,
                                        bodyPlat = bodyPlat,
                                        tailPlat = tailPlat
                                    )
                                    isLoading = false
                                    result.onSuccess { response ->
                                        navController.navigate(VehicleDetailDestination.createRoute(response))
                                    }.onFailure {
                                        errorMessage = it.message
                                    }
                                }
                            } else {
                                errorMessage = "Semua field harus diisi"
                            }
                        }
                        "DIY" -> {
                            // DIY: Direct check
                            if (headPlat.isNotEmpty() && bodyPlat.isNotEmpty() && tailPlat.isNotEmpty()) {
                                isLoading = true
                                coroutineScope.launch {
                                    val result = viewModel.getDiypVehicleInfo(
                                        provinceCode = "DIY",
                                        headPlat = headPlat,
                                        bodyPlat = bodyPlat,
                                        tailPlat = tailPlat
                                    )
                                    isLoading = false
                                    result.onSuccess { response ->
                                        // Convert DIY response to Jabar response format for navigation
                                        val jabarResponse = convertDiypToJabar(response)
                                        navController.navigate(VehicleDetailDestination.createRoute(jabarResponse))
                                    }.onFailure {
                                        errorMessage = it.message
                                    }
                                }
                            } else {
                                errorMessage = "Semua field harus diisi"
                            }
                        }
                        else -> {
                            errorMessage = "Provinsi ini belum didukung"
                        }
                    }
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
                            isLoading = false
                            result.onSuccess { response ->
                                if (response.status == "success") {
                                    // Convert Jatim response to Jabar response format for navigation
                                    val jabarResponse = convertJatimToJabar(response)
                                    navController.navigate(VehicleDetailDestination.createRoute(jabarResponse))
                                } else {
                                    errorMessage = response.message ?: "Gagal memverifikasi captcha"
                                }
                            }.onFailure {
                                errorMessage = it.message
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
    bodyPlat: String,
    onBodyPlatChange: (String) -> Unit,
    tailPlat: String,
    onTailPlatChange: (String) -> Unit,
    noRangka: String,
    onNoRangkaChange: (String) -> Unit,
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
                        headPlat = headPlat,
                        onHeadPlatChange = onHeadPlatChange,
                        bodyPlat = bodyPlat,
                        onBodyPlatChange = onBodyPlatChange,
                        tailPlat = tailPlat,
                        onTailPlatChange = onTailPlatChange
                    )

                    // Show No Rangka input for Jatim
                    if (isJatim) {
                        Spacer(modifier = Modifier.height(16.dp))
                        NoRangkaInput(
                            noRangka = noRangka,
                            onNoRangkaChange = onNoRangkaChange
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
                            if (isJatim) noRangka.length == 5 else true
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
fun LicensePlateInput(
    headPlat: String,
    onHeadPlatChange: (String) -> Unit,
    bodyPlat: String,
    onBodyPlatChange: (String) -> Unit,
    tailPlat: String,
    onTailPlatChange: (String) -> Unit
) {
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            PlateTextField(placeholder = "B", length = 2, modifier = Modifier.width(50.dp), defaultValue = headPlat, onValueChange = onHeadPlatChange)
            Text(
                "•",
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            PlateTextField(placeholder = "1234", length = 4, modifier = Modifier.width(90.dp), defaultValue = bodyPlat, onValueChange = onBodyPlatChange)
            Text(
                "•",
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            PlateTextField(placeholder = "XYZ", length = 3, modifier = Modifier.width(60.dp), defaultValue = tailPlat, onValueChange = onTailPlatChange)
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
fun PlateTextField(placeholder: String, length: Int, modifier: Modifier, defaultValue: String = "", onValueChange: (String) -> Unit = {}) {
    var text by remember { mutableStateOf(defaultValue) }
    BasicTextField(
        value = text,
        onValueChange = { 
            if (it.length <= length) {
                text = it.uppercase()
                onValueChange(it.uppercase())
            }
        },
        modifier = modifier,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.tertiary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.Center) {
                if (text.isEmpty()) {
                    Text(
                        placeholder,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                innerTextField()
            }
        }
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

// Helper function to decode base64 image string to ImageBitmap
fun decodeBase64ToImageBitmap(base64String: String): ImageBitmap? {
    return try {
        val dataUrlPrefix = "data:image/png;base64,"
        val base64Data = if (base64String.startsWith(dataUrlPrefix)) {
            base64String.substring(dataUrlPrefix.length)
        } else {
            base64String
        }
        
        val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        null
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