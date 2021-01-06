package com.github.syari.ss.texture.gitclient

import java.io.File

class Texture(val directory: File) {
    companion object {
        private const val PackMcMeta = "pack.mcmeta"

        private val zipsFolder = File("minimum")

        fun getZipOutput(texture: Texture): File {
            if (zipsFolder.exists().not()) {
                zipsFolder.mkdir()
            }
            return File(zipsFolder, texture.directory.name + ".zip")
        }

        fun File.isTexture() = isDirectory && File(this, PackMcMeta).exists()
    }

    fun makeZip() {
        zipFiles(directory, getZipOutput(this))
    }
}