package com.paondev.infoplat.utils

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(amount: String): String {
    val number = amount.toLongOrNull() ?: 0L
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(number)
}