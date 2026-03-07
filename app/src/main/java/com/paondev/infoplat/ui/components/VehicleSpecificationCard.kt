package com.paondev.infoplat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paondev.infoplat.ui.screen.SpecColorItemHistory
import com.paondev.infoplat.ui.screen.SpecItemHistory

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

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                Row(Modifier.fillMaxWidth()) {
                    SpecItemHistory(
                        "Brand / Model",
                        brandModel,
                        Modifier.weight(1f)
                    )
                    SpecItemHistory(
                        "Manufacturing Year",
                        year,
                        Modifier.weight(1f)
                    )
                }

                Row(Modifier.fillMaxWidth()) {
                    SpecColorItemHistory(
                        "Color",
                        color.uppercase(),
                        Color(0xFFC0C0C0),
                        Modifier.weight(1f)
                    )
                    SpecItemHistory(
                        "Vehicle Type",
                        vehicleType,
                        Modifier.weight(1f)
                    )
                }

                Row(Modifier.fillMaxWidth()) {
                    SpecItemHistory(
                        "Fuel Type",
                        "Gasoline (Bensin)",
                        Modifier.weight(1f)
                    )
                    SpecItemHistory(
                        "Ownership",
                        ownership,
                        Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
