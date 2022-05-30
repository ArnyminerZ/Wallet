package com.arnyminerz.wallet.pkpass.data.boarding

import com.arnyminerz.wallet.pkpass.data.Field

data class BoardingData(
    val transitType: TransitType,
    val headerFields: List<Field>,
    val primaryFields: List<Field>,
    val secondaryFields: List<Field>,
    val auxiliaryFields: List<Field>,
    val backFields: List<Field>,
)
