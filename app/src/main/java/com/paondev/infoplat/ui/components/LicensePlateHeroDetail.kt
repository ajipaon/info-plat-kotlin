package com.paondev.infoplat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LicensePlateHeroDetail(noPolisi: String?, wilayah: String?) {
    val plateNumber = noPolisi ?: "B 1234 XYZ"
    val regionName = wilayah ?: "JAKARTA TIMUR"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(280.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
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
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        letterSpacing = 2.sp
                    )
                )
            }

            Text(
                plateNumber,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
            )

            Text(
                regionName.uppercase(),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }
    }
}
