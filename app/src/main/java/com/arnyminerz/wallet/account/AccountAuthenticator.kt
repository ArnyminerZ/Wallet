package com.arnyminerz.wallet.account

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arnyminerz.wallet.activity.MainActivity

class AccountAuthenticator(context: Context) : AbstractAccountAuthenticator(context) {
    private val am = AccountManager.get(context)

    private val mainActivityIntent = Intent(context, MainActivity::class.java)

    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle = Bundle().apply {
        putParcelable(AccountManager.KEY_INTENT, mainActivityIntent.apply {
            putExtra(MainActivity.EXTRA_ADDING_NEW_ACCOUNT, true)
        })
    }

    override fun confirmCredentials(response: AccountAuthenticatorResponse?, account: Account?, options: Bundle?): Bundle? = null

    override fun editProperties(response: AccountAuthenticatorResponse?, accountType: String?): Bundle? = null

    override fun getAuthToken(response: AccountAuthenticatorResponse, account: Account, authTokenType: String, options: Bundle?): Bundle {
        val authToken = am.peekAuthToken(account, authTokenType)
        if (authToken?.isEmpty() == false)
            return Bundle().apply {
                putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
                putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
                putString(AccountManager.KEY_AUTHTOKEN, authToken)
            }

        return Bundle().apply {
            putParcelable(AccountManager.KEY_INTENT, mainActivityIntent.apply {
                putExtra(MainActivity.EXTRA_ADDING_NEW_ACCOUNT, true)
            })
        }
    }

    override fun getAuthTokenLabel(authTokenType: String?): String? = null

    override fun hasFeatures(response: AccountAuthenticatorResponse?, account: Account?, features: Array<out String>?): Bundle? = null

    override fun updateCredentials(response: AccountAuthenticatorResponse?, account: Account?, authTokenType: String?, options: Bundle?): Bundle? = null
}