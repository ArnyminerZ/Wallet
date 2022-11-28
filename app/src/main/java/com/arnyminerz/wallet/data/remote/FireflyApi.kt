package com.arnyminerz.wallet.data.remote

import android.accounts.Account
import android.content.Context
import androidx.annotation.WorkerThread
import com.arnyminerz.wallet.account.AccountHelper
import com.arnyminerz.wallet.data.`object`.FireflySummary
import com.arnyminerz.wallet.data.`object`.FireflyTransaction
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

private val SHORT_DATE: SimpleDateFormat
    get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

class FireflyApi(context: Context, private val account: Account) {
    private val ah = AccountHelper.getInstance(context)

    @WorkerThread
    @Throws(JSONException::class)
    private suspend fun get(account: Account, endpoint: String, queryParameters: Map<String, String> = emptyMap()) =
        ah.getFireflyRequestData(account).let { data ->
            ah.getEndpoint(
                endpoint,
                data,
                queryParameters,
            )
        }.let { JSONObject(it) }

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
    )
    @WorkerThread
    suspend fun getMonthBalance() = get(
        account,
        "/summary/basic",
        mapOf(
            "start" to Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1) }.time.let { SHORT_DATE.format(it) },
            "end" to Date().let { SHORT_DATE.format(it) },
        ),
    ).let { FireflySummary.fromFirefly(it) }

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
    )
    @WorkerThread
    suspend fun getTransactions(limit: Int = Int.MAX_VALUE): List<FireflyTransaction> {
        val builder = arrayListOf<FireflyTransaction>()

        var page = 0
        do {
            val json = get(
                account,
                "/transactions",
                mapOf(
                    "start" to Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1) }.time.let { SHORT_DATE.format(it) },
                    "end" to Date().let { SHORT_DATE.format(it) },
                    "page" to (++page).toString(),
                ),
            )

            builder.addAll(
                json.getJSONArray("data").let { data ->
                    (0 until data.length()).map { groupIndex ->
                        val group = data.getJSONObject(groupIndex)
                        val attrs = group.getJSONObject("attributes")
                        val transactions = attrs.getJSONArray("transactions")
                        (0 until transactions.length())
                            .map { transactions.getJSONObject(it) }
                            .map { FireflyTransaction.fromJson(it, "") }
                    }
                }.flatten()
            )

            val meta = json.getJSONObject("meta")
            val pagination = meta.getJSONObject("pagination")
            val current = pagination.getLong("current_page")
            val total = pagination.getLong("total_pages")
            if (current >= total) break
        } while (builder.size < limit)

        return builder.subList(0, min(builder.size, limit))
    }
}

fun Account.api(context: Context) = FireflyApi(context, this)
