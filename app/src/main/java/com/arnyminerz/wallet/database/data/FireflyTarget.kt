package com.arnyminerz.wallet.database.data

import com.arnyminerz.wallet.utils.getStringOrNull
import com.arnyminerz.wallet.utils.serializer.FireflyJsonSerializer
import com.arnyminerz.wallet.utils.serializer.JsonSerializable
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import org.json.JSONObject

data class FireflyTarget(
    val id: Long,
    val name: String,
    val iban: String?,
    val type: String,
): JsonSerializable() {
    companion object: FireflyJsonSerializer<FireflyTarget>, JsonSerializer<FireflyTarget> {
        override fun fromJson(json: JSONObject, prefix: String): FireflyTarget = FireflyTarget(
            json.getLong("${prefix}_id"),
            json.getString("${prefix}_name"),
            json.getStringOrNull("${prefix}_iban"),
            json.getString("${prefix}_type"),
        )

        override fun fromJson(json: JSONObject): FireflyTarget = FireflyTarget(
            json.getLong("id"),
            json.getString("name"),
            json.getStringOrNull("iban"),
            json.getString("type"),
        )
    }

    override val toJson: JSONObject.() -> Unit = {
        put("id", id)
        put("name", name)
        put("iban", iban)
        put("type", type)
    }
}
