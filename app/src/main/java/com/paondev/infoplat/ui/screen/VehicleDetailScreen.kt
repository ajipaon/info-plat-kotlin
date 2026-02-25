package com.paondev.infoplat.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.paondev.infoplat.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen() {
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
                    Text(
                        "Vehicle Details",
                        modifier = Modifier.fillMaxWidth().padding(end = 48.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = textColor,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                ),
                modifier = Modifier.shadow(1.dp)
            )
        },
        bottomBar = {
            VehicleDetailBottomNav(isDark)
        },
        containerColor = bgColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                LicensePlateHeroDetail(isDark)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                VehicleSpecificationCard(surfaceColor, borderColor, textColor, subTextColor)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                TaxStatusCard(surfaceColor, borderColor, textColor, subTextColor)
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                PaddingValues(horizontal = 16.dp).let {
                    Button(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(56.dp)
                            .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = Primary.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Icon(Icons.Outlined.History, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("View Tax History", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun LicensePlateHeroDetail(isDark: Boolean) {
    val containerColor = if (isDark) Slate800 else Color.White
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(containerColor)
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(280.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isDark) Color.Black else Slate900)
                .border(4.dp, if (isDark) Slate700 else Slate800, RoundedCornerShape(12.dp))
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
                        color = Slate400,
                        letterSpacing = 2.sp
                    )
                )
            }
            
            Text(
                "B 1234 XYZ",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            Divider(
                color = Slate700,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Text(
                "JAKARTA TIMUR",
                style = TextStyle(
                    color = Slate400,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }
    }
}

@Composable
fun VehicleSpecificationCard(surfaceColor: Color, borderColor: Color, textColor: Color, subTextColor: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Primary.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Vehicle Specification",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Vehicle Image Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuC6a-hsGVO93ckXCf3r-e1jfNPmoLxH9vVgIs3y9g-s7t1aRe5L2h-liuqI1Art5bmngzGK36GbvLUep_piTqp8lYkHHhuIXC--gzhzLiiCQqj6D2ppJSbtP0Jv6nn5U-1Hjic2LjnJ9v9DxSnQY8K1CL9mVWB7imYOTJ0DOeWDIWk4D3cx-kLuwdWWoqT_e69VTcd89h2-y-YTeydQ3VJdmEc_mNcmrHj7Pw_GVHwk1aPLV_QMDD74yr4lbWI5F4UFgGL_OGapSG0x",
                    contentDescription = "Toyota Avanza",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 50f
                            )
                        )
                )
                Text(
                    "Toyota Avanza 1.3 G",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Info Grid
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    SpecItem(label = "Brand / Model", value = "Toyota Avanza", modifier = Modifier.weight(1f), subTextColor, textColor)
                    SpecItem(label = "Manufacturing Year", value = "2020", modifier = Modifier.weight(1f), subTextColor, textColor)
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    SpecColorItem(label = "Color", value = "Silver Metallic", color = Color(0xFFC0C0C0), modifier = Modifier.weight(1f), subTextColor, textColor)
                    SpecItem(label = "Engine Capacity", value = "1329 cc", modifier = Modifier.weight(1f), subTextColor, textColor)
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    SpecItem(label = "Fuel Type", value = "Gasoline (Bensin)", modifier = Modifier.weight(1f), subTextColor, textColor)
                    SpecItem(label = "Ownership", value = "First (Pribadi)", modifier = Modifier.weight(1f), subTextColor, textColor)
                }
            }
        }
    }
}

@Composable
fun SpecItem(label: String, value: String, modifier: Modifier, subTextColor: Color, textColor: Color) {
    Column(modifier = modifier) {
        Text(label, fontSize = 12.sp, color = subTextColor)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
fun SpecColorItem(label: String, value: String, color: Color, modifier: Modifier, subTextColor: Color, textColor: Color) {
    Column(modifier = modifier) {
        Text(label, fontSize = 12.sp, color = subTextColor)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, CircleShape)
                    .border(1.dp, Slate300, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
        }
    }
}

@Composable
fun TaxStatusCard(surfaceColor: Color, borderColor: Color, textColor: Color, subTextColor: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shadowElevation = 1.dp
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
                            .background(Primary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ReceiptLong,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Tax Status",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                }
                
                Surface(
                    shape = RoundedCornerShape(full = 999.dp),
                    color = StatusGreenBg.copy(alpha = if (isSystemInDarkTheme()) 0.2f else 1f),
                    border = BorderStroke(1.dp, StatusGreen.copy(alpha = 0.3f))
                ) {
                    Text(
                        "PAID",
                        color = StatusGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expiry Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSystemInDarkTheme()) Slate700.copy(alpha = 0.3f) else Slate50)
                    .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("STNK Expiry Date", fontSize = 12.sp, color = subTextColor)
                    Text("15 June 2025", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Tax Year", fontSize = 12.sp, color = subTextColor)
                    Text("2024", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Fee Breakdown
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TaxBreakdownItem("PKB (Vehicle Tax)", "Rp 2.450.000", subTextColor, textColor, false)
                TaxBreakdownItem("SWDKLLJ (Jasa Raharja)", "Rp 143.000", subTextColor, textColor, false)
                TaxBreakdownItem("Administration Fee", "Rp 50.000", subTextColor, textColor, false)
                
                Divider(color = borderColor, modifier = Modifier.padding(vertical = 4.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Tax Paid", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)
                    Text("Rp 2.643.000", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Primary)
                }
            }
        }
    }
}

@Composable
fun TaxBreakdownItem(label: String, value: String, subTextColor: Color, textColor: Color, isTotal: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            fontSize = 14.sp,
            color = if (isTotal) textColor else subTextColor,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            value,
            fontSize = 14.sp,
            color = textColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun VehicleDetailBottomNav(isDark: Boolean) {
    val navColor = if (isDark) Slate900 else Color.White
    val borderColor = if (isDark) Slate800 else Slate200

    Surface(
        color = navColor,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(0.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 8.dp, top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItemDetail(Icons.Default.Home, "Home", false)
            NavItemDetail(Icons.Filled.DirectionsCar, "Vehicle", true)
            NavItemDetail(Icons.Default.BookmarkBorder, "Saved", false)
            NavItemDetail(Icons.Outlined.Person, "Profile", false)
        }
    }
}

@Composable
fun NavItemDetail(icon: ImageVector, label: String, isSelected: Boolean) {
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
