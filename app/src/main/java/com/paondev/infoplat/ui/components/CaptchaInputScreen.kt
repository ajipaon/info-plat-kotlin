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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.paondev.infoplat.ui.viewmodel.VehicleDetailUiState
import com.paondev.infoplat.ui.viewmodel.VehicleDetailViewModel
import com.paondev.infoplat.utils.decodeBase64ToImageBitmap
import kotlinx.coroutines.launch

@Composable
fun CaptchaInputScreen(
    navController: NavController,
    viewModel: VehicleDetailViewModel,
    captchaData: com.paondev.infoplat.data.api.JatimCaptchaResponse
) {
    var captchaCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isLoading by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Verifikasi CAPTCHA",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Text(
                    "Masukkan kode yang terlihat pada gambar",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // CAPTCHA Image
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val bitmap = decodeBase64ToImageBitmap(captchaData.image ?: "")
                        if (bitmap != null) {
                            androidx.compose.foundation.Image(
                                bitmap = bitmap,
                                contentDescription = "CAPTCHA",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Fit
                            )
                        } else {
                            Text(
                                "Gagal memuat gambar CAPTCHA",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Captcha Input
                Column {
                    Text(
                        "Kode CAPTCHA",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.foundation.text.BasicTextField(
                        value = captchaCode,
                        onValueChange = {
                            captchaCode = it.uppercase()
                            errorMessage = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .border(
                                2.dp,
                                if (errorMessage != null)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.tertiary,
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 16.dp),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.tertiary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            letterSpacing = 2.sp
                        ),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters
                        ),
                        // Wrap suspend function call in coroutine
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.tertiary),
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

                // Error message
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        errorMessage ?: "",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Verify Button
                Button(
                    onClick = {
                        if (captchaCode.isNotEmpty()) {
                            // Get plate parameters from current navigation (we're in VehicleDetail screen)
                            val backStackEntry = navController.currentBackStackEntry
                            val plateParams = backStackEntry?.arguments?.let {
                                com.paondev.infoplat.navigation.VehicleDetailDestination.parsePlateParameters(
                                    provinceCode = it.getString("provinceCode"),
                                    headPlat = it.getString("headPlat"),
                                    bodyPlat = it.getString("bodyPlat"),
                                    tailPlat = it.getString("tailPlat"),
                                    noRangka = it.getString("noRangka"),
                                    noNik = it.getString("noNik")
                                )
                            }

                            if (plateParams != null) {
                                val nopol = "${plateParams.headPlat}${plateParams.bodyPlat}${plateParams.tailPlat}".lowercase()
                                // Launch coroutine to call suspend function
                                coroutineScope.launch {
                                    viewModel.verifyJatimCaptcha(
                                        sessionId = captchaData.sessionId,
                                        captchaCode = captchaCode,
                                        nopol = nopol,
                                        noRangka = plateParams.noRangka
                                    )
                                }
                            }
                        } else {
                            errorMessage = "Kode captcha harus diisi"
                        }
                    },
                    enabled = captchaCode.isNotEmpty() && isLoading !is VehicleDetailUiState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            4.dp,
                            RoundedCornerShape(12.dp),
                            spotColor = MaterialTheme.colorScheme.tertiary
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (isLoading is VehicleDetailUiState.Loading) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Verifikasi",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Back Button
                Spacer(modifier = Modifier.height(12.dp))
                TextButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Kembali")
                }
            }
        }
    }
}