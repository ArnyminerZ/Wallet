package com.arnyminerz.wallet.utils.serializer

import org.json.JSONObject

interface JsonSerializer <T: Any> {
    fun fromJson(json: JSONObject): T
}