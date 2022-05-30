package com.arnyminerz.wallet.utils

import android.graphics.Color
import android.os.Build
import androidx.annotation.ColorInt
import com.arnyminerz.wallet.pkpass.data.Field
import org.json.JSONObject

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
