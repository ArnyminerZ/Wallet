package com.arnyminerz.wallet.utils.serializer

import org.json.JSONObject

interface FireflyJsonSerializer <T: Any> {
    fun fromJson(json: JSONObject, prefix: String): T
}