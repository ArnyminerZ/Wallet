package com.arnyminerz.wallet.ui.pages.firefly

import android.accounts.Account
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.database.data.FireflyTransaction
import com.arnyminerz.wallet.database.data.TransactionType
import com.arnyminerz.wallet.model.MainViewModel
import com.arnyminerz.wallet.ui.components.BalanceCard
import com.arnyminerz.wallet.ui.components.TransactionCard
import com.arnyminerz.wallet.ui.theme.colorGreen
import com.arnyminerz.wallet.ui.theme.colorRed
import com.arnyminerz.wallet.ui.utils.isScrolledToEnd
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

private const val VIEW_DAILY = 0
private const val VIEW_WEEKLY = 1
private const val VIEW_MONTHLY = 2
private const val VIEW_YEARLY = 3

@Composable
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
fun FireflyDashboard(viewModel: MainViewModel = viewModel(), account: Account) {
    val context = LocalContext.current

    val scrollState = rememberLazyListState()

    val summary by remember { viewModel.getSummary(account) }.observeAsState()
    var transactions by remember { mutableStateOf(emptyList<Pair<String, List<FireflyTransaction>>>()) }
    var selectedView by remember { mutableStateOf(0) }
    var offset by remember { mutableStateOf(0) }
    var reachedEnd by remember { mutableStateOf(false) }

    fun updateTransactions(list: List<FireflyTransaction> = transactions.map { it.second }.flatten()) {
        transactions = list.groupBy {
            SimpleDateFormat(
                when(selectedView) {
                    VIEW_DAILY -> "EEE, MMM d, yyyy"
                    VIEW_WEEKLY -> "'" + context.getString(R.string.dashboard_group_week_number) + "' w, yyyy"
                    VIEW_MONTHLY -> "MMMM yyyy"
                    else -> "yyyy"
                },
                Locale.getDefault(),
            ).format(it.date)
        }.toList()
    }

    LaunchedEffect(transactions) {
        Timber.d("Getting transactions for today")
        viewModel.getTransactions(
            account,
            start = Calendar.getInstance().time,
            end = Calendar.getInstance()
                .apply {
                    add(Calendar.DATE, 1)
                }
                .time,
        ) {
            val all = transactions
                .map { it.second }
                .flatten()
                .toMutableList()
            updateTransactions(all.apply { addAll(it) })
        }
    }

    LaunchedEffect(scrollState) {
        snapshotFlow { scrollState.isScrolledToEnd() }
            .distinctUntilChanged()
            .collect {
                while (scrollState.isScrolledToEnd() && !reachedEnd) {
                    offset++
                    Timber.d("Getting transactions for $offset day(s) ago")
                    viewModel.getTransactions(
                        account,
                        start = Calendar.getInstance()
                            .apply {
                                add(Calendar.DATE, -offset)
                            }
                            .time,
                        end = Calendar.getInstance()
                            .apply {
                                add(Calendar.DATE, -offset)
                            }
                            .time,
                    ) { tr ->
                        if (tr.isEmpty())
                            reachedEnd = true
                        else {
                            val all = transactions
                                .map { it.second }
                                .flatten()
                                .toMutableList()
                            updateTransactions(all.apply { addAll(tr) })
                        }
                    }
                }
            }
    }

    LazyColumn(state = scrollState) {
        item {
            summary?.let { sum -> BalanceCard(sum, modifier = Modifier.padding(horizontal = 8.dp)) }
                ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
        }
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                FilterChip(
                    selected = selectedView == VIEW_DAILY,
                    onClick = { selectedView = VIEW_DAILY; updateTransactions() },
                    label = { Text(stringResource(R.string.dashboard_sort_daily)) },
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
                FilterChip(
                    selected = selectedView == VIEW_WEEKLY,
                    onClick = { selectedView = VIEW_WEEKLY; updateTransactions() },
                    label = { Text(stringResource(R.string.dashboard_sort_weekly)) },
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
                FilterChip(
                    selected = selectedView == VIEW_MONTHLY,
                    onClick = { selectedView = VIEW_MONTHLY; updateTransactions() },
                    label = { Text(stringResource(R.string.dashboard_sort_monthly)) },
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
                FilterChip(
                    selected = selectedView == VIEW_YEARLY,
                    onClick = { selectedView = VIEW_YEARLY; updateTransactions() },
                    label = { Text(stringResource(R.string.dashboard_sort_yearly)) },
                    modifier = Modifier.padding(horizontal = 4.dp),
                )
            }
        }
        items(transactions) { (date, transactions) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 4.dp),
            ) {
                val sum = transactions
                    .filter { arrayOf(TransactionType.DEPOSIT, TransactionType.WITHDRAWAL).contains(it.type) }
                    .sumOf { it.amount * (if (it.type == TransactionType.DEPOSIT) 1 else -1) }
                Text(
                    date,
                    modifier = Modifier
                        .weight(1f),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                )
                Text(
                    sum.let { transactions.firstOrNull()?.currency?.format(it) } ?: "",
                    modifier = Modifier,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = when {
                        sum > 0 -> colorGreen
                        sum < 0 -> colorRed
                        else -> Color.Unspecified
                    }
                )
            }
            for (transaction in transactions) {
                TransactionCard(
                    transaction = transaction, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
