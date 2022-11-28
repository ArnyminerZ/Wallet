package com.arnyminerz.wallet.data.`object`

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.utils.Sampleable
import com.arnyminerz.wallet.utils.serializer.JsonSerializable
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import com.arnyminerz.wallet.utils.serializer.putSerializable
import com.arnyminerz.wallet.utils.serializer.serialize
import org.json.JSONObject

object MetadataOptions {
    val Balance = FireflySummaryEntry.Metadata(0, R.string.dashboard_month_balance, Icons.Rounded.Balance)
    val Spent = FireflySummaryEntry.Metadata(1, R.string.dashboard_month_spent, Icons.Rounded.Payments)
    val Earned = FireflySummaryEntry.Metadata(2, R.string.dashboard_month_earned, Icons.Rounded.Paid)
    val LeftToSpend = FireflySummaryEntry.Metadata(3, R.string.dashboard_month_left, Icons.Rounded.CreditScore)
    val NetWorth = FireflySummaryEntry.Metadata(4, R.string.dashboard_month_net, Icons.Rounded.Insights)

    @Throws(IllegalArgumentException::class)
    fun valueOf(id: Long) = when(id) {
        0L -> Balance
        1L -> Spent
        2L -> Earned
        3L -> LeftToSpend
        4L -> NetWorth
        else -> throw IllegalArgumentException("The given id ($id) is not valid.")
    }
}

data class FireflySummaryEntry(
    val value: Double,
    val currency: FireflyCurrency,
    val metadata: Metadata,
): JsonSerializable() {
    companion object: JsonSerializer<FireflySummaryEntry>, Sampleable<FireflySummaryEntry> {
        override val SAMPLE: FireflySummaryEntry
            get() = FireflySummaryEntry(
                1253.76,
                FireflyCurrency(
                    1,
                    "EUR",
                    "€",
                    2,
                ),
                MetadataOptions.Balance,
            )

        fun fromSummaryJson(json: JSONObject, metadata: Metadata) = FireflySummaryEntry(
            json.getDouble("monetary_value"),
            FireflyCurrency.fromJson(json),
            metadata,
        )

        override fun fromJson(json: JSONObject): FireflySummaryEntry = FireflySummaryEntry(
            json.getDouble("value"),
            json.getJSONObject("currency").serialize(FireflyCurrency.Companion),
            json.getLong("metadata_id").let { MetadataOptions.valueOf(it) },
        )
    }

    data class Metadata(
        val id: Long,
        @StringRes val title: Int,
        val icon: ImageVector,
    )

    override val toJson: JSONObject.() -> Unit = {
        put("value", value)
        putSerializable("currency", currency)
        put("metadata_id", metadata.id)
    }

    /**
     * Returns the [value] field, formatted with the currency's decimal places, and its symbol.
     * @author Arnau Mora
     * @since 20221128
     */
    val valueString: String = "%.${currency.decimalPlaces}f ${currency.symbol}".format(value)
}
