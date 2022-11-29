package com.arnyminerz.wallet.data.remote

import android.content.Context
import androidx.annotation.WorkerThread
import com.arnyminerz.wallet.data.`object`.FireflyAccount
import com.arnyminerz.wallet.data.local.AppDatabase
import timber.log.Timber

class DatabaseSynchronizer(context: Context, private val api: FireflyApi) {
    private val database = AppDatabase.getInstance(context)

    @WorkerThread
    suspend fun synchronizeAccounts() {
        Timber.d("Accounts synchronization for ${api.accountName} started.")
        Timber.d("Getting accounts from remote...")
        val remoteAccounts = api.getAccounts()
        Timber.d("Getting local accounts...")
        val accountsDao = database.accountsDao()
        val localAccounts = accountsDao.getAll()

        // Check for new and updatable accounts
        val new = arrayListOf<FireflyAccount>()
        val updatable = arrayListOf<FireflyAccount>()
        for (account in remoteAccounts) {
            val localAccount = localAccounts.find { it.id == account.id }
            if (localAccount == null)
                new.add(account)
            else if (localAccount.hashCode() != account.hashCode())
                updatable.add(account)
        }

        // Add all new accounts
        Timber.d("Adding ${new.size} accounts.")
        accountsDao.addAll(*new.toTypedArray())

        // Update all the updatable accounts
        Timber.d("Updating ${updatable.size} accounts.")
        accountsDao.updateAll(*updatable.toTypedArray())

        // Remove all non-existing accounts
        val removable = localAccounts.filter { account -> remoteAccounts.find { it.id == account.id } == null }
        Timber.d("Removing ${removable.size} accounts.")
        accountsDao.deleteAll(*removable.toTypedArray())
    }
}