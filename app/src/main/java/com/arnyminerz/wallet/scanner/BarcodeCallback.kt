package com.arnyminerz.wallet.scanner

import com.google.mlkit.vision.barcode.common.Barcode

interface BarcodeCallback {
    fun onFound(barcode: Barcode)
}