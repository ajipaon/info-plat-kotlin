package com.paondev.infoplat.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paondev.infoplat.model.VehicleStatus

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
