package com.arnyminerz.wallet.account

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

data class FireflyAccount(
    val id: Int,
    val createdAt: Date,
    val email: String,
    val role: String,
) {
    companion object {
        fun fromFireflyData(json: JSONObject) = json.getJSONObject("attributes").let { attrs ->
            FireflyAccount(
                json.getString("id").toInt(),
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()).parse(attrs.getString("created_at"))!!,
                attrs.getString("email"),
                attrs.getString("role"),
            )
        }
    }
}
