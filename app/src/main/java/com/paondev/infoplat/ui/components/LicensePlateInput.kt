package com.paondev.infoplat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LicensePlateInput(
    isSinglePlate: Boolean,
    isMultiplePlate: Boolean,
    headPlat: String,
    onHeadPlatChange: (String) -> Unit,
    plateCodes: List<String>,
    selectedPlateCode: String,
    onSelectedPlateCodeChange: (String) -> Unit,
    bodyPlat: String,
    onBodyPlatChange: (String) -> Unit,
    tailPlat: String,
    onTailPlatChange: (String) -> Unit
) {
    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .border(2.dp, MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isSinglePlate) {
                // Auto-filled and disabled text field for single plate
                PlateTextField(
                    placeholder = headPlat,
                    length = 4,
                    modifier = Modifier.width(70.dp),
                    defaultValue = headPlat,
                    onValueChange = { },
                    enabled = false,
                    key = "head-$isSinglePlate"
                )
            } else if (isMultiplePlate) {
                // Dropdown for multiple plate codes
                var expanded by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { expanded = true }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (selectedPlateCode.isEmpty()) "Pilih" else selectedPlateCode,
                                style = TextStyle(
                                    color = if (selectedPlateCode.isEmpty())
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    else
                                        MaterialTheme.colorScheme.tertiary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    textAlign = TextAlign.Center
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(100.dp)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        plateCodes.forEach { code ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        code,
                                        style = TextStyle(
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    )
                                },
                                onClick = {
                                    onSelectedPlateCodeChange(code)
                                    onHeadPlatChange(code)
                                    expanded = false
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = MaterialTheme.colorScheme.tertiary
                                )
                            )
                        }
                    }
                }
            } else {
                // Regular text field for other cases
                PlateTextField(
                    placeholder = "B",
                    length = 2,
                    modifier = Modifier.width(50.dp),
                    defaultValue = headPlat,
                    onValueChange = onHeadPlatChange,
                    key = "head-$isSinglePlate"
                )
            }
            Text(
                "•",
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            PlateTextField(
                placeholder = "1234",
                length = 4,
                modifier = Modifier.width(90.dp),
                defaultValue = bodyPlat,
                onValueChange = onBodyPlatChange,
                key = "body"
            )
            Text(
                "•",
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            PlateTextField(
                placeholder = "XYZ",
                length = 3,
                modifier = Modifier.width(60.dp),
                defaultValue = tailPlat,
                onValueChange = onTailPlatChange,
                key = "tail"
            )
        }
    }
}