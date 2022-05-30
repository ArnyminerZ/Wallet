package com.arnyminerz.wallet.utils.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.DimenRes
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter

object BarcodeEncoder {
    /**
     * Encodes a barcode [Bitmap] from a text to encode.
     * @author Arnau Mora
     * @since 20220530
     * @param context The context that is loading the [Bitmap].
     * @param encode The text to encode.
     * @param format The format of the barcode.
     * @param sizeDp The size of the built barcode. Matches both height and width.
     */
    fun getBitmap(
        context: Context,
        encode: String,
        format: BarcodeFormat,
        @DimenRes sizeDp: Int
    ): Bitmap {
        val hints = hashMapOf<EncodeHintType, Any>()
        hints[EncodeHintType.MARGIN] = 0

        val writer = MultiFormatWriter()

        val barcodeSizePx = context.resources.getDimensionPixelSize(sizeDp)
        val result = writer.encode(encode, format, barcodeSizePx, barcodeSizePx, hints)

        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width)
                pixels[offset + x] = if (result.get(x, y)) Color.BLACK else Color.WHITE
        }

        val barcode = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        barcode.setPixels(pixels, 0, width, 0, 0, width, height)
        return barcode
    }
}
