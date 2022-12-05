package com.arnyminerz.wallet.exception

import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

@Suppress("MemberVisibilityCanBePrivate", "CanBeParameter")
open class HttpResponseException(
    message: String,
    val statusCode: Int,
    val body: String?,
) : IOException("$message. Status: $statusCode. Body: $body") {
    val json: JSONObject?
        get() = body
            ?.let {
                try {
                    JSONObject(it)
                } catch (_: JSONException) {
                    null
                }
            }
}
