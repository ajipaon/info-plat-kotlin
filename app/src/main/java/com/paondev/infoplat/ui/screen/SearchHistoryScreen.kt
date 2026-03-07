package com.paondev.infoplat.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.data.api.JatimPkbResponse
import com.paondev.infoplat.navigation.VehicleHistoryDestination
import com.paondev.infoplat.ui.viewmodel.HistoryDisplayItem
import com.paondev.infoplat.ui.viewmodel.SearchHistoryViewModel

@Composable
fun SearchHistoryScreen(
    navController: NavController,
    viewModel: SearchHistoryViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val historyItems by viewModel.historyItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val filteredHistory = remember(searchQuery, historyItems) {
        if (searchQuery.isBlank()) {
            historyItems
        } else {
            historyItems.filter { item ->
                item.plate.contains(searchQuery, ignoreCase = true) ||
                        item.model.contains(searchQuery, ignoreCase = true)
            }
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
                    .clickable { viewModel.clearAllHistory() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // Search Bar
        HistorySearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.onSearchQueryChanged(it) }
        )

        // History Log Section
        if (filteredHistory.isEmpty()) {
            EmptyState()
        } else {
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
                        item = item,
                        navigateToDetail = { 
                            // Parse data based on region
                            val response: JabarPajakResponse = try {
                                when (item.region) {
                                    "JTM" -> {
                                        val jatimResponse = Gson().fromJson(item.responseData, JatimPkbResponse::class.java)
                                        convertJatimToJabar(jatimResponse)
                                    }
                                    else -> {
                                        Gson().fromJson(item.responseData, JabarPajakResponse::class.java)
                                    }
                                }
                            } catch (e: Exception) {
                                // Fallback: try parsing as Jabar response
                                Gson().fromJson(item.responseData, JabarPajakResponse::class.java)
                            }
                            val route = VehicleHistoryDestination.createRoute(response)
                            navController.navigate(route)
                        },
                        onDelete = { viewModel.deleteHistoryItem(item.id) }
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
                            text = "${filteredHistory.size} search(es) saved",
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
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No Search History",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your vehicle searches will appear here",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            )
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
    item: HistoryDisplayItem,
    navigateToDetail: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToDetail() }
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
            onClick = onDelete,
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