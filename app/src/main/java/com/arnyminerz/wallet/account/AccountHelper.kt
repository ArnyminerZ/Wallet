package com.arnyminerz.wallet.account

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.annotation.WorkerThread
import androidx.browser.customtabs.CustomTabsIntent
import com.arnyminerz.wallet.BuildConfig
import com.arnyminerz.wallet.storage.authCodes
import com.arnyminerz.wallet.storage.tempClientId
import com.arnyminerz.wallet.storage.tempClientSecret
import com.arnyminerz.wallet.storage.tempServer
import com.arnyminerz.wallet.utils.popFromPreference
import com.arnyminerz.wallet.utils.setPreferences
import com.arnyminerz.wallet.utils.toast
import com.arnyminerz.wallet.utils.ui
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.net.URL
import java.net.URLEncoder
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
    suspend fun addAccount(
        authCode: AuthCode,
        tokenType: String,
        accessToken: String,
        refreshToken: String,
    ) {
        val url = URL(authCode.server)
        val rawAccountInfo = getRequest(
            Uri.Builder()
                .authority(url.authority)
                .scheme(url.protocol)
                .path("/api/v1/about/user")
            .build(),
            mapOf(
                "Authorization" to "$tokenType $accessToken",
            )
        )
        Timber.w("Account info: $rawAccountInfo")
        val accountInfoResponse = JSONObject(rawAccountInfo)
        val accountInfo = FireflyAccount.fromFireflyData(accountInfoResponse.getJSONObject("data"))

        val account = Account(accountInfo.email, BuildConfig.APPLICATION_ID)
        am.addAccountExplicitly(account, authCode.clientSecret, Bundle())
        am.setUserData(account, "token_type", tokenType)
        am.setUserData(account, "refresh_token", refreshToken)
        am.setUserData(account, "server", authCode.server)
        am.setUserData(account, "client_id", authCode.clientId)
        am.setUserData(account, "client_secret", authCode.clientSecret)
        am.setUserData(account, "code", authCode.code)
        am.setAuthToken(account, "firefly", accessToken)
    }

    @WorkerThread
    suspend fun login(
        authCode: AuthCode,
    ) {
        httpClient = httpClient.newBuilder()
            .addInterceptor(AuthInterceptor(authCode.clientId, authCode.clientSecret))
            .build()

        val url = URL(authCode.server)
        val uri = Uri.Builder()
            .scheme(url.protocol)
            .authority(url.authority)
            .appendEncodedPath("oauth/token")
            .build()
        val response = postRequest(
            uri,
            mapOf(
                "grant_type" to "authorization_code",
                "code" to authCode.code!!,
                "client_id" to authCode.clientId,
                "client_secret" to authCode.clientSecret,
                "redirect_uri" to "app://com.arnyminerz.wallet",
            ),
        )
        val json = JSONObject(response)
        if (json.has("hint") && json.getString("hint").contains("Authorization code has expired", true))
            throw IllegalStateException("The authorization code has expired.")
        if (json.has("token_type") && json.has("access_token") && json.has("refresh_token")) {
            val tokenType = json.getString("token_type")
            val accessToken = json.getString("access_token")
            val refreshToken = json.getString("refresh_token")

            Timber.i("Adding account...")
            addAccount(authCode, tokenType, accessToken, refreshToken)

            return
        }
        throw IOException("Could not handle server response. Response: $response")
    }

    @WorkerThread
    suspend fun invalidateCode(context: Context, authCode: AuthCode) {
        context.popFromPreference(authCodes, authCode.toJson().toString())
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
            val url = "$server/oauth/authorize?response_type=code&client_id=$clientId&client_secret=$clientSecret&redirect_uri=app://com.arnyminerz.wallet"
            val builder = CustomTabsIntent.Builder()
                .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
            val customTabsIntent = builder.build()

            context.setPreferences(
                tempServer to server,
                tempClientId to clientId,
                tempClientSecret to clientSecret,
            )

            customTabsIntent.launchUrl(context, Uri.parse(url))
        }
    }

    private suspend fun postRequest(uri: Uri, requestBody: Map<String, String>) = suspendCoroutine { c ->
        val body = (requestBody.map { (key, value) -> "$key=${URLEncoder.encode(value, "utf-8")}" }.joinToString("&"))
        Timber.d("Making POST request to: $uri. Body: $body")
        val request = Request.Builder()
            .url(uri.toString())
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .post(body.toRequestBody(CONTENT_TYPE))
            .build()
        httpClient
            .newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) { c.resume(response.body!!.string()) }
                override fun onFailure(call: Call, e: IOException) { c.resumeWithException(e) }
            })
    }

    private suspend fun getRequest(uri: Uri, headers: Map<String, String>) = suspendCoroutine { c ->
        Timber.d("Making GET request to: $uri. Headers: $headers")
        val request = Request.Builder()
            .url(uri.toString())
            .addHeader("User-Agent", "${BuildConfig.APPLICATION_ID}/${BuildConfig.VERSION_NAME}")
            .addHeader("Accept", "*/*")
            .apply { headers.toList().forEach { (k, v) -> addHeader(k, v) } }
            .get()
            .build()
        httpClient
            .newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) { c.resume(response.body!!.string()) }
                override fun onFailure(call: Call, e: IOException) { c.resumeWithException(e) }
            })
    }
}
