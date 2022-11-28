package com.arnyminerz.wallet.exception

import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

open class HttpResponseException(message: String, val statusCode: Int, val body: ResponseBody?) : IOException("$message. Status: $statusCode. Body: ${body?.string()}") {
    val json: JSONObject?
        get() = body
            ?.string()
            ?.let {
                try {
                    JSONObject(it)
                } catch (e: JSONException) {
                    null
                }
            }
}
