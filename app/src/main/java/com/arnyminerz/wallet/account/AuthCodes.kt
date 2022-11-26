package com.arnyminerz.wallet.account

import android.content.Context
import android.net.Uri
import com.arnyminerz.wallet.storage.authCodes
import com.arnyminerz.wallet.utils.getPreference
import com.arnyminerz.wallet.utils.safeMap
import com.arnyminerz.wallet.utils.serializer.JsonSerializable
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import kotlinx.coroutines.flow.first
import org.json.JSONException
import org.json.JSONObject

data class AuthCode(
    val server: String,
    val clientId: String,
    val clientSecret: String,
    val code: String?,
) : JsonSerializable() {
    companion object: JsonSerializer<AuthCode> {
        override fun fromJson(json: JSONObject): AuthCode = AuthCode(json)
    }

    val host: String?
        get() = Uri.parse(server).host

    @Throws(JSONException::class)
    constructor(json: JSONObject) : this(
        json.getString("server"),
        json.getString("client_id"),
        json.getString("client_secret"),
        json.takeIf { it.has("code") }?.let { json.getString("code") },
    )

    override val toJson: JSONObject.() -> Unit = {
        put("server", server)
        put("client_id", clientId)
        put("client_secret", clientSecret)
        put("code", code)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AuthCode

        if (server != other.server) return false
        if (clientId != other.clientId) return false
        if (clientSecret != other.clientSecret) return false

        if (code == null) return true

        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int {
        var result = server.hashCode()
        result = 31 * result + clientId.hashCode()
        result = 31 * result + clientSecret.hashCode()
        result = 31 * result + code.hashCode()
        return result
    }
}

/**
 * Returns all the stored auth codes.
 * @author Arnau Mora
 * @since 20221126
 */
suspend fun Context.getAuthCodes(): List<AuthCode> =
    getPreference(authCodes)
        .first()
        ?.safeMap { JSONObject(it) }
        ?.safeMap { AuthCode(it) }
        ?: emptyList()
