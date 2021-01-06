package com.github.syari.ss.texture.gitclient

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.UserConfig
import java.io.File

object GitClient {
    val git: Git = Git.open(TextureProjects.directory)

    fun addDirectory(directory: File) {
        git.add().addFilepattern(directory.name).call()
    }

    fun update() = git.update()

    fun clearChangeList() {
        git.reset().call()
    }

    fun getUserConfig(): UserConfig = git.repository.config.get(UserConfig.KEY)

    fun commit(message: String, authorName: String, authorEmail: String) {
        git.commit().setMessage(message).setAuthor(authorName, authorEmail).setSign(false).call()
    }
}