package com.github.syari.ss.texture.gitclient

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

private val zipOutput: File
    get() {
        if (TextureSetting.zipDirectory.exists().not()) {
            TextureSetting.zipDirectory.mkdir()
        }
        return File(TextureSetting.zipDirectory, TextureSetting.name + ".zip")
    }

fun makeZip() = zipOutput.apply {
    zipFiles(TextureSetting.textureDirectory, this)
}

private fun zipFiles(directory: File, output: File) {
    ZipOutputStream(FileOutputStream(output)).use {
        it.addFiles(directory.listFiles()!!)
    }
}

private fun ZipOutputStream.addFiles(files: Array<File>, parent: String = "") {
    files.forEach { file ->
        val path = parent + file.name
        if (file.isFile) {
            if (file.name == ".DS_Store") return@forEach
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
