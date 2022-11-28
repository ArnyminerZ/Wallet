package com.arnyminerz.wallet

import android.app.Application
import android.os.Handler
import androidx.core.os.HandlerCompat
import com.arnyminerz.wallet.account.AccountHelper
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
    }

    override fun onTerminate() {
        super.onTerminate()
        ah.stopListeningForAccounts()
    }
}
