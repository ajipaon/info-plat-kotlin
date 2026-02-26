package com.paondev.infoplat.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val code: String,
    val requestDate: LocalDate,
    val region: String,
    val data: String
)