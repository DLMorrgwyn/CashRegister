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
    val selectedProductId: Int? = null,
    val quantityText: String = "",
    val lastPurchase: Purchase? = null,
    val stockProductId: Int? = null,
    val stockText: String = "",
    val message: String? = null
) {
    val selectedProduct: Product?
        get() = products.find { it.id == selectedProductId }

    val stockProduct: Product?
        get() = products.find { it.id == stockProductId }

    val quantity: Int
        get() = quantityText.toIntOrNull() ?: 0

    val total: Double
        get() = (selectedProduct?.unitprice ?: 0.0) * quantity
}

class CashRegisterViewModel(
    private val repository: CashRegisterRepository = CashRegisterRepository
) : ViewModel() {
    private val stockMessage = "Not enough product in stock."
    private val emptyMessage = "All fields are required."

    var uiState by mutableStateOf(CashRegisterUIState())
        private set

    init {
        refresh()
    }

    private fun refresh() {
        uiState = uiState.copy(
            products = repository.getProducts(),
            purchases = repository.getPurchases()
        )
    }

    fun onDigit(digit: Int) {
        val newText = (uiState.quantityText + digit).trimStart('0')
        if (newText.length > 4) return

        val newQuantity = newText.toIntOrNull() ?: 0
        val product = uiState.selectedProduct

        if (product != null && newQuantity > product.quantity) {
            uiState = uiState.copy(message = stockMessage)
            return
        }
        uiState = uiState.copy(quantityText = newText)
    }

    fun selectProduct(product: Product) {
        val enteredQuantity = uiState.quantityText.toIntOrNull() ?: 0
        uiState = if (enteredQuantity > product.quantity) {
            uiState.copy(
                selectedProductId = product.id,
                quantityText = "",
                message = stockMessage
            )
        } else {
            uiState.copy(selectedProductId = product.id)
        }
    }

    fun clearQuantity() {
        uiState = uiState.copy(quantityText = "")
    }

    fun buy() {
        val product = uiState.selectedProduct
        val quantity = uiState.quantityText.toIntOrNull() ?: 0
        when {
            product == null || quantity == 0 ->
                uiState = uiState.copy(message = emptyMessage)

            quantity > product.quantity ->
                uiState = uiState.copy(message = stockMessage)

            else -> {
                repository.updateQuantity(product.id, product.quantity - quantity)
                val purchase = repository.addPurchase(product, quantity)
                refresh()
                uiState = uiState.copy(
                    selectedProductId = null,
                    quantityText = "",
                    lastPurchase = purchase
                )
            }
        }
    }

    fun dismissPurchaseDialog() {
        uiState = uiState.copy(lastPurchase = null)
    }

    fun getPurchase(id: Int): Purchase? = repository.getPurchase(id)

    fun selectStockProduct(product: Product) {
        uiState = uiState.copy(stockProductId = product.id)
    }

    fun onStockTextChange(text: String) {
        if (text.all { it.isDigit() } && text.length <= 5) {
            uiState = uiState.copy(stockText = text)
        }
    }

    fun restock() {
        val product = uiState.stockProduct
        val amount = uiState.stockText.toIntOrNull() ?: 0
        if (product == null || amount <= 0) {
            uiState = uiState.copy(message = emptyMessage)
            return
        }
        repository.updateQuantity(product.id, product.quantity + amount)
        refresh()
        uiState = uiState.copy(
            stockProductId = null,
            stockText = "",
            message = "${product.name} restocked (+$amount)"
        )
    }

    fun resetStock() {
        uiState = uiState.copy(stockProductId = null, stockText = "")
    }

    fun dismissStockDialog() {
        uiState = uiState.copy(message = null)
    }
}
