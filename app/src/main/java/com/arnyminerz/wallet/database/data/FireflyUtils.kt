package com.arnyminerz.wallet.database.data

import java.text.SimpleDateFormat
import java.util.*

val FIREFLY_DATE_FORMATTER: SimpleDateFormat
    get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault())

val FIREFLY_SHORT_DATE: SimpleDateFormat
    get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
