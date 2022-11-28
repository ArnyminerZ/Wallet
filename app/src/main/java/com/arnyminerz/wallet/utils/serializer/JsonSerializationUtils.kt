package com.arnyminerz.wallet.utils.serializer

import org.json.JSONObject

/**
 * Serializes the JSONObject using the given serializer.
 * @author Arnau Mora
 * @since 20221128
 * @param serializer The serializer to use.
 */
fun <T, S: JsonSerializer<T>> JSONObject.serialize(serializer: S) = serializer.fromJson(this)

/**
 * Serializes the JSONObject using the given serializer.
 * @author Arnau Mora
 * @since 20221128
 * @param serializer The serializer to use.
 */
fun <T, S: FireflyJsonSerializer<T>> JSONObject.serialize(serializer: S, prefix: String) = serializer.fromJson(this, prefix)

fun JSONObject.putSerializable(key: String, serializable: JsonSerializable) = put(key, serializable.toJson())
