package com.arnyminerz.wallet.pkpass.data

import com.arnyminerz.wallet.pkpass.data.boarding.BoardingData

data class Pass(
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
