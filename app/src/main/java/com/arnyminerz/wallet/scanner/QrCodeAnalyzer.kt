package com.arnyminerz.wallet.scanner

import android.content.Context
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toComposeRect
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import timber.log.Timber

@ExperimentalGetImage
class QrCodeAnalyzer(
    private val previewViewWidth: Float,
    private val previewViewHeight: Float,
    private val listener: BarcodeCallback,
    private val squares: SnapshotStateList<Rect>,
) : ImageAnalysis.Analyzer {
    /**
     * This parameters will handle preview box scaling
     */
    private var scaleX = 1f
    private var scaleY = 1f

    private fun translateX(x: Float) = x * scaleX
    private fun translateY(y: Float) = y * scaleY

    private fun adjustBoundingRect(rect: Rect) = Rect(
        translateX(rect.left),
        translateY(rect.top),
        translateX(rect.right),
        translateY(rect.bottom),
    )

    override fun analyze(image: ImageProxy) {
        image.image?.let { img ->
            // Update scale factors
            scaleX = previewViewWidth / img.height.toFloat()
            scaleY = previewViewHeight / img.width.toFloat()

            val inputImage = InputImage.fromMediaImage(img, image.imageInfo.rotationDegrees)

            // Process image searching for barcodes
            val options = BarcodeScannerOptions.Builder()
                // .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            BarcodeScanning.getClient(options)
                .process(inputImage)
                .addOnSuccessListener { barcodes ->
                    Timber.i("Found ${barcodes.size} barcodes.")
                    squares.clear()
                    barcodes
                        .takeIf { it.isNotEmpty() }
                        ?.forEach { barcode ->
                            Timber.i("Barcode: $barcode")
                            listener.onFound(barcode)

                            barcode.boundingBox?.let {
                                val rect = it.toComposeRect()
                                squares.add(adjustBoundingRect(rect))
                            }
                        }
                }
                .addOnFailureListener { Timber.e(it, "Could not analyze image.") }
                .addOnCompleteListener { image.close() }
        } ?: Timber.w("ImageProxy returned a null image.")
    }
}