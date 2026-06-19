package com.anderson.financeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val accountName: String,
    val balance: Double,
    val accountType: String = "BANCO",
    val cardNumber: String = ""
)