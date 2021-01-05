package com.github.syari.ss.texture.gitclient

import org.eclipse.jgit.api.Git
import java.io.File

object GitClient {
    private const val JarFileName = "$ProjectName.jar"
    private const val JarFilePath = "jars/$JarFileName"

    private val git = Git.open(TextureProjects.directory)

    fun update() {
        pull()
        updateSubModule()
        copyJar()
    }

    private fun pull() {
        git.pull().call()
    }

    private fun updateSubModule() {
        git.submoduleInit().call()
        git.submoduleUpdate().setFetch(true).call()
    }

    private fun copyJar() {
        File("$ProjectName/$JarFilePath").copyTo(File(JarFileName), true)
    }
}