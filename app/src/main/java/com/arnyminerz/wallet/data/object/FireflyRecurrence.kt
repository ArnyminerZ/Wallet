package com.arnyminerz.wallet.data.`object`

import com.arnyminerz.wallet.utils.serializer.JsonSerializable
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import org.json.JSONObject

data class FireflyRecurrence(
    val id: Long,
    val total: Long,
    val count: Long,
): JsonSerializable() {
    companion object: JsonSerializer<FireflyRecurrence> {
        override fun fromJson(json: JSONObject): FireflyRecurrence = FireflyRecurrence(
            json.getLong("recurrence_id"),
            json.getLong("recurrence_total"),
            json.getLong("recurrence_count"),
        )
    }

    override val toJson: JSONObject.() -> Unit = {
        put("recurrence_", id)
        put("recurrence_total", total)
        put("recurrence_count", count)
    }
}
