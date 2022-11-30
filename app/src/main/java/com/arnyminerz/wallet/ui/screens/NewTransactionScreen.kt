package com.arnyminerz.wallet.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.data.`object`.FireflyCurrency
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private val tabs = listOf(
    (Icons.Rounded.Archive to Icons.Outlined.Archive) to R.string.new_transaction_type_income,
    (Icons.Rounded.Unarchive to Icons.Outlined.Unarchive) to R.string.new_transaction_type_expense,
    (Icons.Rounded.SwapHoriz to Icons.Outlined.SwapHoriz) to R.string.new_transaction_type_income,
)

@ExperimentalPagerApi
@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
@ExperimentalMaterial3Api
fun NewTransactionScreen(onCloseRequested: () -> Unit = {}) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.new_transaction_title)) },
                navigationIcon = {
                    IconButton(onClick = onCloseRequested) {
                        Icon(
                            Icons.Rounded.Close,
                            stringResource(R.string.image_desc_close),
                        )
                    }
                },
            )
        }
    ) { paddingValues ->
        var date by remember {
            mutableStateOf(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date())
            )
        }
        var description by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("0.0") }
        var currency by remember { mutableStateOf(FireflyCurrency(0, "EUR", "€", 2)) }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
        ) {
            var tabIndex by remember { mutableStateOf(0) }

            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }
                    .collect { tabIndex = it }
            }

            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, (icons, textRes) ->
                    Tab(
                        selected = tabIndex == index,
                        onClick = {
                            tabIndex = index
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        icon = {
                            Icon(
                                icons.first.takeIf { tabIndex == index } ?: icons.second,
                                stringResource(textRes),
                            )
                        },
                        text = {
                            Text(stringResource(textRes))
                        },
                    )
                }
            }
            HorizontalPager(
                count = tabs.size,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) { page ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                ) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = { },
                        label = { Text(stringResource(R.string.new_transaction_field_date)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Ascii,
                        ),
                        singleLine = true,
                        readOnly = true,
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(stringResource(R.string.new_transaction_field_description)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            autoCorrect = true,
                            keyboardType = KeyboardType.Text,
                        ),
                        singleLine = false,
                    )
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = currency.format(it.toDoubleOrNull() ?: 0.0) },
                        label = { Text(stringResource(R.string.new_transaction_field_amount)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next,
                        ),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                when(page) {
                                    0 -> Icons.Rounded.Add
                                    1 -> Icons.Rounded.Remove
                                    else -> Icons.Rounded.SwapHoriz
                                },
                                stringResource(
                                    when(page) {
                                        0 -> R.string.image_desc_add_amount
                                        1 -> R.string.image_desc_remove_amount
                                        else -> R.string.image_desc_transfer_amount
                                    }
                                ),
                            )
                        },
                        trailingIcon = {
                            Icon(currency.icon, currency.code)
                        },
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            ) {
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(stringResource(R.string.new_transaction_action_save))
                }
            }
        }
    }
}
