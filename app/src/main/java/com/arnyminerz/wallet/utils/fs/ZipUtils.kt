package com.arnyminerz.wallet.utils.fs

import org.apache.commons.compress.archivers.ArchiveException
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.UUID

/**
 * Extracts zip from inputStream to temporary directory and returns its path.
 * @author shmoula, Arnau Mora
 * @since 20220530
 * @param tempDirRoot The source dir to extract the contents of the zip file to.
 * @return The path of the extracted directory.
 */
@Throws(
    IOException::class,
    ArchiveException::class,
)
fun InputStream.unzip(tempDirRoot: File): String {
    Timber.d("Decompressing file into $tempDirRoot...")
    val tempDirName = UUID.randomUUID().toString()
    Timber.d("Root dir name: $tempDirName")
    val tempDir = File(tempDirRoot, tempDirName)
    tempDir.mkdirs()

    Timber.d("Decompressing...")
    val ais = ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, this)
    var zae: ZipArchiveEntry? = ais.nextEntry as ZipArchiveEntry?
    while (zae != null) {
        var fileName = zae.name
        var tempSubdir: File? = null

        val separatorIndex = fileName.indexOf(File.separator)
        if (separatorIndex > 0) {
            val path = fileName.substring(0, separatorIndex)

            tempSubdir = File(tempDir, path)
            tempSubdir.mkdirs()

            fileName = fileName.substring(separatorIndex + 1)
        }
        ais.copyToFile(File(tempSubdir ?: tempDir, fileName))

        zae = ais.nextEntry as ZipArchiveEntry?
    }

    return tempDir.path
}
