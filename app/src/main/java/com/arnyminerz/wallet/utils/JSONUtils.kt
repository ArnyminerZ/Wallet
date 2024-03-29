package com.arnyminerz.wallet.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Base64
import androidx.annotation.ColorInt
import com.arnyminerz.wallet.pkpass.data.Field
import java.io.ByteArrayOutputStream
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

interface JsonSerializable {
    fun toJSON(): JSONObject
}

interface JsonSerializer <T: Any> {
    fun fromJSON(json: JSONObject): T
}

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

fun JSONObject.putBitmap(key: String, bitmap: Bitmap) {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(BITMAP_WEBP, 100, outputStream)
    val bytes = outputStream.toByteArray()
    val encoded = Base64.encodeToString(bytes, Base64.DEFAULT)
    put(key, encoded)
}

fun JSONObject.getBitmap(key: String): Bitmap {
    val encoded = getStringOrNull(key)
    val bytes = Base64.decode(encoded, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun JSONObject.getStringOrNull(key: String): String? = try {
    if (has(key))
        getString(key)
    else
        null
} catch (e: JSONException) {
    null
}

fun JSONObject.getIntOrNull(key: String): Int? = try {
    if (has(key))
        getInt(key)
    else
        null
} catch (e: JSONException) {
    null
}

fun <T> List<T>.toJSONArray(mapper: (T) -> JSONObject): JSONArray = JSONArray().apply {
    (indices).forEach { put(mapper(this@toJSONArray[it])) }
}

fun <T> JSONArray.map(mapper: (JSONObject) -> T): List<T> = (0 until length()).map {
    mapper(getJSONObject(it))
}
