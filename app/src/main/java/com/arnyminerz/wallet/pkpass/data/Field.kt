package com.arnyminerz.wallet.pkpass.data

import com.arnyminerz.wallet.utils.JsonSerializable
import com.arnyminerz.wallet.utils.JsonSerializer
import com.arnyminerz.wallet.utils.getStringOrNull
import org.json.JSONObject

data class Field(
    val key: String,
    val label: String,
    val value: String,
    val changeValue: String? = null,
): JsonSerializable {
    companion object: JsonSerializer<Field> {
        override fun fromJSON(json: JSONObject): Field = Field(
            json.getString("key"),
            json.getString("label"),
            json.getString("value"),
            json.getStringOrNull("changeValue"),
        )
    }

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("key", key)
        put("label", label)
        put("value", value)
        put("changeValue", changeValue)
    }
}

fun List<Field>.getField(key: String, ignoreCase: Boolean = false): Field? {
    for (field in this)
        if (field.key.equals(key, ignoreCase))
            return field
    return null
}
