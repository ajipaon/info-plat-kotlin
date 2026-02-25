package com.paondev.infoplat.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.paondev.infoplat.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlateCheckScreen() {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) BackgroundDark else BackgroundLight
    val surfaceColor = if (isDark) Slate800 else Color.White
    val textColor = if (isDark) Color.White else Slate900
    val subTextColor = if (isDark) Slate400 else Slate500
    val borderColor = if (isDark) Slate700 else Slate200

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Primary.copy(alpha = 0.1f),
                            modifier = Modifier.padding(end = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Text(
                            text = "Plate Check",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = textColor
                            )
                        )
                    }
                },
                actions = {
                    Box(modifier = Modifier.padding(end = 8.dp)) {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = subTextColor
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 10.dp, end = 10.dp)
                                .size(10.dp)
                                .background(Color.Red, CircleShape)
                                .border(2.dp, bgColor, CircleShape)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor.copy(alpha = 0.8f)
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            BottomNavigationBar(isDark)
        },
        containerColor = bgColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                ProvinceSelectorCard(surfaceColor, borderColor, textColor, subTextColor)
            }

            item {
                HeroSection(textColor, subTextColor, surfaceColor, borderColor, isDark)
            }

            item {
                RecentSearchesHeader(textColor)
            }

            items(recentSearchesRevised) { search ->
                RecentSearchCard(search, surfaceColor, borderColor, textColor, subTextColor)
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun ProvinceSelectorCard(surfaceColor: Color, borderColor: Color, textColor: Color, subTextColor: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Primary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "SEARCHING IN",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = subTextColor,
                        letterSpacing = 0.5.sp
                    )
                )
                Text(
                    text = "DKI Jakarta",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Primary.copy(alpha = 0.05f),
                modifier = Modifier.clickable { /* TODO */ }
            ) {
                Text(
                    text = "Change",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary
                    )
                )
            }
        }
    }
}

@Composable
fun HeroSection(
    textColor: Color,
    subTextColor: Color,
    surfaceColor: Color,
    borderColor: Color,
    isDark: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Check Vehicle Details",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor,
                textAlign = TextAlign.Center
            )
        )
        Text(
            text = "Enter the license plate number for the selected province below.",
            style = TextStyle(
                fontSize = 14.sp,
                color = subTextColor,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp, start = 16.dp, end = 16.dp)
        )

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = surfaceColor,
            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                LicensePlateInputRevised(isDark)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = Primary.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Check Tax Status", fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(modifier = Modifier.weight(1f), color = if (isDark) Slate700 else Slate100)
                    Text(
                        text = "OR USE CAMERA",
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400,
                            letterSpacing = 1.sp
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Divider(modifier = Modifier.weight(1f), color = if (isDark) Slate700 else Slate100)
                }

                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) Slate600 else Slate200),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isDark) Slate700.copy(alpha = 0.5f) else Slate50,
                        contentColor = if (isDark) Slate200 else Slate700
                    )
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Scan Plate", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun LicensePlateInputRevised(isDark: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Slate900)
            .border(4.dp, Slate800, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        // ID Strip
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(32.dp)
                .background(Primary)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "ID",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.size(width = 16.dp, height = 2.dp).background(Color.White.copy(alpha = 0.4f)))
            }
        }

        // Bolts
        BoltRevised(Modifier.align(Alignment.TopStart).padding(top = 8.dp, start = 40.dp))
        BoltRevised(Modifier.align(Alignment.TopEnd).padding(top = 8.dp, end = 8.dp))
        BoltRevised(Modifier.align(Alignment.BottomStart).padding(bottom = 8.dp, start = 40.dp))
        BoltRevised(Modifier.align(Alignment.BottomEnd).padding(bottom = 8.dp, end = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(start = 32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .border(2.dp, Slate900, RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            PlateTextField(placeholder = "B", length = 2, modifier = Modifier.width(50.dp), defaultValue = "B")
            Text("•", color = Slate300, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            PlateTextField(placeholder = "1234", length = 4, modifier = Modifier.width(90.dp))
            Text("•", color = Slate300, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            PlateTextField(placeholder = "XYZ", length = 3, modifier = Modifier.width(60.dp))
        }
    }
}

@Composable
fun BoltRevised(modifier: Modifier) {
    Box(
        modifier = modifier
            .size(6.dp)
            .background(Slate300, CircleShape)
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
            color = Slate900,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.Center) {
                if (text.isEmpty()) {
                    Text(
                        placeholder,
                        color = Slate200,
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
fun RecentSearchesHeader(textColor: Color) {
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
                color = textColor
            )
        )
        Text(
            text = "View All",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Primary
            ),
            modifier = Modifier.clickable { /* TODO */ }
        )
    }
}

@Composable
fun RecentSearchCard(
    search: RecentSearch,
    surfaceColor: Color,
    borderColor: Color,
    textColor: Color,
    subTextColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = surfaceColor,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
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
                    .background(Slate100),
                contentAlignment = Alignment.Center
            ) {
                if (search.imageUrl != null) {
                    AsyncImage(
                        model = search.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Cover
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.NoPhotography,
                        contentDescription = null,
                        tint = Slate400,
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
                            .background(if (isSystemInDarkTheme()) Slate900 else Slate100, RoundedCornerShape(4.dp))
                            .border(1.dp, if (isSystemInDarkTheme()) Slate700 else Slate200, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = textColor
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadgeRevised(search.statusLabel, search.status)
                }
                Text(
                    text = search.carModel,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = subTextColor
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Slate400
            )
        }
    }
}

@Composable
fun StatusBadgeRevised(label: String, status: VehicleStatus) {
    val (bgColor, textColor) = when (status) {
        VehicleStatus.CLEAN -> StatusGreenBg to StatusGreen
        VehicleStatus.STOLEN -> StatusRedBg to StatusRed
        VehicleStatus.TAX_DUE -> StatusYellowBg to StatusYellow
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
fun BottomNavigationBar(isDark: Boolean) {
    val navColor = if (isDark) Slate900 else Color.White
    val borderColor = if (isDark) Slate800 else Slate200

    Surface(
        color = navColor,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(0.dp))
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(Icons.Filled.Home, "Home", true)
            NavItem(Icons.Default.History, "History", false)
            
            // Scan Button
            Box(
                modifier = Modifier
                    .offset(y = (-24).dp)
                    .size(56.dp)
                    .shadow(8.dp, CircleShape, spotColor = Primary.copy(alpha = 0.4f))
                    .background(Primary, CircleShape)
                    .border(4.dp, navColor, CircleShape)
                    .clickable { /* TODO */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CenterFocusStrong,
                    contentDescription = "Scan",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            NavItem(Icons.Default.Favorite, "Saved", false)
            NavItem(Icons.Default.Settings, "Settings", false)
        }
    }
}

@Composable
fun NavItem(icon: ImageVector, label: String, isSelected: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { /* TODO */ }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Primary else Slate400,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Primary else Slate400
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
