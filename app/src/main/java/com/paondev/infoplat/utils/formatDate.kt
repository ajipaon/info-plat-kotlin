package com.paondev.infoplat.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(dateString: String): String {
    val possibleFormats = listOf(
        "yyyy-MM-dd",
        "dd-MM-yyyy",
        "MM/dd/yyyy",
        "dd/MM/yyyy",
        "yyyy/MM/dd",
        "dd MMM yyyy",
        "MMM dd, yyyy",
        "yyyy-MM-dd HH:mm:ss",
        "dd-MM-yyyy HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ssZ"
    )

    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))

    for (format in possibleFormats) {
        try {
            val inputFormat = SimpleDateFormat(format, Locale.getDefault())
            inputFormat.isLenient = false  // Strict parsing agar tidak salah parse
            val date = inputFormat.parse(dateString)
            if (date != null) return outputFormat.format(date)
        } catch (e: Exception) {
            continue
        }
    }

    return dateString;
}
