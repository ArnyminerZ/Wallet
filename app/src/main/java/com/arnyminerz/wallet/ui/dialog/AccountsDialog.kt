package com.arnyminerz.wallet.ui.dialog

import android.accounts.Account
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arnyminerz.wallet.BuildConfig.ACCOUNT_TYPE
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.model.provider.AccountsProvider

@Composable
@ExperimentalMaterial3Api
fun AccountsDialog(
    provider: AccountsProvider,
    onAddRequest: () -> Unit,
    onDeleteRequest: (index: Int, account: Account) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val selectedAccount by provider.selectedAccount
    val accounts by provider.accounts.observeAsState(emptyArray())

    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.title_accounts),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = onAddRequest) {
                        Icon(Icons.Rounded.Add, stringResource(R.string.title_add_account))
                    }
                }
                for ((index, account) in accounts.withIndex()) {
                    ListItem(
                        headlineText = { Text(account.name) },
                        leadingContent = {
                            if (selectedAccount == index)
                                Icon(Icons.Rounded.CheckCircle, stringResource(R.string.accounts_selected))
                            else
                                Icon(Icons.Rounded.Circle, stringResource(R.string.accounts_selected))
                        },
                        trailingContent = {
                            IconButton(onClick = { onDeleteRequest(index, account) }) {
                                Icon(
                                    Icons.Rounded.DeleteForever,
                                    stringResource(R.string.action_delete),
                                )
                            }
                        },
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.dialog_close))
            }
        },
    )
}

@Preview(
    showBackground = true,
)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AccountsDialogPreview() {
    AccountsDialog(
        provider = object : AccountsProvider {
            @SuppressLint("UnrememberedMutableState")
            override val selectedAccount: MutableState<Int> = mutableStateOf(0)

            override val accounts: LiveData<Array<out Account>> = MutableLiveData(
                arrayOf(
                    Account("Sample account 1", ACCOUNT_TYPE),
                    Account("Sample account 2", ACCOUNT_TYPE),
                ),
            )
        },
        { },
        { _, _ -> },
    ) { }
}
