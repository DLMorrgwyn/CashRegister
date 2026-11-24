package com.example.cashregister.data

import com.example.cashregister.model.Product
import com.example.cashregister.model.Purchase

object CashRegisterRepository {   // Singleton object(passing information)
    private val products = mutableListOf<Product>(
        Product(1, "Dresses", 20, 25.99),
        Product(2, "Hats", 30, 9.99),
        Product(3, "Pants", 20, 25.99),
        Product(4, "Shoes", 100, 20.99),
        Product(5, "Socks", 80, 5.99),
        Product(6, "T-Shirts", 50, 12.99)
    )
    private val purchases = mutableListOf<Purchase>()
    private var nextPurchaseId: Int = 1

    fun getProducts(): List<Product> = products.toList()
    fun getPurchases(): List<Purchase> = purchases.toList()
    fun getPurchase(productId: Int): Purchase? = purchases.find { it.id == productId }

    fun addPurchase(product: Product, quantity: Int): Purchase {
        val purchase = Purchase(
            id = nextPurchaseId++,
            name = product.name,
            quantity = quantity,
            price = product.unitprice,
            totalPrice = product.unitprice * quantity
        )
        purchases.add(0, purchase)
        return purchase
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        val index = products.indexOfFirst { it.id == productId }
        if (index != -1) {
            products[index] = products[index].copy(quantity = newQuantity)
        }
    }
}