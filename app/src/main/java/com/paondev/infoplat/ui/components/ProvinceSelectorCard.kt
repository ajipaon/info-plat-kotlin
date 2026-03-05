package com.paondev.infoplat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.paondev.infoplat.data.Province
import com.paondev.infoplat.ui.viewmodel.ProvinceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvinceSelectorCard(
    viewModel: ProvinceViewModel = hiltViewModel()
) {
    var showModal by remember { mutableStateOf(false) }
    
    val provinces by viewModel.provinces.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedProvince by viewModel.selectedProvince.collectAsState()

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
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
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        letterSpacing = 0.5.sp
                    )
                )
                Text(
                    text = selectedProvince?.name ?: "Select Province",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.clickable { showModal = true }
            ) {
                Text(
                    text = "Change",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }

    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = { showModal = false },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            ProvincePickerSheet(
                provinces = provinces,
                isLoading = isLoading,
                error = error,
                selectedProvince = selectedProvince ?: provinces.firstOrNull() ?: Province(
                    name = "",
                    kode = "",
                    isActive = false,
                    withNoRangka = false,
                    withNik = false
                ),
                onSelect = {
                    viewModel.selectProvince(it)
                    showModal = false
                },
                onRetry = { viewModel.fetchProvinces() }
            )
        }
    }
}

@Composable
fun ProvincePickerSheet(
    provinces: List<Province>,
    isLoading: Boolean,
    error: String?,
    selectedProvince: Province,
    onSelect: (Province) -> Unit,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 600.dp)
            .padding(bottom = 32.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Select Province",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = "${provinces.size} provinces",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Failed to load provinces",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.clickable { onRetry() }
                        ) {
                            Text(
                                text = "Retry",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }
            }
            else -> {
                // Province List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(provinces) { province ->
                        val isSelected = province.kode == selectedProvince.kode
                        val isDisabled = !province.isActive
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(
                                    if (isDisabled) {
                                        Modifier
                                    } else {
                                        Modifier.clickable { onSelect(province) }
                                    }
                                )
                                .background(
                                    when {
                                        isSelected -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.07f)
                                        isDisabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f)
                                        else -> Color.Transparent
                                    }
                                )
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Kode badge
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = when {
                                    isSelected -> MaterialTheme.colorScheme.tertiary
                                    isDisabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                    else -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.12f)
                                }
                            ) {
                                Text(
                                    text = province.kode,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = TextStyle(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = when {
                                            isSelected -> MaterialTheme.colorScheme.onPrimary
                                            isDisabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                            else -> MaterialTheme.colorScheme.tertiary
                                        }
                                    )
                                )
                            }

                            Text(
                                text = province.name,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 14.dp),
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isDisabled) 
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    else 
                                        MaterialTheme.colorScheme.onSurface
                                )
                            )

                            if (isDisabled) {
                                Text(
                                    text = "Coming Soon",
                                    style = TextStyle(
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    )
                                )
                            } else if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
