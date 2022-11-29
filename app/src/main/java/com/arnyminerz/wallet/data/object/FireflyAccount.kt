package com.arnyminerz.wallet.data.`object`

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arnyminerz.wallet.utils.*
import com.arnyminerz.wallet.utils.serializer.FireflyJsonSerializer
import org.json.JSONObject
import java.util.*

@Entity(
    tableName = "ff_accounts"
)
data class FireflyAccount(
    @PrimaryKey val id: Long,
    val createdAt: Date,
    val active: Boolean,
    val order: Long?,
    val name: String,
    val type: String,
    val role: String?,
    val currency: FireflyCurrency,
    val balance: Double,
    val balanceDate: Date,
    val notes: String?,
    val monthlyPaymentDate: Date?,
    val creditCardType: String?,
    val accountNumber: String?,
    val iban: String?,
    val bic: String?,
    val virtualBalance: Double?,
    val openingBalance: Double?,
    val openingBalanceDate: Date?,
    val liabilityType: String?,
    val liabilityDirection: String?,
    val interest: Double?,
    val interestPeriod: String?,
    val currentDebt: String?,
    val includeNetWorth: Boolean,
    val geoRef: FireflyGeoRef?,
) {
    companion object: FireflyJsonSerializer<FireflyAccount> {
        override fun fromJson(json: JSONObject, prefix: String): FireflyAccount = json.getJSONObject("attributes").let { attrs ->
            FireflyAccount(
                json.getLong("id"),
                attrs.getDate("created_at", FIREFLY_DATE_FORMATTER)!!,
                attrs.getBoolean("active"),
                attrs.getLongOrNull("order"),
                attrs.getString("name"),
                attrs.getString("type"),
                attrs.getStringOrNull("role"),
                attrs.serializeInline(FireflyCurrency.Companion),
                attrs.getDouble("current_balance"),
                attrs.getDate("current_balance_date", FIREFLY_DATE_FORMATTER)!!,
                attrs.getStringOrNull("notes"),
                attrs.getDateOrNull("monthly_payment_date", FIREFLY_DATE_FORMATTER),
                attrs.getStringOrNull("credit_card_type"),
                attrs.getStringOrNull("account_number")?.takeIf { it.isNotBlank() },
                attrs.getStringOrNull("iban"),
                attrs.getStringOrNull("bic")?.takeIf { it.isNotBlank() },
                attrs.getDoubleOrNull("virtual_balance")?.takeIf { it > 0 },
                attrs.getDoubleOrNull("opening_balance")?.takeIf { it > 0 },
                attrs.getDateOrNull("opening_balance_date", FIREFLY_DATE_FORMATTER),
                attrs.getStringOrNull("liability_type"),
                attrs.getStringOrNull("liability_direction"),
                attrs.getDoubleOrNull("interest"),
                attrs.getStringOrNull("interest_period"),
                attrs.getStringOrNull("current_debt"),
                attrs.getBoolean("include_net_worth"),
                attrs.serializeInlineOrNull(FireflyGeoRef.Companion),
            )
        }
    }
}
