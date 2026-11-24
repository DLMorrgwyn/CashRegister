package com.example.cashregister.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.cashregister.model.Purchase
import com.example.cashregister.ui.components.AppTopBar
import com.example.cashregister.ui.theme.CashRegisterTheme
import com.example.cashregister.viewmodel.CashRegisterViewModel

@Composable
fun HistoryScreen(
    viewModel: CashRegisterViewModel,
    onPurchaseClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    HistoryScreenContent(
        purchases = viewModel.uiState.purchases,
        onPurchaseClick = onPurchaseClick,
        onBack = onBack
    )
}

@Composable
fun HistoryScreenContent(
    purchases: List<Purchase>,
    onPurchaseClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { AppTopBar(title = "History", onBack = onBack) }
    ) { padding ->
        if (purchases.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No purchases yet.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
            ) {
                items(purchases, key = { it.id }) { purchase ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPurchaseClick(purchase.id) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = purchase.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "%.2f".format(purchase.totalPrice),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                        Text(text = "Quantity: ${purchase.quantity}")
                    }
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    val samplePurchases = listOf(
        Purchase(1, "Dresses", 2, 25.99, 51.98),
        Purchase(2, "Hats", 1, 9.99, 9.99)
    )
    CashRegisterTheme {
        HistoryScreenContent(
            purchases = samplePurchases,
            onPurchaseClick = {},
            onBack = {}
        )
    }
}
