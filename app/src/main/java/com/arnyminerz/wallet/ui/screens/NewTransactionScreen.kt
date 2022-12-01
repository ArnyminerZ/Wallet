package com.arnyminerz.wallet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.model.MainViewModel
import com.arnyminerz.wallet.ui.colors.clickableDisabledTextFieldColors
import com.arnyminerz.wallet.ui.elements.DropdownTextField
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

private val tabs = listOf(
    (Icons.Rounded.Archive to Icons.Outlined.Archive) to R.string.new_transaction_type_income,
    (Icons.Rounded.Unarchive to Icons.Outlined.Unarchive) to R.string.new_transaction_type_expense,
    (Icons.Rounded.SwapHoriz to Icons.Outlined.SwapHoriz) to R.string.new_transaction_type_income,
)

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
fun NewTransactionScreen(viewMode: MainViewModel = viewModel(), onCloseRequested: () -> Unit = {}) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val currencies by viewMode.storedCurrencies.observeAsState()
    val categories by viewMode.categories.observeAsState()

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
        var date by remember { mutableStateOf(LocalDate.now()) }
        var description by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf("0.0") }
        var currencyIndex by remember { mutableStateOf(0) }
        var category by remember { mutableStateOf("") }

        LaunchedEffect(currencies) {
            snapshotFlow { currencies }
                .distinctUntilChanged()
                .filterNotNull()
                .collect { newCurrencies ->
                    newCurrencies.find { it.default }
                        ?.let { newCurrencies.indexOf(it) }
                        ?.takeIf { it >= 0 }
                        ?.let { currencyIndex = it }
                }
        }

        var showCurrenciesDialog by remember { mutableStateOf(false) }
        if (showCurrenciesDialog)
            AlertDialog(
                onDismissRequest = { showCurrenciesDialog = false },
                title = { Text(stringResource(R.string.currency_dialog_title)) },
                text = {
                    currencies?.let {
                        LazyColumn {
                            itemsIndexed(it) { index, currency ->
                                ListItem(
                                    headlineText = { Text(currency.code) },
                                    leadingContent = { Icon(currency.icon, currency.symbol) },
                                    trailingContent = { if (currency.default) Badge { Text(stringResource(R.string.currency_dialog_default)) } },
                                    modifier = Modifier
                                        .clickable {
                                            currencyIndex = index
                                            showCurrenciesDialog = false
                                        },
                                )
                            }
                        }
                    } ?: Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) { CircularProgressIndicator() }
                },
                confirmButton = {
                    TextButton(onClick = { showCurrenciesDialog = false }) {
                        Text(stringResource(R.string.dialog_close))
                    }
                }
            )

        val dialogState = rememberSheetState(visible = false)
        CalendarDialog(
            state = dialogState,
            selection = CalendarSelection.Date(selectedDate = date) {
                date = it
            },
        )
        
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .imePadding(),
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
                            scope.launch { pagerState.animateScrollToPage(index) }
                            tabIndex = index
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
                state = pagerState,
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
                        value = date.format(DateTimeFormatter.ISO_DATE),
                        onValueChange = { },
                        label = { Text(stringResource(R.string.new_transaction_field_date)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { dialogState.show() },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Ascii,
                        ),
                        colors = clickableDisabledTextFieldColors(false),
                        singleLine = true,
                        readOnly = true,
                        enabled = false,
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(stringResource(R.string.new_transaction_field_description)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            autoCorrect = true,
                            keyboardType = KeyboardType.Text,
                        ),
                        singleLine = false,
                    )
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { txt -> txt.toDoubleOrNull()?.let { amount = currencies?.get(currencyIndex)?.format(it, false) ?: "" } },
                        label = { Text(stringResource(R.string.new_transaction_field_amount)) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next,
                        ),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                when (page) {
                                    0 -> Icons.Rounded.Add
                                    1 -> Icons.Rounded.Remove
                                    else -> Icons.Rounded.SwapHoriz
                                },
                                stringResource(
                                    when (page) {
                                        0 -> R.string.image_desc_add_amount
                                        1 -> R.string.image_desc_remove_amount
                                        else -> R.string.image_desc_transfer_amount
                                    }
                                ),
                            )
                        },
                        trailingIcon = {
                            currencies?.get(currencyIndex)?.let {
                                IconButton(onClick = { showCurrenciesDialog = true }) {
                                    Icon(it.icon, it.code)
                                }
                            } ?: CircularProgressIndicator()
                        },
                    )
                    DropdownTextField(
                        value = category,
                        onValueChange = { category = it },
                        autocomplete = categories?.map { it.name } ?: emptyList(),
                        label = stringResource(R.string.new_transaction_field_category),
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
