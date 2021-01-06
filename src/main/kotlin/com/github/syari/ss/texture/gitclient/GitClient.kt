package com.github.syari.ss.texture.gitclient

import org.eclipse.jgit.api.Git
import java.io.File

object GitClient {
    private val git = Git.open(TextureProjects.directory)

    fun addDirectory(directory: File) {
        git.add().addFilepattern(directory.name).call()
    }

    fun getChangeLists(): Map<String, ChangeLists> = git.getChangeLists()

    fun update() = git.update()
}