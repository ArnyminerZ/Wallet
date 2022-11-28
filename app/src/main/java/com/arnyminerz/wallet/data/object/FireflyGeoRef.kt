package com.arnyminerz.wallet.data.`object`

import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import org.json.JSONObject

data class FireflyGeoRef(
    val latitude: Double,
    val longitude: Double,
    val zoomLevel: Long,
) {
    companion object: JsonSerializer<FireflyGeoRef> {
        override fun fromJson(json: JSONObject): FireflyGeoRef = FireflyGeoRef(
            json.getDouble("latitude"),
            json.getDouble("longitude"),
            json.getLong("zoom_level"),
        )
    }
}
