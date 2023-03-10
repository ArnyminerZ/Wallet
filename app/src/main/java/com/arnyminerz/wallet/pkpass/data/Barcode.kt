package com.arnyminerz.wallet.pkpass.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.core.graphics.BitmapCompat
import androidx.room.Ignore
import com.arnyminerz.wallet.utils.BITMAP_WEBP
import com.arnyminerz.wallet.utils.JsonSerializable
import com.arnyminerz.wallet.utils.JsonSerializer
import com.arnyminerz.wallet.utils.getStringOrNull
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
            val encoded = json.getStringOrNull("bitmap")
            val bytes = Base64.decode(encoded, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
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
            val outputStream = ByteArrayOutputStream()
            image.compress(BITMAP_WEBP, 100, outputStream)
            val bytes = outputStream.toByteArray()
            val encoded = Base64.encodeToString(bytes, Base64.DEFAULT)
            put("bitmap", encoded)
        }
    }
}
