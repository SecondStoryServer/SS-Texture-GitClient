package com.github.syari.ss.texture.gitclient

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun zipFiles(directory: File, output: File) {
    ZipOutputStream(FileOutputStream(output)).use {
        it.addFiles(directory.listFiles()!!)
    }
}

private fun ZipOutputStream.addFiles(files: Array<File>, parent: String = "") {
    files.forEach { file ->
        val path = parent + file.name
        if (file.isFile) {
            FileInputStream(file).use { inputStream ->
                putNextEntry(ZipEntry(path))
                if (file.extension.equals("json", true)) {
                    inputStream.bufferedReader().forEachLine {
                        val line = it.replace("\\s+".toRegex(), "").replace("\\n|\\r", "")
                        val byteArray = line.toByteArray()
                        write(byteArray, 0, byteArray.size)
                    }
                } else {
                    val buffer = ByteArray(1024)
                    var len: Int
                    while (inputStream.read(buffer).also { len = it } > 0) {
                        write(buffer, 0, len)
                    }
                }
            }
        } else {
            file.listFiles()?.let {
                addFiles(it, "$path/")
            }
        }
    }
}