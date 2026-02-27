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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.paondev.infoplat.navigation.VehicleDetailDestination
import com.paondev.infoplat.ui.theme.*

// Data model for history item
data class HistoryItem(
    val plate: String,
    val model: String,
    val date: String,
    val isMotorcycle: Boolean = false
)

private val historyLog = listOf(
    HistoryItem("B 1234 ABC", "Honda Vario 150", "12 Oct 2023", true),
    HistoryItem("D 5678 XYZ", "Toyota Avanza", "10 Oct 2023"),
    HistoryItem("L 9901 QR", "Yamaha NMAX", "08 Oct 2023", true),
    HistoryItem("AD 4432 BG", "Suzuki Ertiga", "05 Oct 2023"),
    HistoryItem("B 2002 TYU", "Mitsubishi Pajero", "01 Oct 2023"),
    HistoryItem("F 7781 GH", "Daihatsu Xenia", "28 Sep 2023")
)

@Composable
fun SearchHistoryScreen(
    navController: NavController,
    onBackClick: () -> Unit = {},
    onClearAllClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredHistory = remember(searchQuery) {
        if (searchQuery.isBlank()) historyLog
        else historyLog.filter {
            it.plate.contains(searchQuery, ignoreCase = true) ||
                    it.model.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Searches",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Clear All",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClearAllClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // Search Bar
        HistorySearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        // History Log Section
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Section Title Label
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "HISTORY LOG",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            letterSpacing = 1.2.sp
                        )
                    )
                }
            }

            // List Items
            items(filteredHistory) { item ->
                HistoryListItem(
                    item,
                    navigateToDetail = { i -> navController.navigate(VehicleDetailDestination.route)}
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                )
            }

            // Footer Text
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Showing last 30 days of history",
                        style = TextStyle(
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun HistorySearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                    .height(48.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
                    modifier = Modifier.weight(1f),
                    decorationBox = { inner ->
                        if (query.isEmpty()) {
                            Text(
                                text = "Search through history",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                fontSize = 14.sp
                            )
                        }
                        inner()
                    }
                )
            }
        }
    }
}

@Composable
private fun HistoryListItem(
    item: HistoryItem,
    navigateToDetail: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToDetail(item.model) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (item.isMotorcycle) Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.plate.uppercase(),
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.model,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .size(3.dp)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape)
                )
                Text(
                    text = item.date,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                )
            }
        }

        // Delete Button
        IconButton(
            onClick = { /* TODO */ },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

private enum class HistoryNav(
    val label: String,
    val icon: ImageVector,
) {
    Home("Home", Icons.Default.Home),
    History("History", Icons.Default.History),
    TaxInfo("Tax Info", Icons.Default.Description),
    Profile("Profile", Icons.Default.Person)
}

//@Preview(showBackground = true)
//@Composable
//fun SearchHistoryScreenPreview() {
//    InfoPlatTheme(darkTheme = false) {
//        SearchHistoryScreen()
//    }
//}