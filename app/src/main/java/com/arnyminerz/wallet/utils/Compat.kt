package com.arnyminerz.wallet.utils

import android.graphics.Bitmap
import android.os.Build

@Suppress("DEPRECATION")
val BITMAP_WEBP: Bitmap.CompressFormat
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        Bitmap.CompressFormat.WEBP_LOSSLESS
    else
        Bitmap.CompressFormat.WEBP
