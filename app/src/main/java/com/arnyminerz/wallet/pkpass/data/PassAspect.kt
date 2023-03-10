package com.arnyminerz.wallet.pkpass.data

import androidx.annotation.ColorInt
import com.arnyminerz.wallet.utils.JsonSerializable
import com.arnyminerz.wallet.utils.JsonSerializer
import com.arnyminerz.wallet.utils.getIntOrNull
import org.json.JSONObject

data class PassAspect(
    @ColorInt val labelColor: Int?,
    @ColorInt val foregroundColor: Int?,
    @ColorInt val backgroundColor: Int?,
): JsonSerializable {
    companion object: JsonSerializer<PassAspect> {
        override fun fromJSON(json: JSONObject): PassAspect = PassAspect(
            json.getIntOrNull("labelColor"),
            json.getIntOrNull("foregroundColor"),
            json.getIntOrNull("backgroundColor"),
        )
    }

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("labelColor", labelColor)
        put("foregroundColor", foregroundColor)
        put("backgroundColor", backgroundColor)
    }
}
