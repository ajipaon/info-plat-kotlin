package com.paondev.infoplat.utils

import java.security.MessageDigest

fun generateToket(headPlat: String, bodyPlat: String, tailPlat: String): String {
    val input = "$headPlat-mengandung-$bodyPlat-akhir-$tailPlat"
    val bytes = MessageDigest.getInstance("SHA-1").digest(input.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}