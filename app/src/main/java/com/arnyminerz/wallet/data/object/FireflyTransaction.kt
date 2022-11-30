package com.arnyminerz.wallet.data.`object`

import com.arnyminerz.wallet.utils.*
import com.arnyminerz.wallet.utils.serializer.*
import org.json.JSONObject
import java.util.Date

data class FireflyTransaction(
    val user: Long,
    val id: Long,
    val type: String,
    val date: Date,
    val order: Long,
    val currency: FireflyCurrency,
    val amount: Double,
    val foreignCurrency: FireflyCurrency?,
    val foreignAmount: Double?,
    val description: String?,
    val source: FireflyTarget?,
    val destination: FireflyTarget?,
    val budget: FireflyIdName?,
    val category: FireflyIdName?,
    val bill: FireflyIdName?,
    val reconciled: Boolean,
    val notes: String?,
    val tags: List<String>,
    val internalReference: String?,
    val externalId: String?,
    val externalUrl: String?,
    val originalSource: String?,
    val recurrence: FireflyRecurrence?,
    val sepa: FireflySepa?,
    val interestDate: Date?,
    val bookDate: Date?,
    val processDate: Date?,
    val dueDate: Date?,
    val paymentDate: Date?,
    val invoiceDate: Date?,
    val geoRef: FireflyGeoRef?,
) : JsonSerializable() {
    companion object : FireflyJsonSerializer<FireflyTransaction>, JsonSerializer<FireflyTransaction> {
        override fun fromJson(json: JSONObject, prefix: String): FireflyTransaction = FireflyTransaction(
            json.getLong("user"),
            json.getLong("transaction_journal_id"),
            json.getString("type"),
            json.getDate("date", FIREFLY_DATE_FORMATTER),
            json.getLong("order"),
            json.serializeInline(FireflyCurrency.Companion, "currency"),
            json.getDouble("amount"),
            json.serializeInlineOrNull(FireflyCurrency.Companion, "foreign"),
            json.getDoubleOrNull("foreign_amount"),
            json.getStringOrNull("description"),
            json.serializeInlineOrNull(FireflyTarget.Companion, "source"),
            json.serializeInlineOrNull(FireflyTarget.Companion, "destination"),
            json.serializeInlineOrNull(FireflyIdName.Companion, "budget"),
            json.serializeInlineOrNull(FireflyIdName.Companion, "category"),
            json.serializeInlineOrNull(FireflyIdName.Companion, "bill"),
            json.getBoolean("reconciled"),
            json.getStringOrNull("notes"),
            json.getStringArray("tags"),
            json.getStringOrNull("internal_reference"),
            json.getStringOrNull("external_id"),
            json.getStringOrNull("external_url"),
            json.getString("original_source"),
            json.serializeInlineOrNull(FireflyRecurrence.Companion),
            json.serializeInlineOrNull(FireflySepa.Companion),
            json.getDateOrNull("interest_date", FIREFLY_DATE_FORMATTER),
            json.getDateOrNull("book_date", FIREFLY_DATE_FORMATTER),
            json.getDateOrNull("process_date", FIREFLY_DATE_FORMATTER),
            json.getDateOrNull("due_date", FIREFLY_DATE_FORMATTER),
            json.getDateOrNull("payment_date", FIREFLY_DATE_FORMATTER),
            json.getDateOrNull("invoice_date", FIREFLY_DATE_FORMATTER),
            json.serializeInlineOrNull(FireflyGeoRef.Companion),
        )

        override fun fromJson(json: JSONObject): FireflyTransaction = FireflyTransaction(
            json.getLong("user"),
            json.getLong("id"),
            json.getString("type"),
            json.getDate("date", FIREFLY_DATE_FORMATTER),
            json.getLong("order"),
            json.getSerializable("currency", FireflyCurrency.Companion),
            json.getDouble("amount"),
            json.getSerializableOrNull("foreignCurrency", FireflyCurrency.Companion),
            json.getDoubleOrNull("foreignAmount"),
            json.getStringOrNull("description"),
            json.getSerializableOrNull("source", FireflyTarget.Companion),
            json.getSerializableOrNull("destination", FireflyTarget.Companion),
            json.getSerializableOrNull("budget", FireflyIdName.Companion),
            json.getSerializableOrNull("category", FireflyIdName.Companion),
            json.getSerializableOrNull("bill", FireflyIdName.Companion),
            json.getBoolean("reconciled"),
            json.getStringOrNull("notes"),
            json.getStringArray("tags"),
            json.getStringOrNull("internalReference"),
            json.getStringOrNull("externalId"),
            json.getStringOrNull("externalUrl"),
            json.getStringOrNull("originalSource"),
            json.getSerializableOrNull("recurrence", FireflyRecurrence.Companion),
            json.getSerializableOrNull("sepa", FireflySepa.Companion),
            json.getDateOrNull("interestDate", FIREFLY_DATE_FORMATTER),
            json.getDateOrNull("bookDate", FIREFLY_DATE_FORMATTER),
            json.getDateOrNull("processDate", FIREFLY_DATE_FORMATTER),
            json.getDateOrNull("dueDate", FIREFLY_DATE_FORMATTER),
            json.getDateOrNull("paymentDate", FIREFLY_DATE_FORMATTER),
            json.getDateOrNull("invoiceDate", FIREFLY_DATE_FORMATTER),
            json.getSerializableOrNull("geoRef", FireflyGeoRef.Companion),
        )
    }

    /**
     * Returns the [amount] field, formatted with the currency's decimal places, and its symbol.
     * @author Arnau Mora
     * @since 20221128
     */
    val amountString: String = currency.format(amount)

    override val toJson: JSONObject.() -> Unit = {
        put("user", user)
        put("id", id)
        put("type", type)
        putDate("date", date, FIREFLY_DATE_FORMATTER)
        put("order", order)
        putSerializable("currency", currency)
        put("amount", amount)
        putSerializable("foreignCurrency", foreignCurrency)
        put("foreignAmount", foreignAmount)
        put("description", description)
        putSerializable("source", source)
        put("destination", destination)
        putSerializable("budget", budget)
        putSerializable("category", category)
        putSerializable("bill", bill)
        put("reconciled", reconciled)
        put("notes", notes)
        putArray("tags", tags)
        put("internalReference", internalReference)
        put("externalId", externalId)
        put("externalUrl", externalUrl)
        put("originalSource", originalSource)
        putSerializable("recurrence", recurrence)
        putSerializable("sepa", sepa)
        putDate("interestDate", interestDate, FIREFLY_DATE_FORMATTER)
        putDate("bookDate", bookDate, FIREFLY_DATE_FORMATTER)
        putDate("processDate", processDate, FIREFLY_DATE_FORMATTER)
        putDate("dueDate", dueDate, FIREFLY_DATE_FORMATTER)
        putDate("paymentDate", paymentDate, FIREFLY_DATE_FORMATTER)
        putDate("invoiceDate", invoiceDate, FIREFLY_DATE_FORMATTER)
        putSerializable("geoRef", geoRef)
    }
}
