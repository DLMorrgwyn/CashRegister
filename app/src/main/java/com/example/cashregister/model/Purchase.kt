package com.example.cashregister.model

import java.util.Date

data class Purchase(
    val id: Int,
    val name: String,
    val quantity: Int,
    val price: Double,
    val totalPrice: Double,
    val date: Date = Date()
)
