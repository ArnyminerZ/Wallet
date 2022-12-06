package com.arnyminerz.wallet.database.data

import com.arnyminerz.wallet.utils.Sampleable
import com.arnyminerz.wallet.utils.serializer.JsonSerializable
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import com.arnyminerz.wallet.utils.serializer.putSerializable
import com.arnyminerz.wallet.utils.serializer.serialize
import org.json.JSONObject

data class FireflySummary(
    val balance: FireflySummaryEntry,
    val spent: FireflySummaryEntry,
    val earned: FireflySummaryEntry,
    val leftToSpend: FireflySummaryEntry?,
    val netWorth: FireflySummaryEntry,
): JsonSerializable() {
    companion object: JsonSerializer<FireflySummary>, Sampleable<FireflySummary> {
        override val SAMPLE: FireflySummary = FireflySummary(
            FireflySummaryEntry.SAMPLE,
            FireflySummaryEntry.SAMPLE.copy(value = -318.0),
            FireflySummaryEntry.SAMPLE,
            FireflySummaryEntry.SAMPLE,
            FireflySummaryEntry.SAMPLE,
        )

        fun fromFirefly(json: JSONObject) = FireflySummary(
            json.keys().asSequence().find { it.startsWith("balance-") }!!.let { FireflySummaryEntry.fromSummaryJson(json.getJSONObject(it), MetadataOptions.Balance) },
            json.keys().asSequence().find { it.startsWith("spent-") }!!.let { FireflySummaryEntry.fromSummaryJson(json.getJSONObject(it), MetadataOptions.Spent) },
            json.keys().asSequence().find { it.startsWith("earned-") }!!.let { FireflySummaryEntry.fromSummaryJson(json.getJSONObject(it), MetadataOptions.Earned) },
            json.keys().asSequence().find { it.startsWith("left-to-spend-") }?.let { FireflySummaryEntry.fromSummaryJson(json.getJSONObject(it), MetadataOptions.LeftToSpend) },
            json.keys().asSequence().find { it.startsWith("net-worth-") }!!.let { FireflySummaryEntry.fromSummaryJson(json.getJSONObject(it), MetadataOptions.NetWorth) },
        )

        override fun fromJson(json: JSONObject): FireflySummary = FireflySummary(
            json.getJSONObject("balance").serialize(FireflySummaryEntry.Companion),
            json.getJSONObject("spent").serialize(FireflySummaryEntry.Companion),
            json.getJSONObject("earned").serialize(FireflySummaryEntry.Companion),
            json.getJSONObject("left_to_spend").serialize(FireflySummaryEntry.Companion),
            json.getJSONObject("net_worth").serialize(FireflySummaryEntry.Companion),
        )
    }

    override val toJson: JSONObject.() -> Unit = {
        putSerializable("balance", balance)
        putSerializable("spent", balance)
        putSerializable("earned", balance)
        putSerializable("left_to_spend", balance)
        putSerializable("net_worth", balance)
    }
}
