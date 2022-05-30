package com.arnyminerz.wallet.pkpass

import android.content.Context
import android.graphics.Bitmap
import com.arnyminerz.wallet.R
import com.arnyminerz.wallet.pkpass.data.Barcode
import com.arnyminerz.wallet.utils.fs.HashUtils
import com.arnyminerz.wallet.utils.fs.MessageDigestAlgorithm
import com.arnyminerz.wallet.utils.fs.unzip
import com.arnyminerz.wallet.utils.image.BarcodeEncoder
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest

class Parser(context: Context, pkpass: InputStream) {
    companion object {
        const val FILENAME_MANIFEST = "manifest.json"
    }

    private val pkPassDirectory = pkpass.unzip(context.cacheDir)

    private lateinit var barcode: Barcode

    val validManifest: Boolean
        get() {
            Timber.d("Checking if manifest is valid...")
            val manifestFile = getFile(FILENAME_MANIFEST)
            val reader = manifestFile.inputStream().reader()
            val element = JSONObject(reader.readText())
            Timber.d("Manifest contents: ${element.toString(2)}")
            for (key in element.keys()) {
                val file = File(pkPassDirectory, key)
                val hashFromFile = HashUtils.getCheckSumFromFile(
                    MessageDigest.getInstance(MessageDigestAlgorithm.SHA_1),
                    file
                )
                Timber.d("Hash for $key: $hashFromFile")
                val hashFromManifest = element.getString(key)
                if (hashFromFile != hashFromManifest)
                    return false
            }
            return true
        }

    private fun getFile(filename: String) = File(pkPassDirectory, filename)

    /**
     * Reads a pass.json data file and parses it into a [JSONObject].
     * @author Arnau Mora
     * @since 20220530
     * @return The contents of the pass file parsed.
     * @throws IOException When there has been an error while reading the file.
     * @throws JSONException When the file could not be parsed from JSON.
     */
    @Throws(
        IOException::class,
        JSONException::class,
    )
    fun readPass(): JSONObject {
        val passFile = getFile("pass.json")
        return JSONObject(passFile.readText())
    }

    fun getBarcode(): Barcode =
        if (this::barcode.isInitialized)
            barcode
        else
            readPass()
                .getJSONObject("barcode")
                .let { barcode ->
                    Barcode(
                        barcode.getString("message"),
                        barcode.getString("format"),
                        barcode.getString("messageEncoding"),
                        barcode.getString("altText"),
                    )
                }
                .also { barcode = it }

    fun getBitmap(context: Context): Bitmap {
        Timber.d("Getting barcode data...")
        val barcode = getBarcode()
        Timber.d("Encoding bitmap from message: ${barcode.message}")
        return BarcodeEncoder.getBitmap(
            context,
            barcode.message,
            barcode.zxingFormat
                ?: throw IllegalStateException("The barcode does not have a valid format: ${barcode.format}"),
            R.dimen.barcode_size,
        )
    }
}