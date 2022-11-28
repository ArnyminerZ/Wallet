package com.arnyminerz.wallet.account

import android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class AuthenticatorService: Service() {
    companion object {
        @Volatile
        private var accountAuthenticator: AccountAuthenticator? = null

        private fun getAuthenticator(context: Context): AccountAuthenticator = accountAuthenticator ?: synchronized(this) {
            accountAuthenticator ?: AccountAuthenticator(context).also { accountAuthenticator = it }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = getAuthenticator(this).takeIf { intent?.action.equals(ACTION_AUTHENTICATOR_INTENT) }?.iBinder
}