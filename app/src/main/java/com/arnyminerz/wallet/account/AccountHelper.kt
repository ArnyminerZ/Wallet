package com.arnyminerz.wallet.account

import android.accounts.*
import android.app.Activity
import android.content.Context
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.annotation.MainThread
import androidx.annotation.RequiresPermission
import androidx.annotation.WorkerThread
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arnyminerz.wallet.BuildConfig
import com.arnyminerz.wallet.database.remote.FireflyRequestData
import com.arnyminerz.wallet.exception.HttpClientException
import com.arnyminerz.wallet.exception.HttpRedirectException
import com.arnyminerz.wallet.exception.HttpResponseException
import com.arnyminerz.wallet.exception.HttpServerException
import com.arnyminerz.wallet.network.interceptor.AuthHeaderInterceptor
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
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

val CONTENT_TYPE = "application/x-www-form-urlencoded".toMediaType()

class AccountHelper private constructor(context: Context) : OnAccountsUpdateListener {
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
        get() = am.getAccountsByType(BuildConfig.ACCOUNT_TYPE)

    val accountsLive = MutableLiveData<Array<out Account>>(emptyArray())

    private val accountsListener = arrayListOf<(accounts: Array<out Account>) -> Unit>()

    override fun onAccountsUpdated(accounts: Array<out Account>?) {
        accounts
            ?.onEach { Timber.i("Accounts: ${it.name} (${it.type})") }
            ?.filter { it.type == BuildConfig.ACCOUNT_TYPE }
            ?.toTypedArray()
            ?.let { list ->
                accountsLive.postValue(list)
                accountsListener.forEach { it(list) }
            }
    }

    /**
     * Should be called by the application. Starts listening for updates for the [accountsLive] [LiveData].
     * @author Arnau Mora
     * @since 20221128
     */
    @MainThread
    fun startListeningForAccounts(handler: Handler) {
        am.addOnAccountsUpdatedListener(this, handler, true)
    }

    /**
     * Should be called when the application is being destroyed. Stops updating [accountsLive].
     * Also removes all listeners added with [addListener].
     * @author Arnau Mora
     * @since 20221128
     */
    @MainThread
    fun stopListeningForAccounts() {
        am.removeOnAccountsUpdatedListener(this)
        accountsListener.clear()
    }

    /**
     * Adds a new listener, that will be called whenever accounts are updated.
     * @author Arnau Mora
     * @since 20221129
     * @param listener The block of code to run when the accounts are updated.
     * @return If the new listener has been added successfully.
     */
    fun addListener(@MainThread listener: (accounts: (Array<out Account>)) -> Unit) = accountsListener.add(listener)

    @WorkerThread
    suspend fun addAccount(
        authCode: AuthCode,
        tokenType: String,
        accessToken: String,
        refreshToken: String,
    ) {
        httpClient = httpClient.newBuilder()
            .addInterceptor(AuthHeaderInterceptor(tokenType, accessToken))
            .build()

        val url = URL(authCode.server)
        val rawAccountInfo = getRequest(
            Uri.Builder()
                .authority(url.authority)
                .scheme(url.protocol)
                .path("/api/v1/about/user")
                .build(),
            emptyMap(),
        )
        Timber.w("Account info: $rawAccountInfo")
        val accountInfoResponse = JSONObject(rawAccountInfo)
        val accountInfo = FireflyAccount.fromFireflyData(accountInfoResponse.getJSONObject("data"))

        val account = Account(accountInfo.email, BuildConfig.ACCOUNT_TYPE)
        am.addAccountExplicitly(account, authCode.clientSecret, Bundle())
        am.setUserData(account, "token_type", tokenType)
        am.setUserData(account, "refresh_token", refreshToken)
        am.setUserData(account, "server", authCode.server)
        am.setUserData(account, "client_id", authCode.clientId)
        am.setUserData(account, "client_secret", authCode.clientSecret)
        am.setUserData(account, "code", authCode.code)
        am.setAuthToken(account, BuildConfig.ACCOUNT_TYPE, accessToken)
    }

    @WorkerThread
    @RequiresPermission(value = "android.permission.AUTHENTICATE_ACCOUNTS")
    fun removeAccount(account: Account) = am.removeAccountExplicitly(account)

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
            val url = Uri.parse(server)
            val uri = Uri.Builder()
                .scheme(url.scheme)
                .authority(url.authority)
                .path("/oauth/authorize")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("client_secret", clientId)
                .appendQueryParameter("redirect_uri", "app://com.arnyminerz.wallet")
                .build()
            val builder = CustomTabsIntent.Builder()
                .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
            val customTabsIntent = builder.build()

            context.setPreferences(
                tempServer to server,
                tempClientId to clientId,
                tempClientSecret to clientSecret,
            )

            if (context !is Activity)
                customTabsIntent.intent.flags = FLAG_ACTIVITY_NEW_TASK

            customTabsIntent.launchUrl(context, uri)
        }
    }

    @WorkerThread
    suspend fun authoriseClient(context: Context, account: Account) = authoriseClient(
        context,
        am.getUserData(account, "server"),
        am.getUserData(account, "client_id"),
        am.getUserData(account, "client_secret"),
    )

    /**
     * Gets the AuthToken for the given accounts.
     * @author Arnau Mora
     * @since 20221128
     * @return An instance of [FireflyRequestData].
     * @throws AuthenticatorException When there was an issue with the authenticator,.
     * @throws OperationCanceledException When the operation has been cancelled externally for any reason.
     * @throws IOException Any exception related with IO, normally network issues.
     */
    @Throws(
        IOException::class,
        AuthenticatorException::class,
        OperationCanceledException::class,
    )
    @WorkerThread
    fun getFireflyRequestData(account: Account): FireflyRequestData {
        val server = am.getUserData(account, "server")
        val tokenType = am.getUserData(account, "token_type")
        val token = am.blockingGetAuthToken(account, BuildConfig.ACCOUNT_TYPE, true)
        return FireflyRequestData(server, tokenType, token)
    }

    /**
     * Makes a GET request to the desired endpoint.
     * @author Arnau Mora
     * @since 20221128
     * @param endpoint The endpoint to make the request to. Must start with "/", matches the contents after "/api/v1".
     * @param requestData Data for authorising the request.
     * @param queryParameters Some parameters to append to the query.
     * @see HttpRedirectException
     * @see HttpClientException
     * @see HttpServerException
     * @see getFireflyRequestData
     */
    @WorkerThread
    @Throws(HttpResponseException::class)
    suspend fun getEndpoint(endpoint: String, requestData: FireflyRequestData, queryParameters: Map<String, String> = emptyMap()): String {
        httpClient = httpClient.newBuilder()
            .addInterceptor(AuthHeaderInterceptor(requestData.authTokenType, requestData.authToken))
            .build()

        val url = URL(requestData.server)
        return getRequest(
            Uri.Builder()
                .authority(url.authority)
                .scheme(url.protocol)
                .path("/api/v1$endpoint")
                .apply { queryParameters.forEach { (k, v) -> appendQueryParameter(k, v) } }
                .build(),
            emptyMap(),
        )
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
                override fun onResponse(call: Call, response: Response) {
                    c.resume(response.body!!.string())
                }

                override fun onFailure(call: Call, e: IOException) {
                    c.resumeWithException(e)
                }
            })
    }

    /**
     * Makes a POST request to the server. Take into account that [httpClient] is used, and might have interceptors that override the headers given.
     * @author Arnau Mora
     * @since 20221128
     * @param uri The url to make the request to.
     * @param headers The headers to add to the request.
     * @throws HttpResponseException If there's an issue while making the request.
     * @see HttpRedirectException
     * @see HttpClientException
     * @see HttpServerException
     */
    @Throws(HttpResponseException::class)
    private suspend fun getRequest(uri: Uri, headers: Map<String, String>) = suspendCoroutine { c ->
        Timber.d("Making GET request to: $uri. Headers: $headers")
        val request = Request.Builder()
            .url(uri.toString())
            .addHeader("User-Agent", "${BuildConfig.APPLICATION_ID}/${BuildConfig.VERSION_NAME}")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/vnd.api+json")
            .apply { headers.toList().forEach { (k, v) -> addHeader(k, v) } }
            .method("GET", null)
            .build()
        httpClient
            .newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    when (response.code) {
                        in 200..299 -> c.resume(response.body!!.string())
                        in 300..399 -> c.resumeWithException(
                            HttpRedirectException(
                                "Request returned a non-success code.",
                                response.code,
                                response.body?.string(),
                            )
                        )
                        in 400..499 -> c.resumeWithException(
                            HttpClientException(
                                "Request returned a non-success code.",
                                response.code,
                                response.body?.string(),
                            )
                        )
                        in 500..599 -> c.resumeWithException(
                            HttpServerException(
                                "Request returned a non-success code.",
                                response.code,
                                response.body?.string(),
                            )
                        )
                        else -> c.resumeWithException(
                            HttpResponseException(
                                "Request returned a non-success code (${response.code})",
                                response.code,
                                response.body?.string(),
                            )
                        )
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    c.resumeWithException(e)
                }
            })
    }
}
