package com.paondev.infoplat.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.paondev.infoplat.ui.theme.*

// Data model untuk provinsi
data class Province(
    val name: String,
    val polda: String,
    val imageUrl: String
)

// Data dummy semua provinsi
private val allProvinces = listOf(
    Province(
        name = "DKI Jakarta",
        polda = "Polda Metro Jaya",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBxgbk_SKvei2gbuNxwhxwqNbs6nhE3XeZrJZRIVDPL0Mc_ud1Tsr6QZQ-bXBii1XX2gS5wvDLvb1a8SUgVIqkENoFfN4_rNQQ_VsOfErXlfMbnBf5-wotuUpPp1vBk-HSd132H8ut_RYVCaQ2X6QcYHRNondvlLPxu-ll-mFPSYa6_BlHUeZRk61WLzxw9vIBcH05Q5b4NLGQu-P9vvXCDtHvZTXJMZtu-Fz4qVWThleeDKQUeTWr1g42URnTMuJ_baLz7Q88UHnd_"
    ),
    Province(
        name = "Jawa Barat",
        polda = "Polda Jawa Barat",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCObsHGzKUN3me-OvDBx7SjOU2tUpv_vABUQEC3WOYR_x84_B6qUhawCRkEqbL_g2y5Fw9FPWx7Iw-lLpVClHZMIeizVANbQ3D_l2XNcdrqaFF_Io3hqJqbd6OVo-GI_uNxrOR5VakQFtLQLrICq8J-XgAqSFgVMDZ30iNTdFvbUvSwWG9deHTh_uOoKNf_MZ4FtwcAmWgs-49rhlAF9YZyrU6Ijypy7Uho8jeUpSfvJhq3aaFBfQHmRpi6zyKrVT73xS1GEqIzQ6vD"
    ),
    Province(
        name = "Jawa Tengah",
        polda = "Polda Jawa Tengah",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuA7dK6zSQEFBQcL29EINtLeID64pSSp8KXVfjM509074OWVV0L61_JsyimylzTgi8_tPGVVpUrOb2yJoE0xfTuYprXmXs8LCKNXLe6Tq8gG6tAl1TlXHPON2lur5djTdHg7LxsVW74_AO5Zm1N5LEvmWMCotz53BGLJxdpRUgt-WuNnVx8YRPiLMT0ThKwaB-Ue8UiWCkZJ2iJ863rq_KukZ_UVfBeplPwWXAvsxHhn6ZkrIBQoqiM7gABAaXut2kmm9CuhLPcieFFh"
    ),
    Province(
        name = "Jawa Timur",
        polda = "Polda Jawa Timur",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDro4ayENof9jIPTax78bBSl5Oi_eYJzJb232bPQ3QjMkV4BnGH5NhRC6pMQWIemjvai32v1m_J04CKPtMKAxZIig9O8b_Guam_hoskxaN2NqrGqLuzLnB34ITWSIjAlHrm8w5QNhXkpdIypAGOs5-cGZMBgB-dMem5lVhM2zVUP7dzAn4KT00-JkaOn0iGxSuOLIyfOvV0cRJRPin7v1Co-RKZNM0gmNJbKzEhmoPWrBXOe0kfR1oP5IXiwNPbc8cyXq-Hx9WUhGoQ"
    ),
    Province(
        name = "Bali",
        polda = "Polda Bali",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDIMiujqSXV4jiw-mklIWOSGhMjTKdMewK8GYF9TMvOuWTo2u9i0fSY7uVrkJNmcz8IY4D4m3Q8P1jZg0iNCpeLK8uW1QHIPKsAde6tMTfc68PqKMBQ0liZcWdzNU_rVkHnNDfjNnnMpLigwhBGjURZwa_CiiKnR9cVFyHi6jOmkVRGvlH4W7GB6seqR4gGjnvnIoYvwpLwP98UwZKMoaHP-9NImpDWefxcWIH0q8dI7hMU4Rwl8StUDtWY5J3KmcKjSyTpUMireutp"
    ),
    Province(
        name = "Banten",
        polda = "Polda Banten",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCQK1jSfqjkZMkPdjRnYe2Qh70T9GlHn6vIZxCzkIxoujG9I90nmHedJMtP3fidspjlePv9CjoqLtW7thR8SuI0tIuE2cGLBWxeOMPTVRDl_qIDq_G8NYRGN39NAYSLNS8gMM5BC7PkpA_-aSOmJh2EDhgZrm_xWpST-SNFveXAcoKSQTX1WrxKnw9H2B6K2CSGljoXQgfHlj1JOZn12TBe4JPUEk_dL6wt93oaPdn817NzFxLlVkBP1-tsQcUt3OSBl295OMqoizOI"
    ),
    Province(
        name = "Sumatera Utara",
        polda = "Polda Sumatera Utara",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBogaAUDOGZE28mlm-FHpnuPvr-cNyaZTzNCvrvGLJQr5WWa1wss-HFedCOAvovvhPOI-3tqgHNAr3ydabuZ6v1SK2Dgw4SnRjKZRr_q3M045U-s0TuLHIZRbPGhgHXxwUCCp750nsY0trj4cDVJpUisHRPlNxKANPEXq9eqd2DWkAdMJa8IFHOMvIZJXGpkP40rcGmZyFtt9G1CuCMom_Wqj4_t-1ePcE6fNq-BQpVJbYRHYEtVi5QXVqEFceYkBxb1tD1d7EaTBxG"
    ),
    Province(
        name = "Sumatera Barat",
        polda = "Polda Sumatera Barat",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/03/Jam_Gadang_2014-08-03.jpg/320px-Jam_Gadang_2014-08-03.jpg"
    ),
    Province(
        name = "Sulawesi Selatan",
        polda = "Polda Sulawesi Selatan",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f7/Fort_Rotterdam_Makassar.jpg/320px-Fort_Rotterdam_Makassar.jpg"
    ),
    Province(
        name = "Kalimantan Barat",
        polda = "Polda Kalimantan Barat",
        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8b/Equator_Monument_Pontianak.jpg/320px-Equator_Monument_Pontianak.jpg"
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectProvinceScreen(
    onBackClick: () -> Unit = {},
    onProvinceClick: (Province) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredProvinces = remember(searchQuery) {
        if (searchQuery.isBlank()) allProvinces
        else allProvinces.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.polda.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            SelectProvinceBottomNav()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header
            SelectProvinceHeader(
                onBackClick = onBackClick
            )

            // Search Bar
            SelectProvinceSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )

            // Section Title
            Text(
                text = "ALL PROVINCES",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 12.dp
                )
            )

            // Province List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (filteredProvinces.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Provinsi tidak ditemukan",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(filteredProvinces) { province ->
                        SelectProvinceItem(
                            province = province,
                            onClick = { onProvinceClick(province) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectProvinceHeader(
    onBackClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "Select Province",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 40.dp),
                textAlign = TextAlign.Center
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            thickness = 1.dp
        )
    }
}

@Composable
private fun SelectProvinceSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .height(48.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
                modifier = Modifier.weight(1f),
                decorationBox = { inner ->
                    if (query.isEmpty()) {
                        Text(
                            text = "Search province or region",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            fontSize = 15.sp
                        )
                    }
                    inner()
                }
            )
            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onQueryChange("") }
                )
            }
        }
    }
}

@Composable
private fun SelectProvinceItem(
    province: Province,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Province thumbnail
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                AsyncImage(
                    model = province.imageUrl,
                    contentDescription = province.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Province info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = province.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = province.polda,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Chevron
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Select",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(22.dp)
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            thickness = 1.dp,
            modifier = Modifier.padding(start = 84.dp)
        )
    }
}

private enum class NavItem(
    val label: String,
    val icon: ImageVector,
    val activeIcon: ImageVector? = null
) {
    Home("Home", Icons.Default.Home),
    CheckTax("Check Tax", Icons.Default.DirectionsCar),
    History("History", Icons.Default.History),
    Profile("Profile", Icons.Default.Person)
}

@Composable
private fun SelectProvinceBottomNav() {
    var selected by remember { mutableIntStateOf(1) } // CheckTax aktif

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                RoundedCornerShape(0.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem.entries.forEachIndexed { index, item ->
                val isSelected = index == selected
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { selected = index }
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected)
                            MaterialTheme.colorScheme.tertiary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.tertiary
                        else
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SelectProvinceScreenPreview() {
    InfoPlatTheme {
        SelectProvinceScreen()
    }
}
