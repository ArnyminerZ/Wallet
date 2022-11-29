package com.arnyminerz.wallet.data.remote

import android.content.Context
import androidx.annotation.WorkerThread
import com.arnyminerz.wallet.data.`object`.FireflyAccount
import com.arnyminerz.wallet.data.local.AppDatabase

class DatabaseSynchronizer(context: Context, private val api: FireflyApi) {
    private val database = AppDatabase.getInstance(context)

    @WorkerThread
    suspend fun synchronizeAccounts() {
        val remoteAccounts = api.getAccounts()
        val accountsDao = database.accountsDao()
        val localAccounts = accountsDao.getAll()

        // Check for new and updatable accounts
        val new = arrayListOf<FireflyAccount>()
        val updatable = arrayListOf<FireflyAccount>()
        for (account in remoteAccounts) {
            val localAccount = localAccounts.find { it.id == account.id }
            if (localAccount != null)
                updatable.add(account)
            else
                new.add(account)
        }

        // Add all new accounts
        accountsDao.addAll(*new.toTypedArray())

        // Update all the updatable accounts
        accountsDao.updateAll(*updatable.toTypedArray())

        // Remove all non-existing accounts
        val removable = localAccounts.mapNotNull { account -> remoteAccounts.find { it.id == account.id } }
        accountsDao.deleteAll(*removable.toTypedArray())
    }
}