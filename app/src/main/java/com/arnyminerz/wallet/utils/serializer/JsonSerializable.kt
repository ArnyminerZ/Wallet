package com.arnyminerz.wallet.utils.serializer

import org.json.JSONObject

abstract class JsonSerializable {
    protected abstract val toJson: JSONObject.() -> Unit

    fun toJson(): JSONObject = JSONObject().apply { toJson.invoke(this) }

    override fun toString(): String = toJson().toString()

    fun toString(indentSpaces: Int): String = toJson().toString(indentSpaces)
}
