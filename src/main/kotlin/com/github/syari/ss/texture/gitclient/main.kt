package com.github.syari.ss.texture.gitclient

import java.util.logging.Logger
import java.util.logging.Logger.getLogger

const val ProjectName = "SS-Texture-GitClient"
const val Version = 12
val Logger: Logger = getLogger(ProjectName)

fun main() {
    Logger.info("Hello!! $ProjectName v$Version")
    updateOrInit()
    GitClient.clearChangeList()
    TextureProjects.projects.forEach { texture ->
        println("""
            
            *** ${texture.name} ***

        """.trimIndent())
        texture.addToGit()
        val changeList = texture.getChangeList()
        println("Single")
        changeList.single.forEach {
            println("- ${it.status} ${it.file}")
        }
        println("Pair")
        changeList.pairModelTexture.forEach {
            println("- ${it.json.status} ${it.json.file} / ${it.png.status} ${it.png.file}")
        }
        GitClient.addDirectory(Texture.zipsFolder)
        print("Commit Message: ")
        val commitMessage = readLine() ?: return Logger.warning("Failure Commit: No Commit Message")
        val user = GitClient.getUserConfig()
        print("Author Name: (${user.authorName})")
        val authorName = readLine()?.ifBlank { null } ?: user.authorName
        print("Author Email: (${user.authorEmail})")
        val authorEmail = readLine()?.ifBlank { null } ?: user.authorEmail
        GitClient.commit("${texture.name}: $commitMessage", authorName, authorEmail)
    }
}

fun updateOrInit() {
    val result = GitClient.update()
    Logger.info(result.message)
}