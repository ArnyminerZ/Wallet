package com.arnyminerz.wallet.data.`object`

import com.arnyminerz.wallet.utils.getStringOrNull
import com.arnyminerz.wallet.utils.serializer.FireflyJsonSerializer
import org.json.JSONObject

data class FireflyTarget(
    val id: Long,
    val name: String,
    val iban: String?,
    val type: String,
) {
    companion object: FireflyJsonSerializer<FireflyTarget> {
        override fun fromJson(json: JSONObject, prefix: String): FireflyTarget = FireflyTarget(
            json.getLong("${prefix}_id"),
            json.getString("${prefix}_name"),
            json.getStringOrNull("${prefix}_iban"),
            json.getString("${prefix}_type"),
        )
    }
}
