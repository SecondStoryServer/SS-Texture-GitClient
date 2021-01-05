package com.github.syari.ss.texture.gitclient

import java.io.File

class Texture(val directory: File) {
    companion object {
        private const val PackMcMeta = "pack.mcmeta"

        fun File.isTexture() = isDirectory && File(this, PackMcMeta).exists()
    }
}