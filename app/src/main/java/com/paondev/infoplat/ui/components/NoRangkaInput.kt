package com.paondev.infoplat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoRangkaInput(
    noRangka: String,
    onNoRangkaChange: (String) -> Unit
) {
    Column {
        Text(
            text = "No Rangka (5 digit terakhir)",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        BasicTextField(
            value = noRangka,
            onValueChange = {
                if (it.length <= 5 && it.all { char -> char.isDigit() }) {
                    onNoRangkaChange(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (noRangka.isEmpty()) {
                        Text(
                            "12345",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}
