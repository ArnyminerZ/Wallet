package com.arnyminerz.wallet.database.data

import com.arnyminerz.wallet.utils.serializer.FireflyJsonSerializer
import com.arnyminerz.wallet.utils.serializer.JsonSerializable
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import org.json.JSONObject

data class FireflyIdName(
    val id: Long,
    val name: String,
) : JsonSerializable() {
    companion object : JsonSerializer<FireflyIdName>, FireflyJsonSerializer<FireflyIdName> {
        override fun fromJson(json: JSONObject): FireflyIdName = FireflyIdName(
            json.getLong("id"),
            json.getString("name"),
        )

        override fun fromJson(json: JSONObject, prefix: String): FireflyIdName = FireflyIdName(
            json.getLong("${prefix}_id"),
            json.getString("${prefix}_name"),
        )
    }

    override val toJson: JSONObject.() -> Unit = {
        put("id", id)
        put("name", name)
    }
}
