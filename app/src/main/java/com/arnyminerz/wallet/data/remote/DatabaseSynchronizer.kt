package com.arnyminerz.wallet.data.remote

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Dao
import com.arnyminerz.wallet.data.`object`.FireflyAccount
import com.arnyminerz.wallet.data.`object`.FireflyObject
import com.arnyminerz.wallet.data.local.AppDatabase
import com.arnyminerz.wallet.data.local.FireflyDao
import timber.log.Timber

class DatabaseSynchronizer(context: Context, private val api: FireflyApi) {
    private val database = AppDatabase.getInstance(context)

    @Suppress("RedundantSuspendModifier")
    private suspend inline fun <reified T: FireflyObject> synchronize(dao: FireflyDao<T>, remoteFetch: () -> List<T>): DatabaseSynchronizer {
        val typeName = T::class.simpleName
        Timber.d("$typeName synchronization for ${api.accountName} started.")
        Timber.d("Getting $typeName from remote...")
        val remote = remoteFetch()
        Timber.d("Getting local $typeName...")
        val local = dao.getAll()

        // Check for new and updatable accounts
        val new = arrayListOf<T>()
        val updatable = arrayListOf<T>()
        for (obj in remote) {
            val localObj = local.find { it.id == obj.id }
            if (localObj == null)
                new.add(obj)
            else if (localObj.hashCode() != obj.hashCode())
                updatable.add(obj)
        }

        // Add all new accounts
        Timber.d("Adding ${new.size} $typeName.")
        dao.addAll(*new.toTypedArray())

        // Update all the updatable accounts
        Timber.d("Updating ${updatable.size} $typeName.")
        dao.updateAll(*updatable.toTypedArray())

        // Remove all non-existing accounts
        val removable = local.filter { account -> remote.find { it.id == account.id } == null }
        Timber.d("Removing ${removable.size} $typeName.")
        dao.deleteAll(*removable.toTypedArray())

        return this
    }

    @WorkerThread
    suspend fun synchronizeAccounts() = synchronize(database.accountsDao()) { api.getAccounts() }

    @WorkerThread
    suspend fun synchronizeCurrencies() = synchronize(database.currenciesDao()) { api.getCurrencies() }

    @WorkerThread
    suspend fun synchronizeCategories() = synchronize(database.categoriesDao()) { api.getCategories() }
}