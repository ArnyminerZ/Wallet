package com.arnyminerz.wallet.utils

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import kotlin.reflect.KClass

/**
 * Uses the correct method for the current SDK level to get a parcelable from a [Bundle].
 * @author Arnau Mora
 * @since 20221114
 * @param key The key of the parcelable to obtain.
 * @param kClass The class type of the parcelable.
 */
@Suppress("DEPRECATION")
fun <T: Parcelable> Bundle.getParcelableCompat(key: String, kClass: KClass<T>): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        getParcelable(key, kClass.java)
    else
        getParcelable(key)
