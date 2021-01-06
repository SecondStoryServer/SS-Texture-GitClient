package com.github.syari.ss.texture.gitclient

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.UserConfig
import java.io.File

object GitClient {
    private val git = Git.open(TextureProjects.directory)

    fun addDirectory(directory: File) {
        git.add().addFilepattern(directory.name).call()
    }

    fun getChangeLists(): Map<String, ChangeLists> = git.getChangeLists()

    fun update() = git.update()

    fun getUserConfig(): UserConfig = git.repository.config.get(UserConfig.KEY)

    fun commit(message: String, authorName: String, authorEmail: String) {
        git.commit().setMessage(message).setAuthor(authorName, authorEmail).setSign(false).call()
    }
}