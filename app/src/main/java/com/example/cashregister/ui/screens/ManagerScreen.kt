package com.example.cashregister.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cashregister.ui.components.AppTopBar

@Composable
fun ManagerScreen(
    onHistoryClick: () -> Unit,
    onRestockClick: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppTopBar(title = "Manager", onBack = onBack) })
    { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onHistoryClick, modifier = Modifier.width(200.dp))
            { Text("History") }

            Spacer(Modifier.height(16.dp))

            Button(onClick = onRestockClick, modifier = Modifier.width(200.dp))
            { Text("Restock") }

            Spacer(Modifier.height(16.dp))
        }
    }
}

