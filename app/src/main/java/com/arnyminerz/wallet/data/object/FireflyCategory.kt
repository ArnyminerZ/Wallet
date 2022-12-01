package com.arnyminerz.wallet.data.`object`

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.arnyminerz.wallet.utils.serializer.JsonSerializer
import org.json.JSONObject

@Entity(
    tableName = "ff_categories"
)
data class FireflyCategory(
    @PrimaryKey override val id: Long,
    val name: String,
): FireflyObject(id) {
    companion object: JsonSerializer<FireflyCategory> {
        override fun fromJson(json: JSONObject): FireflyCategory = FireflyCategory(
            json.getLong("id"),
            json.getString("name"),
        )
    }

    @Ignore
    override val toJson: JSONObject.() -> Unit = {
        put("id", id)
        put("name", name)
    }
}