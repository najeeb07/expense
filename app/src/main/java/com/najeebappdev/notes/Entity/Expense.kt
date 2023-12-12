package com.najeebappdev.notes.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String
)