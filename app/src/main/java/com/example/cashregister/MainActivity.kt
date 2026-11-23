package com.example.cashregister

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cashregister.navigation.CashRegisterApp
import com.example.cashregister.ui.theme.CashRegisterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CashRegisterTheme {
                CashRegisterApp()
            }
        }
    }
}