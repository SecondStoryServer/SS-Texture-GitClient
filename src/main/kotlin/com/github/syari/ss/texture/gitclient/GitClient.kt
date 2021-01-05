package com.github.syari.ss.texture.gitclient

import org.eclipse.jgit.api.Git
import java.io.File

object GitClient {
    private val git = Git.open(TextureProjects.directory)

    fun update() {
        git.pull().call()
    }
}