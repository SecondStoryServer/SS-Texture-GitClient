package com.github.syari.ss.texture.gitclient

import org.eclipse.jgit.api.Git
import java.io.File

object GitClient {
    private val git = Git.open(TextureProjects.directory)

    fun addDirectory(directory: File) {
        git.add().addFilepattern(directory.name).call()
    }

    fun printStatus() {
        val status = git.status().call()

        status.added.forEach {
            println("追加: $it")
        }
        status.changed.forEach {
            println("変更: $it")
        }
        status.removed.forEach {
            println("削除: $it")
        }
    }

    fun update() = git.update()
}