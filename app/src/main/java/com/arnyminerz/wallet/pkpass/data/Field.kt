package com.arnyminerz.wallet.pkpass.data

data class Field(
    val key: String,
    val label: String,
    val value: String,
    val changeValue: String? = null,
)

fun List<Field>.getField(key: String, ignoreCase: Boolean = false): Field? {
    for (field in this)
        if (field.key.equals(key, ignoreCase))
            return field
    return null
}
