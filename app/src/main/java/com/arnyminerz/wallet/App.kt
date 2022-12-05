package com.arnyminerz.wallet

import android.app.Application
import androidx.core.os.HandlerCompat
import com.arnyminerz.wallet.account.AccountHelper
import com.arnyminerz.wallet.database.remote.DatabaseSynchronizer
import com.arnyminerz.wallet.database.remote.api
import com.arnyminerz.wallet.exception.HttpClientException
import com.arnyminerz.wallet.utils.doAsync
import com.arnyminerz.wallet.utils.getStringOrNull
import timber.log.Timber

class App : Application() {
    private lateinit var ah: AccountHelper

    override fun onCreate() {
        super.onCreate()

        Timber.plant(
            Timber.DebugTree()
        )

        ah = AccountHelper.getInstance(this)
        ah.startListeningForAccounts(HandlerCompat.createAsync(mainLooper))
        ah.addListener { accounts ->
            doAsync {
                Timber.i("Got ${accounts.size} accounts. Synchronizing data...")
                accounts.forEach { account ->
                    try {
                        Timber.d("Synchronizing data for \"${account.name}\"")
                        DatabaseSynchronizer(this@App, account.api(this@App))
                            .synchronizeAccounts()
                            .synchronizeCurrencies()
                            .synchronizeCategories()
                    } catch (e: HttpClientException) {
                        val json = e.json
                        if (json?.getStringOrNull("message") == "Unauthenticated") {
                            // User not authenticated, or access not granted, log in again
                            ah.authoriseClient(this@App, account)
                        } else
                            throw e
                    }
                }
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        ah.stopListeningForAccounts()
    }
}
