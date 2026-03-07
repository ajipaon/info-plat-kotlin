package com.paondev.infoplat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paondev.infoplat.utils.decodeBase64ToImageBitmap

@Composable
fun CaptchaSection(
    captchaImage: String?,
    captchaCode: String,
    onCaptchaCodeChange: (String) -> Unit,
    isLoading: Boolean,
    onRefreshCaptcha: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // CAPTCHA Image
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else if (captchaImage != null) {
                    val bitmap = decodeBase64ToImageBitmap(captchaImage)
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap,
                            contentDescription = "CAPTCHA",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Text(
                            "Gagal memuat gambar CAPTCHA",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Text(
                        "Gagal memuat CAPTCHA",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Refresh CAPTCHA button
        TextButton(
            onClick = onRefreshCaptcha,
            enabled = !isLoading
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Refresh CAPTCHA",
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Refresh CAPTCHA",
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CAPTCHA Input
        Column {
            Text(
                text = "Kode CAPTCHA",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = captchaCode,
                onValueChange = onCaptchaCodeChange,
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
                    textAlign = TextAlign.Center,
                    letterSpacing = 2.sp
                ),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (captchaCode.isEmpty()) {
                            Text(
                                "Masukkan kode",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}