package com.github.syari.ss.texture.gitclient

import java.util.logging.Logger
import java.util.logging.Logger.getLogger

const val ProjectName = "SS-Texture-GitClient"
const val Version = 6
val Logger: Logger = getLogger(ProjectName)

fun main() {
    Logger.info("Hello!! $ProjectName v$Version")
    updateOrInit()
    TextureProjects.projects.forEach(Texture::addToGit)
    val changeLists = GitClient.getChangeLists()
    changeLists.forEach { (project, list) ->
        println(project)
        println("  Single")
        list.single.forEach {
            println("  - ${it.status} ${it.file}")
        }
        println("  Pair")
        list.pairModelTexture.forEach {
            println("  - ${it.json.status} ${it.json.file} / ${it.png.status} ${it.png.file}")
        }
    }
    TextureProjects.projects.filter { changeLists.keys.contains(it.directory.name) }.forEach(Texture::makeZip)
}

fun updateOrInit() {
    val result = GitClient.update()
    Logger.info(result.message)
}