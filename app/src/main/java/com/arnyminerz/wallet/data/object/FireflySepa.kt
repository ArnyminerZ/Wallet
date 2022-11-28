package com.arnyminerz.wallet.data.`object`

import com.arnyminerz.wallet.utils.serializer.JsonSerializable
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import org.json.JSONObject

data class FireflySepa(
    val cc: String,
    val ctOp: String,
    val ctId: String,
    val db: String,
    val country: String,
    val ep: String,
    val ci: String,
    val batchId: String,
): JsonSerializable() {
    companion object: JsonSerializer<FireflySepa> {
        override fun fromJson(json: JSONObject): FireflySepa = FireflySepa(
            json.getString("sepa_cc"),
            json.getString("sepa_ct_op"),
            json.getString("sepa_ct_id"),
            json.getString("sepa_db"),
            json.getString("sepa_country"),
            json.getString("sepa_ep"),
            json.getString("sepa_ci"),
            json.getString("sepa_batch_id"),
        )
    }

    override val toJson: JSONObject.() -> Unit = {
        put("sepa_cc", cc)
        put("sepa_ct_op", ctOp)
        put("sepa_ct_id", ctId)
        put("sepa_db", db)
        put("sepa_country", country)
        put("sepa_ep", ep)
        put("sepa_ci", ci)
        put("sepa_batch_id", batchId)
    }
}
