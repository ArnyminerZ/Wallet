package com.arnyminerz.wallet.data.remote

import android.accounts.Account
import android.content.Context
import androidx.annotation.WorkerThread
import com.arnyminerz.wallet.account.AccountHelper
import com.arnyminerz.wallet.data.`object`.*
import com.arnyminerz.wallet.exception.HttpResponseException
import com.arnyminerz.wallet.utils.asJSONObjects
import com.arnyminerz.wallet.utils.mapObjects
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

private val SHORT_DATE: SimpleDateFormat
    get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

class FireflyApi(context: Context, private val account: Account) {
    private val ah = AccountHelper.getInstance(context)

    internal val accountName = account.name

    @WorkerThread
    @Throws(JSONException::class, HttpResponseException::class)
    private suspend fun makeRequest(account: Account, endpoint: String, queryParameters: Map<String, String> = emptyMap()) =
        ah.getFireflyRequestData(account).let { data ->
            ah.getEndpoint(
                endpoint,
                data,
                queryParameters,
            )
        }

    @WorkerThread
    @Throws(JSONException::class, HttpResponseException::class)
    private suspend fun getObject(account: Account, endpoint: String, queryParameters: Map<String, String> = emptyMap()) =
        JSONObject(makeRequest(account, endpoint, queryParameters))

    @WorkerThread
    @Throws(JSONException::class, HttpResponseException::class)
    private suspend fun getArray(account: Account, endpoint: String, queryParameters: Map<String, String> = emptyMap()) =
        JSONArray(makeRequest(account, endpoint, queryParameters))

    /**
     * Runs [get] with multiple pages. Keeps querying pages until [limit] is reached, or the last one is reached.
     * @author Arnau Mora
     * @since 20221129
     * @param endpoint The endpoint of the API to make the request to. Must start with "/".
     * @param headers Extra headers to add to the request. Note that "page" will be overridden by the current page.
     * @param limit The maximum amount of elements to get.
     * @return A list of [JSONObject].
     */
    @WorkerThread
    @Throws(JSONException::class, HttpResponseException::class)
    private suspend fun getWithPages(endpoint: String, headers: Map<String, String>, limit: Int = Int.MAX_VALUE): List<JSONObject> {
        val builder = arrayListOf<JSONObject>()

        var page = 0
        do {
            val json = getObject(
                account,
                endpoint,
                headers.toMutableMap().apply { put("page", (++page).toString()) },
            )

            builder.addAll(
                json.getJSONArray("data").asJSONObjects
            )

            val meta = json.getJSONObject("meta")
            val pagination = meta.getJSONObject("pagination")
            val current = pagination.getLong("current_page")
            val total = pagination.getLong("total_pages")
            if (current >= total) break
        } while (builder.size < limit)

        return builder.subList(0, min(builder.size, limit))
    }

    /**
     * Gets the basic summary.
     * @author Arnau Mora
     * @since 20221128
     * @throws NullPointerException If the response doesn't contain the required fields.
     * @throws JSONException If there's an error while parsing the JSON response.
     */
    @Throws(
        JSONException::class,
        NullPointerException::class,
        HttpResponseException::class,
    )
    @WorkerThread
    suspend fun getMonthBalance() = getObject(
        account,
        "/summary/basic",
        mapOf(
            "start" to Calendar.getInstance()
                .apply { set(Calendar.DAY_OF_MONTH, 1) }
                .time
                .let { SHORT_DATE.format(it) },
            "end" to Calendar.getInstance()
                .apply { add(Calendar.MONTH, 1); set(Calendar.DAY_OF_MONTH, 1); add(Calendar.DATE, -1) }
                .time
                .let { SHORT_DATE.format(it) },
        ),
    ).let { FireflySummary.fromFirefly(it) }

    /**
     * Gets a list of transactions of the last month.
     * @author Arnau Mora
     * @since 20221128
     * @param limit The maximum amount of transactions to get.
     * @throws NullPointerException If the response doesn't contain the required fields.
     * @throws JSONException If there's an error while parsing the JSON response.
     */
    @Throws(
        JSONException::class,
        NullPointerException::class,
        HttpResponseException::class,
    )
    @WorkerThread
    suspend fun getTransactions(limit: Int = Int.MAX_VALUE): List<FireflyTransaction> =
        getWithPages(
            "/transactions",
            mapOf(
                "start" to Calendar.getInstance()
                    .apply { set(Calendar.DAY_OF_MONTH, 1) }
                    .time
                    .let { SHORT_DATE.format(it) },
                "end" to Calendar.getInstance()
                    .apply { add(Calendar.MONTH, 1); set(Calendar.DAY_OF_MONTH, 1); add(Calendar.DATE, -1) }
                    .time
                    .let { SHORT_DATE.format(it) },
            ),
            limit,
        ).map { group ->
            val attrs = group.getJSONObject("attributes")
            val transactions = attrs.getJSONArray("transactions")
            (0 until transactions.length())
                .map { transactions.getJSONObject(it) }
                .map { FireflyTransaction.fromJson(it, "") }
        }.flatten()

    /**
     * Gets a list of accounts.
     * @author Arnau Mora
     * @since 20221129
     * @param limit The maximum amount of transactions to get.
     * @throws NullPointerException If the response doesn't contain the required fields.
     * @throws JSONException If there's an error while parsing the JSON response.
     */
    @Throws(
        JSONException::class,
        NullPointerException::class,
        HttpResponseException::class,
    )
    @WorkerThread
    suspend fun getAccounts(limit: Int = Int.MAX_VALUE): List<FireflyAccount> =
        getWithPages(
            "/accounts",
            emptyMap(),
            limit,
        ).map { FireflyAccount.fromJson(it, "") }

    /**
     * Gets a list of currencies from the server.
     * @author Arnau Mora
     * @since 20221129
     * @param limit The maximum amount of transactions to get.
     * @throws NullPointerException If the response doesn't contain the required fields.
     * @throws JSONException If there's an error while parsing the JSON response.
     */
    @Throws(
        JSONException::class,
        NullPointerException::class,
        HttpResponseException::class,
    )
    @WorkerThread
    suspend fun getCurrencies(limit: Int = Int.MAX_VALUE): List<FireflyCurrency> =
        getWithPages(
            "/currencies",
            emptyMap(),
            limit,
        ).map { json ->
            val attrs = json.getJSONObject("attributes")
            attrs.put("id", json.getString("id"))
            FireflyCurrency.fromJson(attrs)
        }

    /**
     * Gets a list of all the categories.
     * @author Arnau Mora
     * @since 20221201
     * @throws NullPointerException If the response doesn't contain the required fields.
     * @throws JSONException If there's an error while parsing the JSON response.
     */
    @Throws(
        JSONException::class,
        NullPointerException::class,
        HttpResponseException::class,
    )
    @WorkerThread
    suspend fun getCategories(): List<FireflyCategory> =
        getArray(
            account,
            "/autocomplete/categories",
        ).mapObjects { FireflyCategory.fromJson(it) }
}

fun Account.api(context: Context) = FireflyApi(context, this)
