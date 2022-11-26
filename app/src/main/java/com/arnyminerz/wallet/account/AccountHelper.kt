package com.arnyminerz.wallet.account

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.WorkerThread
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import com.arnyminerz.wallet.BuildConfig
import com.arnyminerz.wallet.storage.tempClientId
import com.arnyminerz.wallet.storage.tempClientSecret
import com.arnyminerz.wallet.storage.tempServer
import com.arnyminerz.wallet.utils.getParcelableCompat
import com.arnyminerz.wallet.utils.setPreference
import com.arnyminerz.wallet.utils.toast
import com.arnyminerz.wallet.utils.ui
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

val CONTENT_TYPE = "application/x-www-form-urlencoded".toMediaType()

class AccountHelper private constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: AccountHelper? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: AccountHelper(context).also { INSTANCE = it }
        }
    }

    private val am = AccountManager.get(context)

    private var httpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor("client_id", "client_secret"))
        .build()

    val accounts: Array<out Account>
        get() = am.getAccountsByType(BuildConfig.APPLICATION_ID)

    @WorkerThread
    fun addAccount(
        username: String,
        password: String,
        clientId: String,
        clientSecret: String,
        serverUrl: String,
    ) = am.addAccountExplicitly(
        Account(username, BuildConfig.APPLICATION_ID),
        password,
        Bundle().apply {
            putString("client_id", clientId)
            putString("client_secret", clientSecret)
            putString("server", serverUrl)
        },
    )

    @WorkerThread
    suspend fun getAuthToken(activity: Activity, account: Account): String =
        suspendCancellableCoroutine { cont ->
            am.getAuthToken(
                account,
                "Firefly III",
                Bundle(),
                activity,
                object : AccountManagerCallback<Bundle> {
                    override fun run(result: AccountManagerFuture<Bundle>) {
                        val bundle = result.result
                        bundle
                            .getString(AccountManager.KEY_AUTHTOKEN)
                            ?.also { return cont.resume(it) }

                        bundle
                            .getParcelableCompat(AccountManager.KEY_INTENT, Intent::class)
                            ?.also { return activity.startActivity(it) }

                        // Obtain an auth token
                        runBlocking {
                            // TODO: Check for null
                            val username = account.name
                            val password = am.getPassword(account)

                            val serverUrl = am.getUserData(account, "server")
                            val clientId = am.getUserData(account, "client_id")
                            val clientSecret = am.getUserData(account, "client_secret")

                            val response = login(username, password, serverUrl, clientId, clientSecret)
                            Timber.i("Login response: $response")
                        }
                        /*val url = URL("")
                        val conn = url.openConnection() as HttpURLConnection
                        conn.apply {
                            addRequestProperty("client_id", your client id)
                            addRequestProperty("client_secret", your client secret)
                            setRequestProperty("Authorization", "OAuth $token")
                        }*/
                    }
                },
                null,
            )
        }

    suspend fun login(
        username: String,
        password: String,
        serverUrl: String,
        clientId: String,
        clientSecret: String,
    ): String? {
        httpClient = httpClient.newBuilder()
            .addInterceptor(AuthInterceptor(clientId, clientSecret))
            .build()

        val requestData = "grant_type=password&username=$username&password=$password"
        return loginRequest(serverUrl, requestData)
    }

    /**
     * Requests the server authorisation to use the OAuth client.
     * @author Arnau Mora
     * @since 20221126
     */
    @WorkerThread
    suspend fun authoriseClient(context: Context, server: String, clientId: String, clientSecret: String) {
        val authCode = AuthCode(server, clientId, clientSecret, null)
        val authCodes = context.getAuthCodes()
        if (authCodes.find { it == authCode } != null) {
            // Code already authorised
            ui { context.toast("Already authed") }
        } else {
            val url = "$server/oauth/authorize?client_id=$clientId&client_secret=$clientSecret&redirect_uri=app://com.arnyminerz.wallet&response_type=code"
            val builder = CustomTabsIntent.Builder()
                .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
            val customTabsIntent = builder.build()

            context.setPreference(tempServer, server)
            context.setPreference(tempClientId, clientId)
            context.setPreference(tempClientSecret, clientSecret)

            customTabsIntent.launchUrl(context, Uri.parse(url))
        }
    }

    fun registerCode() {

    }

    private suspend fun loginRequest(appUrl: String, requestData: String) = suspendCoroutine { c ->
        val body = requestData.toRequestBody(CONTENT_TYPE)
        val request = Request.Builder()
            .url("$appUrl/oauth/token".also { Timber.d("Request: $it") })
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .post(body)
            .build()
        httpClient
            .newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    c.resume(response.body?.string())
                }

                override fun onFailure(call: Call, e: IOException) {
                    c.resumeWithException(e)
                }
            })
    }
}
