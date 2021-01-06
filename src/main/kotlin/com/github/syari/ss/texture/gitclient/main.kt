package com.github.syari.ss.texture.gitclient

import java.util.logging.Logger
import java.util.logging.Logger.getLogger

const val ProjectName = "SS-Texture-GitClient"
const val Version = 4
val Logger: Logger = getLogger(ProjectName)

fun main() {
    Logger.info("Hello!! $ProjectName v$Version")
    updateOrInit()
    TextureProjects.projects.forEach(Texture::makeZip)
    GitClient.printStatus()
}

fun updateOrInit() {
    val result = GitClient.update()
    Logger.info(result.message)
}