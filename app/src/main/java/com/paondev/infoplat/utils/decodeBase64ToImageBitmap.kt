package com.paondev.infoplat.utils

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap

fun decodeBase64ToImageBitmap(base64String: String): androidx.compose.ui.graphics.ImageBitmap? {
    return try {
        val dataUrlPrefix = "data:image/png;base64,"
        val base64Data = if (base64String.startsWith(dataUrlPrefix)) {
            base64String.substring(dataUrlPrefix.length)
        } else {
            base64String
        }

        val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}