package com.arnyminerz.wallet.database.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.arnyminerz.wallet.utils.serializer.FireflyJsonSerializer
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import org.json.JSONObject

@Entity(
    tableName = "ff_currencies"
)
data class FireflyCurrency(
    @PrimaryKey override val id: Long,
    val code: String,
    val symbol: String,
    val decimalPlaces: Long,
    val default: Boolean,
) : FireflyObject(id) {
    companion object : JsonSerializer<FireflyCurrency>, FireflyJsonSerializer<FireflyCurrency> {
        override fun fromJson(json: JSONObject): FireflyCurrency = FireflyCurrency(
            json.getLong("id"),
            json.getString("code"),
            json.getString("symbol"),
            json.getLong("decimal_places"),
            json.getBoolean("default"),
        )

        override fun fromJson(json: JSONObject, prefix: String): FireflyCurrency = FireflyCurrency(
            json.getLong("${prefix}_id"),
            json.getString("${prefix}_code"),
            json.getString("${prefix}_symbol"),
            json.getLong("${prefix}_decimal_places"),
            false,
        )
    }

    @Ignore
    override val toJson: JSONObject.() -> Unit = {
        put("id", id)
        put("code", code)
        put("symbol", symbol)
        put("decimal_places", decimalPlaces)
        put("default", default)
    }

    fun format(amount: Double, addSymbol: Boolean = true) = if (addSymbol)
        "%.${decimalPlaces}f $symbol".format(amount)
    else
        "%.${decimalPlaces}f".format(amount)

    val icon: ImageVector
        get() = when (symbol) {
            "€" -> Icons.Rounded.EuroSymbol
            "¥" -> Icons.Rounded.CurrencyYen
            "$" -> Icons.Rounded.AttachMoney
            "£" -> Icons.Rounded.CurrencyPound
            "v" -> Icons.Rounded.CurrencyRuble
            "元" -> Icons.Rounded.CurrencyYuan
            "₺" -> Icons.Rounded.CurrencyLira
            "₣" -> Icons.Rounded.CurrencyFranc
            else -> Icons.Rounded.Money
        }
}
