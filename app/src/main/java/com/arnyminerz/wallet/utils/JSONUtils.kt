package com.arnyminerz.wallet.utils

import android.graphics.Color
import android.os.Build
import androidx.annotation.ColorInt
import com.arnyminerz.wallet.pkpass.data.Field
import com.arnyminerz.wallet.utils.serializer.FireflyJsonSerializer
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import com.arnyminerz.wallet.utils.serializer.serialize
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

@ColorInt
fun JSONObject.getColor(key: String): Int {
    val value = getString(key).replace(" ", "")
    if (value.startsWith("#"))
        return Color.parseColor(value)
    else {
        val colors = value.substring(
            value.indexOf('(') + 1,
            value.indexOf(')'),
        ).split(',')
        if (value.startsWith("rgb", true)) {
            if (colors.isNotEmpty())
                return if (colors[0].contains('.')) // It's float
                    Color.rgb(
                        colors[0].toInt(),
                        colors[1].toInt(),
                        colors[2].toInt(),
                    )
                else
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Color.rgb(
                            colors[0].toFloat(),
                            colors[1].toFloat(),
                            colors[2].toFloat(),
                        )
                    } else {
                        Color.rgb(
                            (colors[0].toFloat() * 255).toInt(),
                            (colors[1].toFloat() * 255).toInt(),
                            (colors[2].toFloat() * 255).toInt(),
                        )
                    }
        } else if (value.startsWith("argb", true))
            if (colors.isNotEmpty())
                return if (colors[0].contains('.')) // It's float
                    Color.argb(
                        colors[0].toInt(),
                        colors[1].toInt(),
                        colors[2].toInt(),
                        colors[3].toInt(),
                    )
                else
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Color.argb(
                            colors[0].toFloat(),
                            colors[1].toFloat(),
                            colors[2].toFloat(),
                            colors[3].toFloat(),
                        )
                    } else {
                        Color.argb(
                            (colors[0].toFloat() * 255).toInt(),
                            (colors[1].toFloat() * 255).toInt(),
                            (colors[2].toFloat() * 255).toInt(),
                            (colors[2].toFloat() * 255).toInt(),
                        )
                    }
    }
    return Color.BLACK
}

fun JSONObject.getFields(key: String): List<Field> {
    val array = getJSONArray(key)
    val fields = arrayListOf<Field>()
    for (k in 0 until array.length()) {
        val obj = array.getJSONObject(k)
        fields.add(
            Field(
                obj.getString("key"),
                obj.getString("label"),
                obj.getString("value"),
            )
        )
    }
    return fields
}

fun <T : Any> JSONObject.getSerializable(key: String, serializer: JsonSerializer<T>) =
    getJSONObject(key).serialize(serializer)

fun <T : Any> JSONObject.serializeInline(serializer: JsonSerializer<T>) = serialize(serializer)

fun <T : Any> JSONObject.serializeInline(serializer: FireflyJsonSerializer<T>, prefix: String) = serialize(serializer, prefix)

fun JSONObject.getDate(key: String, formatter: SimpleDateFormat): Date =
    getString(key).let { formatter.parse(it)!! }

fun JSONObject.getStringArray(key: String) = getJSONArray(key).let { arr ->
    (0 until arr.length()).map { arr.getString(it) }
}

fun JSONObject.getStringOrNull(key: String): String? = key.takeIf { has(it) && !isNull(it) }?.let { getString(it) }

fun JSONObject.getDoubleOrNull(key: String): Double? = key.takeIf { has(it) && !isNull(it) }?.let { getDouble(it) }

fun JSONObject.getLongOrNull(key: String): Long? = key.takeIf { has(it) && !isNull(it) }?.let { getLong(it) }

fun <T : Any> JSONObject.getSerializableOrNull(key: String, serializer: JsonSerializer<T>): T? =
    key.takeIf { has(it) && !isNull(it) }?.let { getSerializable(it, serializer) }

fun JSONObject.getDateOrNull(key: String, formatter: SimpleDateFormat): Date? = key
    .takeIf { has(it) && !isNull(it) }
    ?.let { getString(key).let { formatter.parse(it)!! } }

fun <T : Any> JSONObject.serializeInlineOrNull(serializer: JsonSerializer<T>): T? = try {
    serializeInline(serializer)
} catch (e: JSONException) {
    null
}

fun <T : Any> JSONObject.serializeInlineOrNull(serializer: FireflyJsonSerializer<T>, prefix: String): T? = try {
    serializeInline(serializer, prefix)
} catch (e: JSONException) {
    null
}

/**
 * Maps all the values of the array into [JSONObject].
 * @author Arnau Mora
 * @since 20221129
 * @throws JSONException If a value is not a [JSONObject].
 */
val JSONArray.asJSONObjects: List<JSONObject>
    get() = (0 until length()).map { getJSONObject(it) }
