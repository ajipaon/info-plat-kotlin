package com.paondev.infoplat.ui.screen

import androidx.compose.foundation.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.paondev.infoplat.navigation.SearchHistoryDestination
import com.paondev.infoplat.ui.components.ProvinceSelectorCard
import com.paondev.infoplat.ui.theme.*

@Composable
fun PlateCheckScreen(
    navController: NavController,
    padding: PaddingValues = PaddingValues(0.dp)
) {
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
            PlateCheckHeroSection()
        }

        item {
            RecentSearchesHeader(
                toHistory = {
                    navController.navigate(SearchHistoryDestination.route)
                }
            )
        }

        items(recentSearchesRevised) { search ->
            RecentSearchCard(search)
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun PlateCheckHeroSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Cek pajak kendaraan",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = "Masukkan plat nomor kendaraan",
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
                LicensePlateInput()

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* TODO */ },
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
                    Icon(Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cek Pajak", fontWeight = FontWeight.Bold)
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
fun LicensePlateInput() {
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
            PlateTextField(placeholder = "B", length = 2, modifier = Modifier.width(50.dp))
            Text(
                "•",
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            PlateTextField(placeholder = "1234", length = 4, modifier = Modifier.width(90.dp))
            Text(
                "•",
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            PlateTextField(placeholder = "XYZ", length = 3, modifier = Modifier.width(60.dp))
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
fun PlateTextField(placeholder: String, length: Int, modifier: Modifier, defaultValue: String = "") {
    var text by remember { mutableStateOf(defaultValue) }
    BasicTextField(
        value = text,
        onValueChange = { if (it.length <= length) text = it.uppercase() },
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
    search: RecentSearch
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO */ }
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
    val imageUrl: String? = null
)

val recentSearchesRevised = listOf(
    RecentSearch(
        plate = "B 8992 GZ",
        carModel = "2018 Toyota Camry • Jakarta",
        status = VehicleStatus.CLEAN,
        statusLabel = "Paid",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCsVDYAuAYanw_gUz2Ygc5yiMY2CZlxEZGmU3KKJGJmprDEoLV8GrCrWVCcmipLdNfn2Gh9xbvlQx8wFye0l4Mxinnzqc3Uia-IJPF-VfEgvs167xhn0UBYkqREqhsGt7eGqFyW-m7JMMx0u2mF8CILDzHwJsk-aEHPXxYfrAFsVMGhFePbgqakaGNZEs4AMQ1u5-j3Q_yTxy_KvZcxj4ribhaoOz26L106RpqIitXU3y3kvcAQGZSig__WkYVlLokppazpCtxia1fx"
    ),
    RecentSearch(
        plate = "B 1010 LUV",
        carModel = "2021 Ford Mustang • Jakarta",
        status = VehicleStatus.STOLEN,
        statusLabel = "Blocked",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCRxHOlUzd6AUR6ZwuvdDNyv3fxy0UjjGLa56IDQKjWRu-ZmVbYMk-PD6Yfhp4hcUlr1iJ9lo28Ax41b0xEPTLXp3Pe9HVvrINxhB_xsUUd2pRTyvIYy5zkfvI5OAGjwEKVh9W8-kbtlEZj2FESYIOPDriMpTfm8QRIGhohDKlxlFRVa4eEuHHQtSL72RUsE3Nke6ujZo9sAfHJhSSwzaEL1Vw0rQmoOImPcM2TbmyZqGgNYqLKFRgTpIYwDR1wxyaEr34qTNAhdjRd"
    )
)

//@Preview(showBackground = false)
//@Composable
//fun PlateCheckScreenPreview() {
//    InfoPlatTheme {
//        PlateCheckScreen()
//    }
//}
