package com.arnyminerz.wallet.data.`object`

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.arnyminerz.wallet.utils.*
import com.arnyminerz.wallet.utils.serializer.FireflyJsonSerializer
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import com.arnyminerz.wallet.utils.serializer.putDate
import com.arnyminerz.wallet.utils.serializer.putSerializable
import org.json.JSONObject
import java.util.*

@Entity(
    tableName = "ff_accounts"
)
data class FireflyAccount(
    @PrimaryKey override val id: Long,
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
) : FireflyObject(id) {
    companion object : FireflyJsonSerializer<FireflyAccount>, JsonSerializer<FireflyAccount> {
        override fun fromJson(json: JSONObject, prefix: String): FireflyAccount = json.getJSONObject("attributes").let { attrs ->
            FireflyAccount(
                json.getLong("id"),
                attrs.getDate("created_at", FIREFLY_DATE_FORMATTER),
                attrs.getBoolean("active"),
                attrs.getLongOrNull("order"),
                attrs.getString("name"),
                attrs.getString("type"),
                attrs.getStringOrNull("role"),
                attrs.serializeInline(FireflyCurrency.Companion, "currency"),
                attrs.getDouble("current_balance"),
                attrs.getDate("current_balance_date", FIREFLY_DATE_FORMATTER),
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

        override fun fromJson(json: JSONObject): FireflyAccount = FireflyAccount(
            json.getLong("id"),
            json.getDate("created_at", FIREFLY_SHORT_DATE),
            json.getBoolean("active"),
            json.getLongOrNull("order"),
            json.getString("name"),
            json.getString("type"),
            json.getStringOrNull("role"),
            json.getSerializable("currency", FireflyCurrency.Companion),
            json.getDouble("balance"),
            json.getDate("balance_date", FIREFLY_SHORT_DATE),
            json.getStringOrNull("notes"),
            json.getDateOrNull("monthly_payment_date", FIREFLY_SHORT_DATE),
            json.getStringOrNull("credit_card_type"),
            json.getStringOrNull("account_number"),
            json.getStringOrNull("iban"),
            json.getStringOrNull("bic"),
            json.getDoubleOrNull("virtual_balance"),
            json.getDouble("opening_balance"),
            json.getDateOrNull("opening_balance_date", FIREFLY_SHORT_DATE),
            json.getStringOrNull("liability_type"),
            json.getStringOrNull("liability_direction"),
            json.getDoubleOrNull("interest"),
            json.getStringOrNull("interest_period"),
            json.getStringOrNull("current_debt"),
            json.getBoolean("include_net_worth"),
            json.getSerializableOrNull("geo_ref", FireflyGeoRef.Companion),
        )
    }

    @Ignore
    override val toJson: JSONObject.() -> Unit = {
        put("id", id)
        putDate("created_at", createdAt, FIREFLY_SHORT_DATE)
        put("active", active)
        put("order", order)
        put("name", name)
        put("type", type)
        put("role", role)
        putSerializable("currency", currency)
        put("balance", balance)
        putDate("balance_date", balanceDate, FIREFLY_SHORT_DATE)
        put("notes", notes)
        putDate("monthly_payment_date", monthlyPaymentDate, FIREFLY_SHORT_DATE)
        put("credit_card_type", creditCardType)
        put("account_number", accountNumber)
        put("iban", iban)
        put("bic", bic)
        put("virtual_balance", virtualBalance)
        put("opening_balance", openingBalance)
        putDate("opening_balance_date", openingBalanceDate, FIREFLY_SHORT_DATE)
        put("liability_type", liabilityType)
        put("liability_direction", liabilityDirection)
        put("interest", interest)
        put("interest_period", interestPeriod)
        put("current_debt", currentDebt)
        put("include_net_worth", includeNetWorth)
        putSerializable("geo_ref", geoRef)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FireflyAccount

        if (id != other.id) return false
        if (createdAt != other.createdAt) return false
        if (active != other.active) return false
        if (order != other.order) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (role != other.role) return false
        if (currency != other.currency) return false
        if (balance != other.balance) return false
        if (balanceDate != other.balanceDate) return false
        if (notes != other.notes) return false
        if (monthlyPaymentDate != other.monthlyPaymentDate) return false
        if (creditCardType != other.creditCardType) return false
        if (accountNumber != other.accountNumber) return false
        if (iban != other.iban) return false
        if (bic != other.bic) return false
        if (virtualBalance != other.virtualBalance) return false
        if (openingBalance != other.openingBalance) return false
        if (openingBalanceDate != other.openingBalanceDate) return false
        if (liabilityType != other.liabilityType) return false
        if (liabilityDirection != other.liabilityDirection) return false
        if (interest != other.interest) return false
        if (interestPeriod != other.interestPeriod) return false
        if (currentDebt != other.currentDebt) return false
        if (includeNetWorth != other.includeNetWorth) return false
        if (geoRef != other.geoRef) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + active.hashCode()
        result = 31 * result + (order?.hashCode() ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (role?.hashCode() ?: 0)
        result = 31 * result + currency.hashCode()
        result = 31 * result + balance.hashCode()
        result = 31 * result + balanceDate.hashCode()
        result = 31 * result + (notes?.hashCode() ?: 0)
        result = 31 * result + (monthlyPaymentDate?.hashCode() ?: 0)
        result = 31 * result + (creditCardType?.hashCode() ?: 0)
        result = 31 * result + (accountNumber?.hashCode() ?: 0)
        result = 31 * result + (iban?.hashCode() ?: 0)
        result = 31 * result + (bic?.hashCode() ?: 0)
        result = 31 * result + (virtualBalance?.hashCode() ?: 0)
        result = 31 * result + (openingBalance?.hashCode() ?: 0)
        result = 31 * result + (openingBalanceDate?.hashCode() ?: 0)
        result = 31 * result + (liabilityType?.hashCode() ?: 0)
        result = 31 * result + (liabilityDirection?.hashCode() ?: 0)
        result = 31 * result + (interest?.hashCode() ?: 0)
        result = 31 * result + (interestPeriod?.hashCode() ?: 0)
        result = 31 * result + (currentDebt?.hashCode() ?: 0)
        result = 31 * result + includeNetWorth.hashCode()
        result = 31 * result + (geoRef?.hashCode() ?: 0)
        return result
    }


}
