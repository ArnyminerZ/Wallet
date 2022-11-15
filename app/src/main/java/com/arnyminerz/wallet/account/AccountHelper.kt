package com.arnyminerz.wallet.account

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.WorkerThread
import com.arnyminerz.wallet.BuildConfig
import com.arnyminerz.wallet.storage.SERVER_URL
import com.arnyminerz.wallet.utils.getParcelableCompat
import com.arnyminerz.wallet.utils.getPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
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

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor("client_id", "client_secret"))
        .build()

    val accounts: Array<out Account>
        get() = am.getAccountsByType(BuildConfig.APPLICATION_ID)

    private suspend fun getServerUrl(context: Context) = context.getPreference(SERVER_URL).first()

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
                            val base = getServerUrl(activity)!!
                            val username = account.name
                            val password = am.getPassword(account)
                            val requestData = "grant_type=password&username=$username&password=$password"
                            loginRequest(base)
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

    private suspend fun loginRequest(appUrl: String, requestData: String) = suspendCoroutine { c ->
        val body = requestData.toRequestBody(CONTENT_TYPE)
        val request = Request.Builder()
            .url(appUrl)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .post(body)
            .build()
        httpClient
            .newCall(request)
            .enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    c.resume(response.body?.string())
                }

                override fun onFailure(call: Call, e: IOException) {
                    c.resumeWithException(e)
                }
            })
    }
}
