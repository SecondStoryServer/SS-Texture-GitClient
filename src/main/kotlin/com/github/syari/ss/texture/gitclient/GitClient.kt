package com.github.syari.ss.texture.gitclient

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.UserConfig
import org.eclipse.jgit.transport.URIish
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess

object GitClient {
    val git: Git

    init {
        val cloneUrl = "https://github.com/SecondStoryServer/SS-Texture"
        if (File(TextureProjects.directory, Constants.DOT_GIT).exists().not()) {
            val clone = Git.cloneRepository().setURI(cloneUrl).call()
            val cloneDirectory = clone.repository.directory.parentFile
            cloneDirectory.listFiles()?.forEach {
                val destFile = File(TextureProjects.directory, it.name)
                if (destFile.exists().not()) {
                    it.renameTo(destFile)
                }
            }
            cloneDirectory.deleteRecursively()
        }
        git = Git.open(TextureProjects.directory)
    }

    fun add(file: File) {
        git.add().addFilepattern(file.path.removePrefix("./")).call()
    }

    fun update() = git.update()

    fun clearChangeList() {
        git.reset().call()
    }

    fun clearChangeList(texture: Texture) {
        git.reset().addPath(texture.name).call()
    }

    fun getUserConfig(): UserConfig = git.repository.config.get(UserConfig.KEY)

    fun commit(message: String, authorName: String, authorEmail: String) {
        git.commit().setMessage(message).setAuthor(authorName, authorEmail).setSign(false).call()
    }

    fun push(userName: String, password: String) {
        val credentialsProvider = UsernamePasswordCredentialsProvider(userName, password)
        git.push().setCredentialsProvider(credentialsProvider).call()
    }
}