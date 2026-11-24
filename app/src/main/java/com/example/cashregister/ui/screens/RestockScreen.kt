package com.example.cashregister.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.cashregister.model.Product
import com.example.cashregister.ui.components.AppTopBar
import com.example.cashregister.ui.components.ProductList
import com.example.cashregister.ui.theme.CashRegisterTheme
import com.example.cashregister.viewmodel.CashRegisterUIState
import com.example.cashregister.viewmodel.CashRegisterViewModel

@Composable
fun RestockScreen(
    viewModel: CashRegisterViewModel,
    onCancel: () -> Unit
) {
    RestockScreenContent(
        state = viewModel.uiState,
        onStockTextChange = viewModel::onStockTextChange,
        onRestock = viewModel::restock,
        onSelectStockProduct = viewModel::selectStockProduct,
        onCancel = onCancel
    )
}

@Composable
fun RestockScreenContent(
    state: CashRegisterUIState,
    onStockTextChange: (String) -> Unit,
    onRestock: () -> Unit,
    onSelectStockProduct: (Product) -> Unit,
    onCancel: () -> Unit
) {
    BackHandler(onBack = onCancel)

    Scaffold(topBar = { AppTopBar(title = "Restock", onBack = onCancel) })
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(8.dp),
        ) {
            Text(
                text = state.stockProduct?.name ?: "Select a product",
                style = MaterialTheme.typography.titleLarge
            )
            OutlinedTextField(
                value = state.stockText,
                onValueChange = onStockTextChange,
                label = { Text("Enter new quantity") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Row(modifier = Modifier.padding(vertical = 8.dp)) {
                Button(onClick = onRestock) { Text("OK") }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = onCancel) { Text("Cancel") }
            }
            ProductList(
                products = state.products,
                selectedId = state.stockProductId,
                onSelect = onSelectStockProduct,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RestockScreenPreview() {
    val sampleProducts = listOf(
        Product(1, "Dresses", 20, 25.99),
        Product(2, "Hats", 30, 9.99)
    )
    CashRegisterTheme {
        RestockScreenContent(
            state = CashRegisterUIState(products = sampleProducts),
            onStockTextChange = {},
            onRestock = {},
            onSelectStockProduct = {},
            onCancel = {}
        )
    }
}

