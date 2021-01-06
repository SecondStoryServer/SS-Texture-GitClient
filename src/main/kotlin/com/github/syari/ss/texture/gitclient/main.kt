package com.github.syari.ss.texture.gitclient

import java.util.logging.Logger
import java.util.logging.Logger.getLogger

const val ProjectName = "SS-Texture-GitClient"
const val Version = 10
val Logger: Logger = getLogger(ProjectName)

fun main() {
    Logger.info("Hello!! $ProjectName v$Version")
    updateOrInit()
    TextureProjects.projects.forEach(Texture::addToGit)
    val changeLists = GitClient.getChangeLists().filterKeys { it != Texture.zipsFolder.name }
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
    if (changeLists.isNotEmpty()) {
        TextureProjects.projects.filter { changeLists.keys.contains(it.directory.name) }.forEach(Texture::makeZip)
        GitClient.addDirectory(Texture.zipsFolder)
        print("Commit Message: ")
        val commitMessage = readLine() ?: return Logger.warning("Failure Commit: No Commit Message")
        val user = GitClient.getUserConfig()
        print("Author Name: (${user.authorName})")
        val authorName = readLine() ?: user.authorName
        print("Author Email: (${user.authorEmail})")
        val authorEmail = readLine() ?: user.authorEmail
        GitClient.commit(commitMessage, authorName, authorEmail)
    } else {
        Logger.info("ChangeList is Empty")
    }
}

fun updateOrInit() {
    val result = GitClient.update()
    Logger.info(result.message)
}