package com.arnyminerz.wallet.model

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.arnyminerz.wallet.pkpass.Parser
import com.arnyminerz.wallet.pkpass.data.Pass
import com.arnyminerz.wallet.pkpass.data.PassAspect
import com.arnyminerz.wallet.pkpass.data.boarding.BoardingData
import com.arnyminerz.wallet.pkpass.data.boarding.TransitType
import com.arnyminerz.wallet.utils.drop
import com.arnyminerz.wallet.utils.getColor
import com.arnyminerz.wallet.utils.getFields
import timber.log.Timber
import java.io.File
import java.io.FileInputStream

class MainViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Stores the [Parser] instance that gets loaded by [loadPkPass].
     * @author Arnau Mora
     * @since 20220530
     */
    private lateinit var parser: Parser

    var pass by mutableStateOf<Pass?>(null)

    /**
     * Loads a pkpass from the response uri from a file picker.
     * @author Arnau Mora
     * @since 20220530
     * @param uri The uri to load. If null no load will be performed.
     * @throws SecurityException When the hash of a file does not match the manifest.
     */
    @Throws(
        SecurityException::class,
    )
    fun loadPkPass(uri: Uri?) {
        if (uri == null)
            return

        val context = getApplication<Application>()
            .applicationContext
        context
            .contentResolver
            .openFileDescriptor(uri, "r")
            ?.use { descriptor ->
                Timber.i("Loading pkpass from $uri...")
                parser = Parser(context, FileInputStream(descriptor.fileDescriptor))

                @Suppress("ThrowableNotThrown")
                val manifestValid = parser.validManifest
                    .drop(SecurityException("The pkpass file is corrupt: Hashes do not match."))
                Timber.i("Loaded pkpass file. Valid: $manifestValid")

                Timber.i("Loading barcode...")
                val data = parser.readPass()
                val boardingPass = data.getJSONObject("boardingPass")
                pass = Pass(
                    data.getInt("formatVersion"),
                    data.getString("passTypeIdentifier"),
                    data.getString("serialNumber"),
                    data.getString("teamIdentifier"),
                    data.getString("organizationName"),
                    data.getString("description"),
                    PassAspect(
                        data.getColor("labelColor"),
                        data.getColor("foregroundColor"),
                        data.getColor("backgroundColor"),
                    ),
                    parser.getBarcode()
                        .also {
                            Timber.i("Loading bitmap...")
                            it.bitmap = parser.getBitmap(context)
                                .also { bmp ->
                                    File(context.filesDir, "file.png")
                                        .also { if (it.exists()) it.delete() }
                                        .outputStream()
                                        .use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
                                }
                        },
                    BoardingData(
                        TransitType.valueOf(boardingPass.getString("transitType")),
                        boardingPass.getFields("headerFields"),
                        boardingPass.getFields("primaryFields"),
                        boardingPass.getFields("secondaryFields"),
                        boardingPass.getFields("auxiliaryFields"),
                        boardingPass.getFields("backFields"),
                    )
                )
            }
    }
}