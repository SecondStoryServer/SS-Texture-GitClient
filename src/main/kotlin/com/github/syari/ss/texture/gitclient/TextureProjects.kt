package com.github.syari.ss.texture.gitclient

import com.github.syari.ss.texture.gitclient.Texture.Companion.isTexture
import java.io.File
import java.io.FileFilter

object TextureProjects {
    private val directory = File(".")

    val projects = directory.listFiles(FileFilter { it.isTexture() })?.map { Texture(it) }.orEmpty()
}