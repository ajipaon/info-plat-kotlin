package com.paondev.infoplat.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.paondev.infoplat.ui.theme.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material3.HorizontalDivider
import androidx.navigation.NavController
import com.paondev.infoplat.data.api.JabarPajakResponse
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Currency

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
    jabarPajakData: JabarPajakResponse? = null
) {
    val data = jabarPajakData?.data
    
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            LicensePlateHeroDetail(data?.noPolisi, data?.infoPkbPnpb?.wilayah)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            VehicleSpecificationCard(data)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            TaxStatusCard(data)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
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
                Icon(
                    Icons.Outlined.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "View Tax History",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
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
    val taxYear = data?.masaPajak?.tanggalBerlakuSampai?.substring(0, 4) ?: "2024"
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


//@Preview(showBackground = true)
//@Composable
//fun VehicleDetailScreenPreview() {
//    InfoPlatTheme {
//        VehicleDetailScreen()
//    }
//}