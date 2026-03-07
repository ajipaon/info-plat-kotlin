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
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paondev.infoplat.ui.screen.TaxBreakdownItemHistory
import com.paondev.infoplat.utils.formatCurrency
import com.paondev.infoplat.utils.formatDate

@Composable
fun TaxStatusCard(data: com.paondev.infoplat.data.api.JabarPajakData?) {
    val stnkExpiry = data?.infoPkbPnpb?.tanggalStnk?.let { formatDate(it) } ?: "15 June 2025"
    val taxYear = if (data?.masaPajak?.tanggalBerlakuSampai != null && data.masaPajak.tanggalBerlakuSampai.length >= 4) {
        formatDate(data.masaPajak.tanggalBerlakuSampai)
    } else {
        "-"
    }
    val statusText = data?.keterangan ?: "UNKNOWN"
    val isPaid = data?.canBePaid == false
    val statusColor = if (isPaid) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error

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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(statusColor.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ReceiptLong,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        "Tax Status",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = statusColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        statusText,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column {
                    Text(
                        "STNK Expiry Date",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        stnkExpiry,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Tax Year",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        taxYear,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                val pembayaran = data?.infoPembayaran

                TaxBreakdownItemHistory(
                    "PKB (Vehicle Tax)",
                    formatCurrency(pembayaran?.pkb?.pokok ?: "0"),
                    false
                )
                TaxBreakdownItemHistory(
                    "Opsen (Admin)",
                    formatCurrency(pembayaran?.opsen?.pokok ?: "0"),
                    false
                )
                TaxBreakdownItemHistory(
                    "SWDKLLJ (Jasa Raharja)",
                    formatCurrency(pembayaran?.swdkllj?.pokok ?: "0"),
                    false
                )

                val pnpbStnk = pembayaran?.pnpb?.stnk?.toLongOrNull() ?: 0L
                val pnpbTnkb = pembayaran?.pnpb?.tnkb?.toLongOrNull() ?: 0L
                val totalPnpb = pnpbStnk + pnpbTnkb

                if (totalPnpb > 0) {
                    TaxBreakdownItemHistory(
                        "PNPB (Admin)",
                        formatCurrency(totalPnpb.toString()),
                        false
                    )
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                TaxBreakdownItemHistory(
                    "Total Tax",
                    formatCurrency(pembayaran?.jumlah ?: "0"),
                    true
                )
            }
        }
    }
}
