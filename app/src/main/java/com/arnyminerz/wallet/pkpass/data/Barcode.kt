package com.arnyminerz.wallet.pkpass.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.core.graphics.BitmapCompat
import androidx.room.Ignore
import com.arnyminerz.wallet.utils.BITMAP_WEBP
import com.arnyminerz.wallet.utils.JsonSerializable
import com.arnyminerz.wallet.utils.JsonSerializer
import com.arnyminerz.wallet.utils.getBitmap
import com.arnyminerz.wallet.utils.getStringOrNull
import com.arnyminerz.wallet.utils.putBitmap
import com.google.zxing.BarcodeFormat
import java.io.ByteArrayOutputStream
import org.json.JSONObject

data class Barcode(
    val message: String,
    val format: String,
    val messageEncoding: String,
    val altText: String,
): JsonSerializable {
    companion object: JsonSerializer<Barcode> {
        override fun fromJSON(json: JSONObject): Barcode = Barcode(
            json.getString("message"),
            json.getString("format"),
            json.getString("messageEncoding"),
            json.getString("altText"),
        ).apply {
            bitmap = json.getBitmap("bitmap")
        }
    }

    val zxingFormat = when (format) {
        "PKBarcodeFormatPDF417" -> BarcodeFormat.PDF_417
        "PKBarcodeFormatQR" -> BarcodeFormat.QR_CODE
        "PKBarcodeFormatAztec" -> BarcodeFormat.AZTEC
        else -> null
    }

    var bitmap: Bitmap? = null

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("message", message)
        put("format", format)
        put("messageEncoding", messageEncoding)
        put("altText", altText)
        bitmap?.let { image ->
            putBitmap("bitmap", image)
        }
    }
}
