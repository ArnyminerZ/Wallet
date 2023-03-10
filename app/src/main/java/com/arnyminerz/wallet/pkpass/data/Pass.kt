package com.arnyminerz.wallet.pkpass.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arnyminerz.wallet.pkpass.data.boarding.BoardingData

@Entity(tableName = "passes")
data class Pass(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val archived: Boolean = false,
    val formatVersion: Int,
    val passTypeIdentifier: String,
    val serialNumber: String,
    val teamIdentifier: String,
    val organizationName: String,
    val description: String,
    val aspect: PassAspect?,
    val barcode: Barcode?,
    val boardingData: BoardingData?,
)
