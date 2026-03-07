package com.paondev.infoplat.ui.screen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.paondev.infoplat.data.api.JabarPajakResponse
import com.paondev.infoplat.ui.components.LicensePlateHeroDetail
import com.paondev.infoplat.ui.components.TaxStatusCard
import com.paondev.infoplat.ui.components.VehicleSpecificationCard

@Composable
fun VehicleHistoryScreen(
    navController: NavController,
    jabarPajakData: JabarPajakResponse
) {
    val data = jabarPajakData.data
    
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        if (data != null) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
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

@Composable
fun SpecItemHistory(
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
fun SpecColorItemHistory(
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
fun TaxBreakdownItemHistory(
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
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
        )
    }
}