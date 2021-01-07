package com.github.syari.ss.texture.gitclient

import java.util.logging.Logger
import java.util.logging.Logger.getLogger

const val ProjectName = "SS-Texture-GitClient"
const val Version = 18
const val RemoteURL = "https://github.com/SecondStoryServer/SS-Texture"
val Logger: Logger = getLogger(ProjectName)

fun main() {
    Logger.info("Hello!! $ProjectName v$Version")
    val result = GitClient.update()
    Logger.info(result.message)
    GitClient.clearChangeList()
    var commitCount = 0
    TextureProjects.projects.forEach { texture ->
        texture.addToGit()
        val changeList = texture.getChangeList()
        if (changeList.isEmpty()) return@forEach
        println("""
            
            *** ${texture.name} ***

        """.trimIndent())
        println("Single")
        changeList.single.forEach {
            println("- ${it.status} ${it.file}")
        }
        println("Pair")
        changeList.pairModelTexture.forEach {
            println("- ${it.json.status} ${it.json.file} / ${it.png.status} ${it.png.file}")
        }
        GitClient.add(texture.makeZip())
        print("Commit Message: ")
        val commitMessage = readLine()
        if (commitMessage.isNullOrEmpty()) {
            Logger.info("Ignore Project")
            GitClient.clearChangeList(texture)
            return@forEach
        }
        val user = GitClient.getUserConfig()
        print("Author Name: (${user.authorName})")
        val authorName = readLine()?.ifBlank { null } ?: user.authorName
        print("Author Email: (${user.authorEmail})")
        val authorEmail = readLine()?.ifBlank { null } ?: user.authorEmail
        GitClient.commit("${texture.name}: $commitMessage", authorName, authorEmail)
        commitCount ++
    }
    Logger.info("Committed $commitCount")
    if (commitCount != 0) {
        print("GitHub UserName: ")
        val authorName = readLine() ?: return Logger.warning("Push Failure")
        print("GitHub Password: (Hidden)")
        val authorEmail = System.console().readPassword("")?.joinToString("") ?: return Logger.warning("Push Failure")
        GitClient.push(authorName, authorEmail)
    }
}