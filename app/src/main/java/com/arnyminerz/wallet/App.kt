package com.arnyminerz.wallet

import android.app.Application
import android.os.Handler
import androidx.core.os.HandlerCompat
import com.arnyminerz.wallet.account.AccountHelper
import com.arnyminerz.wallet.data.remote.DatabaseSynchronizer
import com.arnyminerz.wallet.data.remote.api
import com.arnyminerz.wallet.utils.doAsync
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
                accounts.forEach { DatabaseSynchronizer(this@App, it.api(this@App)).synchronizeAccounts() }
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        ah.stopListeningForAccounts()
    }
}
