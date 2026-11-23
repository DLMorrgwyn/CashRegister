package com.example.cashregister.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cashregister.data.CashRegisterRepository
import com.example.cashregister.model.Product
import com.example.cashregister.model.Purchase

data class CashRegisterUIState(
    val products: List<Product> = emptyList(),
    val purchases: List<Purchase> = emptyList(),
//HomeScreen
    val selectedProductId: Int? = null,
    val quantityText: String = "",
    val lastPurchase: Purchase? = null,  //not null show "thank you" message
// Stock Screen
    val stockProductId: Int? = null,
    val stockProductName: String = "",
    val stockText: String = "",
    val message: String? = null      //One-time message shown as a Toas
){}
