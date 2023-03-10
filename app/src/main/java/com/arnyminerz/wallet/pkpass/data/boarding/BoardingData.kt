package com.arnyminerz.wallet.pkpass.data.boarding

import com.arnyminerz.wallet.pkpass.data.Field
import com.arnyminerz.wallet.utils.JsonSerializable
import com.arnyminerz.wallet.utils.JsonSerializer
import com.arnyminerz.wallet.utils.map
import com.arnyminerz.wallet.utils.toJSONArray
import org.json.JSONObject

data class BoardingData(
    val transitType: TransitType,
    val headerFields: List<Field>,
    val primaryFields: List<Field>,
    val secondaryFields: List<Field>,
    val auxiliaryFields: List<Field>,
    val backFields: List<Field>,
) : JsonSerializable {
    companion object : JsonSerializer<BoardingData> {
        override fun fromJSON(json: JSONObject): BoardingData = BoardingData(
            json.getString("transitType").let { TransitType.valueOf(it) },
            json.getJSONArray("headerFields").map { Field.fromJSON(it) },
            json.getJSONArray("primaryFields").map { Field.fromJSON(it) },
            json.getJSONArray("secondaryFields").map { Field.fromJSON(it) },
            json.getJSONArray("auxiliaryFields").map { Field.fromJSON(it) },
            json.getJSONArray("backFields").map { Field.fromJSON(it) },
        )
    }

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("transitType", transitType.name)
        put("headerFields", headerFields.toJSONArray { it.toJSON() })
        put("primaryFields", primaryFields.toJSONArray { it.toJSON() })
        put("secondaryFields", secondaryFields.toJSONArray { it.toJSON() })
        put("auxiliaryFields", auxiliaryFields.toJSONArray { it.toJSON() })
        put("backFields", backFields.toJSONArray { it.toJSON() })
    }
}
