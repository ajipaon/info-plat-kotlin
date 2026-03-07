package com.paondev.infoplat.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScanPlate(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
        Text(
            text = "OR USE CAMERA",
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                letterSpacing = 1.sp
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    }

    OutlinedButton(
        onClick = { /* TODO */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.tertiary
        ),
        border = ButtonDefaults.outlinedButtonBorder(
            enabled = true,
//                        shape = RoundedCornerShape(12.dp)
        ).copy(
            brush = SolidColor(MaterialTheme.colorScheme.tertiary)
        )
    ) {
        Icon(
            Icons.Default.PhotoCamera,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "Scan Plate",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
