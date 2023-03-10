package com.arnyminerz.wallet.database

import androidx.room.TypeConverter
import com.arnyminerz.wallet.pkpass.data.Barcode
import com.arnyminerz.wallet.pkpass.data.PassAspect
import com.arnyminerz.wallet.pkpass.data.boarding.BoardingData
import org.json.JSONObject

class Converters {
    @TypeConverter
    fun fromPassAspect(value: PassAspect?): String? = value?.toJSON()?.toString()
    @TypeConverter
    fun toPassAspect(value: String?): PassAspect? = value?.let { PassAspect.fromJSON(JSONObject(it)) }

    @TypeConverter
    fun fromBarcode(value: Barcode?): String? = value?.toJSON()?.toString()
    @TypeConverter
    fun toBarcode(value: String?): Barcode? = value?.let { Barcode.fromJSON(JSONObject(it)) }

    @TypeConverter
    fun fromBoardingData(value: BoardingData?): String? = value?.toJSON()?.toString()
    @TypeConverter
    fun toBoardingData(value: String?): BoardingData? = value?.let { BoardingData.fromJSON(JSONObject(it)) }
}