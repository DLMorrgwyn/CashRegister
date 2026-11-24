package com.example.cashregister.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.cashregister.model.Product
import com.example.cashregister.model.Purchase
import com.example.cashregister.ui.theme.CashRegisterTheme
import com.example.cashregister.ui.screens.*
import com.example.cashregister.viewmodel.CashRegisterUIState
import com.example.cashregister.viewmodel.CashRegisterViewModel

object Routes{
    const val HOME = "home"
    const val MANAGER = "manager"
    const val HISTORY = "history"
    const val HISTORY_DETAILS = "history_details/{purchaseId}"
    const val RESTOCK = "restock"

    fun historyDetails(purchaseId: Int) = "history_details/$purchaseId"
}

@Composable
fun CashRegisterApp(
    viewModel: CashRegisterViewModel = viewModel()
) {
    val navController = rememberNavController()
    CashRegisterAppContent(
        state = viewModel.uiState,
        navController = navController,
        onDismissStockDialog = viewModel::dismissStockDialog,
        getPurchase = viewModel::getPurchase,
        onResetStock = viewModel::resetStock,
        onSelectProduct = viewModel::selectProduct,
        onDigit = viewModel::onDigit,
        onClearQuantity = viewModel::clearQuantity,
        onBuy = viewModel::buy,
        onDismissPurchaseDialog = viewModel::dismissPurchaseDialog,
        onStockTextChange = viewModel::onStockTextChange,
        onRestock = viewModel::restock,
        onSelectStockProduct = viewModel::selectStockProduct
    )
}

@Composable
fun CashRegisterAppContent(
    state: CashRegisterUIState,
    navController: NavHostController,
    onDismissStockDialog: () -> Unit,
    getPurchase: (Int) -> Purchase?,
    onResetStock: () -> Unit,
    onSelectProduct: (Product) -> Unit,
    onDigit: (Int) -> Unit,
    onClearQuantity: () -> Unit,
    onBuy: () -> Unit,
    onDismissPurchaseDialog: () -> Unit,
    onStockTextChange: (String) -> Unit,
    onRestock: () -> Unit,
    onSelectStockProduct: (Product) -> Unit
) {
    val context = LocalContext.current

    //Toast messaging
    val message = state.message
    LaunchedEffect(message) {
        if (message != null) {
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
            onDismissStockDialog()
        }
    }
    NavHost(navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreenContent(
                state = state,
                onSelectProduct = onSelectProduct,
                onDigit = onDigit,
                onClearQuantity = onClearQuantity,
                onBuy = onBuy,
                onDismissPurchaseDialog = onDismissPurchaseDialog,
                onManagerClick = { navController.navigate(Routes.MANAGER) }
            )
        }

        composable(Routes.MANAGER) {
            ManagerScreen(
                onHistoryClick = { navController.navigate(Routes.HISTORY) },
                onRestockClick = { navController.navigate(Routes.RESTOCK) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreenContent(
                purchases = state.purchases,
                onPurchaseClick = { purchaseId ->
                    navController.navigate(Routes.historyDetails(purchaseId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.HISTORY_DETAILS,
            arguments = listOf(navArgument("purchaseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val purchaseId = backStackEntry.arguments?.getInt("purchaseId") ?: -1

            HistoryDetailsScreen(
                purchase = getPurchase(purchaseId),
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.RESTOCK) {
            RestockScreenContent(
                state = state,
                onStockTextChange = onStockTextChange,
                onRestock = onRestock,
                onSelectStockProduct = onSelectStockProduct,
                onCancel = {
                    onResetStock()
                    navController.popBackStack()
                }  //back to Manager Screen
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CashRegisterAppPreview() {
    val sampleProducts = listOf(
        Product(1, "Dresses", 20, 25.99),
        Product(2, "Hats", 30, 9.99),
        Product(3, "Pants", 20, 25.99),
        Product(4, "Shoes", 100, 20.99),
        Product(5, "Socks", 80, 5.99),
        Product(6, "T-Shirts", 50, 12.99)
    )
    val navController = rememberNavController()
    CashRegisterTheme {
        CashRegisterAppContent(
            state = CashRegisterUIState(products = sampleProducts),
            navController = navController,
            onDismissStockDialog = {},
            getPurchase = { null },
            onResetStock = {},
            onSelectProduct = {},
            onDigit = {},
            onClearQuantity = {},
            onBuy = {},
            onDismissPurchaseDialog = {},
            onStockTextChange = {},
            onRestock = {},
            onSelectStockProduct = {}
        )
    }
}
