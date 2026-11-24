package com.example.cashregister.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cashregister.model.Purchase
import com.example.cashregister.ui.components.AppTopBar
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HistoryDetailsScreen(purchase: Purchase?, onBack: () -> Unit) {
    val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.getDefault())
    Scaffold(
        topBar = { AppTopBar(title = "Purchase Details", onBack = onBack) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (purchase == null) {
                Text("Purchase not found")
            } else {
                Text("Product: ${purchase.name}", style = MaterialTheme.typography.titleLarge)
                Text("Quantity: ${purchase.quantity}")
                Text("Price: $%.2f".format(purchase.price))
                Text("Total: $%.2f".format(purchase.totalPrice))
                Text("Date: ${formatter.format(purchase.date)}")
            }
            Spacer(Modifier.height(24.dp))
            Button(onClick = onBack) { Text("Back") }
        }

    }
}
