package com.arnyminerz.wallet.data.`object`

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.arnyminerz.wallet.utils.serializer.FireflyJsonSerializer
import com.arnyminerz.wallet.utils.serializer.JsonSerializable
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import org.json.JSONObject

data class FireflyCurrency(
    val id: Long,
    val code: String,
    val symbol: String,
    val decimalPlaces: Long,
) : JsonSerializable() {
    companion object : JsonSerializer<FireflyCurrency>, FireflyJsonSerializer<FireflyCurrency> {
        override fun fromJson(json: JSONObject): FireflyCurrency = FireflyCurrency(
            json.getLong("currency_id"),
            json.getString("currency_code"),
            json.getString("currency_symbol"),
            json.getLong("currency_decimal_places"),
        )

        override fun fromJson(json: JSONObject, prefix: String): FireflyCurrency = FireflyCurrency(
            json.getLong("${prefix}_currency_id"),
            json.getString("${prefix}_currency_code"),
            json.getString("${prefix}_currency_symbol"),
            json.getLong("${prefix}_currency_decimal_places"),
        )
    }

    override val toJson: JSONObject.() -> Unit = {
        put("currency_id", id)
        put("currency_code", code)
        put("currency_symbol", symbol)
        put("currency_decimal_places", decimalPlaces)
    }

    fun format(amount: Double) = "%.${decimalPlaces}f $symbol".format(amount)

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
