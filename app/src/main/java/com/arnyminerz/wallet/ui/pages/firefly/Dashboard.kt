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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.model.MainViewModel
import com.arnyminerz.wallet.ui.elements.SummaryEntry

@Composable
@ExperimentalMaterial3Api
fun FireflyDashboard(viewModel: MainViewModel = viewModel(), account: Account) {
    val summary by remember { viewModel.getSummary(account) }.observeAsState()
    val transactions by remember { viewModel.getTransactions(account, 7) }.observeAsState()
    val accounts by remember { viewModel.storedAccounts }.observeAsState()

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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(
                stringResource(R.string.dashboard_accounts_balance),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
            accounts?.filter { it.type == "asset" }?.forEach { account ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                ) {
                    Text(
                        text = account.name,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        account.currency.format(account.balance),
                        modifier = Modifier
                            .padding(end = 16.dp),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
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
