package com.arnyminerz.wallet.utils.fs

import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * Saves a stream to a target file.
 * @author shmoula, Arnau Mora
 * @since 20220530
 * @param output The target file to write to.
 * @throws IOException When there's an error while writing the file.
 */
@Throws(
    IOException::class
)
fun InputStream.copyToFile(output: File) {
    val buffer = ByteArray(1024)
    val fos = output.outputStream()
    val outputStream = fos.buffered()

    try {
        var bytesRead = read(buffer)
        while (bytesRead != -1) {
            outputStream.write(buffer, 0, bytesRead)
            bytesRead = read(buffer)
        }
    } finally {
        fos.fd.sync()
        outputStream.close()
    }
}
