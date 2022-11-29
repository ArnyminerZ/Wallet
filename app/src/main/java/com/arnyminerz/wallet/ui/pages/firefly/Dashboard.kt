package com.arnyminerz.wallet.ui.pages.firefly

import android.accounts.Account
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arnyminerz.wallet.model.MainViewModel
import com.arnyminerz.wallet.ui.elements.SummaryEntry

@Composable
@ExperimentalMaterial3Api
fun FireflyDashboard(viewModel: MainViewModel = viewModel(), account: Account) {
    val summary by remember { viewModel.getSummary(account) }.observeAsState()
    val transactions by remember { viewModel.getTransactions(account, 7) }.observeAsState()
    val accounts by remember { viewModel.getAccounts(account, 7) }.observeAsState()

    Column {
        summary?.let { sum ->
            SummaryEntry(
                sum.balance,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        Text("Last 7 transactions:")
        transactions?.forEach { transaction ->
            ListItem(
                headlineText = { Text(transaction.amountString + " - " + transaction.description) },
                leadingContent = {
                    Icon(
                        Icons.Rounded.Login.takeIf { transaction.type == "deposit" } ?: Icons.Rounded.Logout,
                        // TODO: Localize
                        "Income",
                    )
                },
            )
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
