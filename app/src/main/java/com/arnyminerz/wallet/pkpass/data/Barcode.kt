package com.arnyminerz.wallet.pkpass.data

import com.google.zxing.BarcodeFormat

data class Barcode(
    val message: String,
    val format: String,
    val messageEncoding: String,
    val altText: String,
) {
    val zxingFormat = when (format) {
        "PKBarcodeFormatPDF417" -> BarcodeFormat.PDF_417
        "PKBarcodeFormatQR" -> BarcodeFormat.QR_CODE
        "PKBarcodeFormatAztec" -> BarcodeFormat.AZTEC
        else -> null
    }
}
