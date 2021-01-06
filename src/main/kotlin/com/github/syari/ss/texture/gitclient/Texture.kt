package com.github.syari.ss.texture.gitclient

import java.io.File

class Texture(val directory: File) {
    companion object {
        private const val PackMcMeta = "pack.mcmeta"

        val zipsFolder = File("minimum")

        fun getZipOutput(texture: Texture): File {
            if (zipsFolder.exists().not()) {
                zipsFolder.mkdir()
            }
            return File(zipsFolder, texture.directory.name + ".zip")
        }

        fun File.isTexture() = isDirectory && File(this, PackMcMeta).exists()
    }

    val name: String = directory.name

    fun addToGit() {
        GitClient.add(directory)
    }

    fun getChangeList(): ChangeList {
        val status = GitClient.git.status().call()
        return ChangeList.build {
            add(Status.Add, status.added)
            add(Status.Change, status.changed)
            add(Status.Remove, status.removed)
            sort()
        }
    }

    fun makeZip() = getZipOutput(this).apply {
        zipFiles(directory, this)
    }
}