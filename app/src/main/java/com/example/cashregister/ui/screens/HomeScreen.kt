package com.example.cashregister.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx .compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.cashregister.model.Product
import androidx.compose.ui.graphics.Color
import com.example.cashregister.ui.components.AppTopBar
import com.example.cashregister.ui.components.ProductList
import com.example.cashregister.ui.theme.CashRegisterTheme
import com.example.cashregister.viewmodel.CashRegisterUIState
import com.example.cashregister.viewmodel.CashRegisterViewModel

@Composable
fun HomeScreen(
    viewModel: CashRegisterViewModel,
    onManagerClick: () -> Unit
) {
    HomeScreenContent(
        state = viewModel.uiState,
        onSelectProduct = viewModel::selectProduct,
        onDigit = viewModel::onDigit,
        onClearQuantity = viewModel::clearQuantity,
        onBuy = viewModel::buy,
        onDismissPurchaseDialog = viewModel::dismissPurchaseDialog,
        onManagerClick = onManagerClick
    )
}

@Composable
fun HomeScreenContent(
    state: CashRegisterUIState,
    onSelectProduct: (Product) -> Unit,
    onDigit: (Int) -> Unit,
    onClearQuantity: () -> Unit,
    onBuy: () -> Unit,
    onDismissPurchaseDialog: () -> Unit,
    onManagerClick: () -> Unit
) {
    val isLandscape =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val productList: @Composable (Modifier) -> Unit = { listModifier ->
        ProductList(
            products = state.products,
            selectedId = state.selectedProductId,
            onSelect = onSelectProduct,
            modifier = listModifier
        )
    }

        val inventorySection: @Composable (Modifier) -> Unit = { sectionModifier ->
            Column(modifier = sectionModifier, horizontalAlignment = Alignment.CenterHorizontally) {
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Type")
                    Text("Products: ${state.products.size}")
                    Text("Quantity")
                }
                productList(Modifier.weight(1f))
            }
        }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppTopBar(title = "Cash Register") }
    ) { innerPadding ->
        val screenModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(8.dp)

        if (isLandscape) {
            Row(modifier = screenModifier) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    HomeHeader(state, onManagerClick)
                    Spacer(Modifier.width(8.dp))
                    KeypadWithBuy(onDigit, onClearQuantity, onBuy)
                }

                Spacer(Modifier.width(8.dp))

                inventorySection(Modifier.weight(1f))
            }

        } else {
            Column(modifier = screenModifier) {

                HomeHeader(state, onManagerClick)

                KeypadWithBuy(onDigit, onClearQuantity, onBuy)

                Spacer(Modifier.height(8.dp))

                inventorySection(Modifier.weight(1f))
            }
        }
    }

    // Thank you dialog shown after a purchase
    state.lastPurchase?.let { purchase ->
        AlertDialog(
            onDismissRequest = onDismissPurchaseDialog,
            confirmButton = {
                TextButton(onClick = onDismissPurchaseDialog)
                { Text("OK") }
            },
            title = { Text("Thank you for your purchase!") },
            text = {
                Text(
                    "Your purchase is ${purchase.quantity} ${purchase.name} $" +
                            " for $%.2f".format(purchase.totalPrice)
                )
            }
        )
    }
}

@Composable
private fun HomeHeader(
    state: CashRegisterUIState,
    onManagerClick: () -> Unit
) {
    val product = state.products.find {
        it.id == state.selectedProductId
    }
    val quantity = state.quantityText.toIntOrNull() ?: 0
    val total = (product?.unitprice ?: 0.0) * quantity

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = product?.name ?: "Select Product",
            style = MaterialTheme.typography.titleMediumEmphasized
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quantity: $quantity",
                style = MaterialTheme.typography.titleMedium
            )

            Button(onClick = onManagerClick) {
                Text("Manager")
            }
        }
        Text(
            text = "Total: $%.2f".format(total),
            style = MaterialTheme.typography.titleMediumEmphasized
        )

        Spacer(Modifier.height(16.dp))
        HorizontalDivider()
    }
}

@Composable
private fun KeypadWithBuy(
    onDigit: (Int) -> Unit,
    onClear: () -> Unit,
    onBuy: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .height(IntrinsicSize.Min)
    ) {
        Column(modifier = Modifier.weight(3f)) {
            listOf(
                listOf(1, 2, 3),
                listOf(4, 5, 6),
                listOf(7, 8, 9)
            ).forEach { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    row.forEach { digit ->
                        Button(
                            onClick = { onDigit(digit) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(2.dp)
                        ) {
                            Text(digit.toString())
                        }
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onClear,
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp),
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("C")
                }

                Button(
                    onClick = { onDigit(0) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(2.dp)
                ) {
                    Text("0")
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Button(
            onClick = onBuy,
            modifier = Modifier
                .fillMaxHeight()
                .padding(2.dp)
        ) {
            Text("Buy")
        }
    }
}
