package com.paondev.infoplat.ui.screen

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.navigation.VehicleDetailDestination
import com.paondev.infoplat.ui.components.VehicleDetailSkeleton
import com.paondev.infoplat.ui.viewmodel.VehicleDetailUiState
import com.paondev.infoplat.ui.viewmodel.VehicleDetailViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

// Helper function to format currency
fun formatCurrency(amount: String): String {
    val number = amount.toLongOrNull() ?: 0L
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(number)
}

// Helper function to format date
fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}

@Composable
fun VehicleDetailScreen(
    navController: NavController,
    viewModel: VehicleDetailViewModel = hiltViewModel(),
    jabarPajakData: JabarPajakResponse? = null
) {
    val backStackEntry = navController.currentBackStackEntry
    val uiState by viewModel.uiState.collectAsState()
    
    // Parse parameters and fetch data
    LaunchedEffect(backStackEntry) {
        // First, check if pre-converted data is provided (backward compatibility)
        if (jabarPajakData != null) {
            // Use pre-converted data directly
            return@LaunchedEffect
        }
        
        // Check if encoded data is provided (backward compatibility)
        val encodedData = backStackEntry?.arguments?.getString("data")
        if (encodedData != null) {
            val decodedData = VehicleDetailDestination.parseData(encodedData)
            return@LaunchedEffect
        }
        
        // New flow: Parse plate parameters and fetch data
        val plateParams = VehicleDetailDestination.parsePlateParameters(
            provinceCode = backStackEntry?.arguments?.getString("provinceCode"),
            headPlat = backStackEntry?.arguments?.getString("headPlat"),
            bodyPlat = backStackEntry?.arguments?.getString("bodyPlat"),
            tailPlat = backStackEntry?.arguments?.getString("tailPlat"),
            noRangka = backStackEntry?.arguments?.getString("noRangka"),
            noNik = backStackEntry?.arguments?.getString("noNik")
        )
        
        if (plateParams != null) {
            viewModel.fetchVehicleData(
                provinceCode = plateParams.provinceCode,
                headPlat = plateParams.headPlat,
                bodyPlat = plateParams.bodyPlat,
                tailPlat = plateParams.tailPlat,
                noRangka = plateParams.noRangka,
                noNik = plateParams.noNik
            )
        }
    }
    
    // Use data from state
    val vehicleData = when (val state = uiState) {
        is VehicleDetailUiState.Success -> state.data.data
        else -> null
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState) {
            is VehicleDetailUiState.Loading -> {
                item {
                    VehicleDetailSkeleton()
                }
            }
            is VehicleDetailUiState.NeedCaptcha -> {
                item {
                    CaptchaInputScreen(
                        navController = navController,
                        viewModel = viewModel,
                        captchaData = (uiState as VehicleDetailUiState.NeedCaptcha).captchaData
                    )
                }
            }
            is VehicleDetailUiState.Error -> {
                item {
                    ErrorState(message = (uiState as VehicleDetailUiState.Error).message)
                }
            }
            is VehicleDetailUiState.Success -> {
                val data = (uiState as VehicleDetailUiState.Success).data.data
                if (data != null) {
                    item {
                        LicensePlateHeroDetail(data.noPolisi, data.infoPkbPnpb?.wilayah)
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        VehicleSpecificationCard(data)
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        TaxStatusCard(data)
                    }
                }
            }
        }

//        item {
//            Spacer(modifier = Modifier.height(24.dp))
//            Button(
//                onClick = { },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//                    .height(56.dp)
//                    .shadow(
//                        4.dp,
//                        RoundedCornerShape(12.dp),
//                        spotColor = MaterialTheme.colorScheme.tertiary
//                    ),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.tertiary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary
//                )
//            ) {
//                Icon(
//                    Icons.Outlined.History,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.onPrimary
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    "View Tax History",
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//        }
    }
}

@Composable
fun LicensePlateHeroDetail(noPolisi: String?, wilayah: String?) {
    val plateNumber = noPolisi ?: "B 1234 XYZ"
    val regionName = wilayah ?: "JAKARTA TIMUR"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(280.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "12 • 25",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        letterSpacing = 2.sp
                    )
                )
            }

            Text(
                plateNumber,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
            )

            Text(
                regionName.uppercase(),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }
    }
}

@Composable
fun VehicleSpecificationCard(data: com.paondev.infoplat.data.api.JabarPajakData?) {
    val brandModel = if (data != null) "${data.namaMerk} ${data.namaModel}" else "Toyota Avanza"
    val year = data?.tahunBuatan ?: "2020"
    val color = data?.warna ?: "Silver Metallic"
    val vehicleType = data?.jenis ?: "Roda 4"
    val ownership = when(data?.milikKe) {
        "1" -> "First (Pribadi)"
        "2" -> "Second (Pribadi)"
        "3" -> "Third (Pribadi)"
        else -> "Unknown"
    }
    
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    "Vehicle Specification",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(130.dp)
//                    .clip(RoundedCornerShape(12.dp))
//            ) {
//
//                AsyncImage(
//                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuC6a-hsGVO93ckXCf3r-e1jfNPmoLxH9vVgIs3y9g-s7t1aRe5L2h-liuqI1Art5bmngzGK36GbvLUep_piTqp8lYkHHhuIXC--gzhzLiiCQqj6D2ppJSbtP0Jv6nn5U-1Hjic2LjnJ9v9DxSnQY8K1CL9mVWB7imYOTJ0DOeWDIWk4D3cx-kLuwdWWoqT_e69VTcd89h2-y-YTeydQ3VJdmEc_mNcmrHj7Pw_GVHwk1aPLV_QMDD74yr4lbWI5F4UFgGL_OGapSG0x",
//                    contentDescription = null,
//                    modifier = Modifier.fillMaxSize(),
//                    contentScale = ContentScale.Crop
//                )
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(
//                            Brush.verticalGradient(
//                                listOf(
//                                    Color.Transparent,
//                                    Color.Black.copy(alpha = 0.7f)
//                                )
//                            )
//                        )
//                )
//
//                Text(
//                    "Toyota Avanza 1.3 G",
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 14.sp,
//                    modifier = Modifier
//                        .align(Alignment.BottomStart)
//                        .padding(12.dp)
//                )
//            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                Row(Modifier.fillMaxWidth()) {
                    SpecItem(
                        "Brand / Model",
                        brandModel,
                        Modifier.weight(1f)
                    )
                    SpecItem(
                        "Manufacturing Year",
                        year,
                        Modifier.weight(1f)
                    )
                }

                Row(Modifier.fillMaxWidth()) {
                    SpecColorItem(
                        "Color",
                        color.uppercase(),
                        Color(0xFFC0C0C0),
                        Modifier.weight(1f)
                    )
                    SpecItem(
                        "Vehicle Type",
                        vehicleType,
                        Modifier.weight(1f)
                    )
                }

                Row(Modifier.fillMaxWidth()) {
                    SpecItem(
                        "Fuel Type",
                        "Gasoline (Bensin)",
                        Modifier.weight(1f)
                    )
                    SpecItem(
                        "Ownership",
                        ownership,
                        Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun SpecItem(
    label: String,
    value: String,
    modifier: Modifier
) {
    Column(modifier) {
        Text(
            label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun SpecColorItem(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier
) {
    Column(modifier) {

        Text(
            label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun TaxStatusCard(data: com.paondev.infoplat.data.api.JabarPajakData?) {
    val stnkExpiry = data?.infoPkbPnpb?.tanggalStnk?.let { formatDate(it) } ?: "15 June 2025"
    val taxYear = if (data?.masaPajak?.tanggalBerlakuSampai != null && data.masaPajak.tanggalBerlakuSampai.length >= 4) {
        data.masaPajak.tanggalBerlakuSampai.substring(0, 4)
    } else {
        "2024"
    }
    val statusText = data?.keterangan ?: "UNKNOWN"
    val isPaid = data?.canBePaid == false
    val statusColor = if (isPaid) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
    
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(statusColor.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ReceiptLong,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        "Tax Status",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = statusColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        statusText,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(
                        "STNK Expiry Date",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        stnkExpiry,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Tax Year",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        taxYear,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val pembayaran = data?.infoPembayaran
                
                TaxBreakdownItem(
                    "PKB (Vehicle Tax)",
                    formatCurrency(pembayaran?.pkb?.pokok ?: "0"),
                    false
                )
                TaxBreakdownItem(
                    "Opsen (Admin)",
                    formatCurrency(pembayaran?.opsen?.pokok ?: "0"),
                    false
                )
                TaxBreakdownItem(
                    "SWDKLLJ (Jasa Raharja)",
                    formatCurrency(pembayaran?.swdkllj?.pokok ?: "0"),
                    false
                )

                val pnpbStnk = pembayaran?.pnpb?.stnk?.toLongOrNull() ?: 0L
                val pnpbTnkb = pembayaran?.pnpb?.tnkb?.toLongOrNull() ?: 0L
                val totalPnpb = pnpbStnk + pnpbTnkb
                
                if (totalPnpb > 0) {
                    TaxBreakdownItem(
                        "PNPB (Admin)",
                        formatCurrency(totalPnpb.toString()),
                        false
                    )
                }

                Divider(
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                TaxBreakdownItem(
                    "Total Tax",
                    formatCurrency(pembayaran?.jumlah ?: "0"),
                    true
                )
            }
        }
    }
}

@Composable
fun TaxBreakdownItem(
    label: String,
    value: String,
    isTotal: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            label,
            fontSize = if (isTotal) 16.sp else 14.sp,
            color = if (isTotal)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )

        Text(
            value,
            fontSize = if (isTotal) 16.sp else 14.sp,
            color = if (isTotal)
                MaterialTheme.colorScheme.tertiary
            else
                MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }
}


// Helper function to decode base64 image
fun decodeBase64ToImageBitmap(base64String: String): androidx.compose.ui.graphics.ImageBitmap? {
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
fun CaptchaInputScreen(
    navController: NavController,
    viewModel: VehicleDetailViewModel,
    captchaData: com.paondev.infoplat.data.api.JatimCaptchaResponse
) {
    var captchaCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isLoading by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Verifikasi CAPTCHA",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                
                Text(
                    "Masukkan kode yang terlihat pada gambar",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // CAPTCHA Image
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val bitmap = decodeBase64ToImageBitmap(captchaData.image ?: "")
                        if (bitmap != null) {
                            androidx.compose.foundation.Image(
                                bitmap = bitmap,
                                contentDescription = "CAPTCHA",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Fit
                            )
                        } else {
                            Text(
                                "Gagal memuat gambar CAPTCHA",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Captcha Input
                Column {
                    Text(
                        "Kode CAPTCHA",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.foundation.text.BasicTextField(
                        value = captchaCode,
                        onValueChange = { 
                            captchaCode = it.uppercase()
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .border(
                                2.dp,
                                if (errorMessage != null)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.tertiary,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            letterSpacing = 2.sp
                        ),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters
                        ),
                        // Wrap suspend function call in coroutine
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.tertiary),
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
                
                // Error message
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        errorMessage ?: "",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Verify Button
                Button(
                    onClick = {
                        if (captchaCode.isNotEmpty()) {
                            // Get plate parameters from current navigation (we're in VehicleDetail screen)
                            val backStackEntry = navController.currentBackStackEntry
                            val plateParams = backStackEntry?.arguments?.let {
                                com.paondev.infoplat.navigation.VehicleDetailDestination.parsePlateParameters(
                                    provinceCode = it.getString("provinceCode"),
                                    headPlat = it.getString("headPlat"),
                                    bodyPlat = it.getString("bodyPlat"),
                                    tailPlat = it.getString("tailPlat"),
                                    noRangka = it.getString("noRangka"),
                                    noNik = it.getString("noNik")
                                )
                            }
                            
                            if (plateParams != null) {
                                val nopol = "${plateParams.headPlat}${plateParams.bodyPlat}${plateParams.tailPlat}".lowercase()
                                // Launch coroutine to call suspend function
                                coroutineScope.launch {
                                    viewModel.verifyJatimCaptcha(
                                        sessionId = captchaData.sessionId,
                                        captchaCode = captchaCode,
                                        nopol = nopol,
                                        noRangka = plateParams.noRangka
                                    )
                                }
                            }
                        } else {
                            errorMessage = "Kode captcha harus diisi"
                        }
                    },
                    enabled = captchaCode.isNotEmpty() && isLoading !is VehicleDetailUiState.Loading,
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
                    if (isLoading is VehicleDetailUiState.Loading) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Verifikasi",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // Back Button
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Kembali")
                }
            }
        }
    }
}

@Composable
fun ErrorState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Text(
                message,
                style = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun VehicleDetailScreenPreview() {
//    InfoPlatTheme {
//        VehicleDetailScreen()
//    }
//}
