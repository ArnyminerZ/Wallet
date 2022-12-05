package com.arnyminerz.wallet.database.local

import androidx.room.TypeConverter
import com.arnyminerz.wallet.database.data.FireflyCurrency
import com.arnyminerz.wallet.database.data.FireflyGeoRef
import org.json.JSONObject
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun fromCurrency(value: FireflyCurrency?): String? = value?.toJson()?.toString()

    @TypeConverter
    fun toCurrency(value: String?): FireflyCurrency? = value?.let { FireflyCurrency.fromJson(JSONObject(it)) }

    @TypeConverter
    fun fromGeoRef(value: FireflyGeoRef?): String? = value?.toJson()?.toString()

    @TypeConverter
    fun toGeoRef(value: String?): FireflyGeoRef? = value?.let { FireflyGeoRef.fromJson(JSONObject(it)) }
}