package com.arnyminerz.wallet.model.provider

import android.accounts.Account
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData

interface AccountsProvider {
    val selectedAccount: MutableState<Int>

    val accounts: LiveData<Array<out Account>>
}