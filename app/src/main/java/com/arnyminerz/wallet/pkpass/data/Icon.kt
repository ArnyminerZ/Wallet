package com.arnyminerz.wallet.pkpass.data

import android.graphics.Bitmap
import com.arnyminerz.wallet.utils.JsonSerializable
import com.arnyminerz.wallet.utils.JsonSerializer
import com.arnyminerz.wallet.utils.getBitmap
import com.arnyminerz.wallet.utils.putBitmap
import org.json.JSONObject

data class Icon(
    val icon: Bitmap,
    val icon2x: Bitmap,
): JsonSerializable {
    companion object: JsonSerializer<Icon> {
        override fun fromJSON(json: JSONObject): Icon = Icon(
            json.getBitmap("icon"),
            json.getBitmap("icon2x"),
        )
    }

    override fun toJSON(): JSONObject = JSONObject().apply {
        putBitmap("icon", icon)
        putBitmap("icon2x", icon2x)
    }
}
