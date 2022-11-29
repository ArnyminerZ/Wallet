package com.arnyminerz.wallet.utils.serializer

import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

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

fun <S: JsonSerializable> JSONObject.putSerializable(key: String, serializable: S?): JSONObject = put(key, serializable?.toJson())

fun <T, C: Collection<T>> JSONObject.putArray(key: String, collection: C): JSONObject = put(key, JSONArray(collection))

fun JSONObject.putDate(key: String, date: Date?, formatter: SimpleDateFormat): JSONObject = put(key, date?.let { formatter.format(it) })
