package com.github.syari.ss.texture.gitclient

import org.apache.log4j.Logger

const val ProjectName = "SS-Texture-GitClient"
const val Version = 20
const val RemoteURL = "https://github.com/SecondStoryServer/SS-Texture"
val logger: Logger = Logger.getLogger(ProjectName)

fun main() {
    logger.info("Hello!! $ProjectName v$Version")
    val result = GitClient.update()
    logger.info(result.message)
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
            logger.info("Ignore Project")
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
    logger.info("Committed $commitCount")
    if (commitCount != 0) {
        print("GitHub UserName: ")
        val authorName = readLine() ?: return logger.warn("Push Failure")
        print("GitHub Password: (Hidden)")
        val authorEmail = System.console().readPassword("")?.joinToString("") ?: return logger.warn("Push Failure")
        GitClient.push(authorName, authorEmail)
    }
}