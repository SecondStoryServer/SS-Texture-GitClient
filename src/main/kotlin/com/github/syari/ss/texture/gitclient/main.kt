package com.github.syari.ss.texture.gitclient

import org.apache.log4j.Logger

const val ProjectName = "SS-Texture-GitClient"
const val Version = 23
const val RemoteURL = "https://github.com/SecondStoryServer/SS-Texture"
val logger: Logger = Logger.getLogger(ProjectName)

fun main() {
    logger.info("Hello!! $ProjectName v$Version")
    checkUpdate()
    UserSetting.run {
        load()
        commitAuthorName.ifEmpty {
            print("commitAuthorName: ")
            commitAuthorName = readLine().orEmpty()
        }
        commitAuthorEmail.ifEmpty {
            print("commitAuthorEmail: ")
            commitAuthorEmail = readLine().orEmpty()
        }
        remoteUserName.ifEmpty {
            print("remoteUserName: ")
            remoteUserName = readLine().orEmpty()
        }
        remotePassword.ifEmpty {
            print("remotePassword: ")
            remotePassword = readLine().orEmpty()
        }
        save()
    }
    GitClient.clearChangeList()
    var commitCount = 0
    TextureProjects.projects.forEach { texture ->
        texture.addToGit()
        val changeList = texture.getChangeList()
        if (changeList.isEmpty()) return@forEach
        println(
            """
            
            *** ${texture.name} ***

            """.trimIndent()
        )
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
        GitClient.commit("${texture.name}: $commitMessage", UserSetting.commitAuthorName, UserSetting.commitAuthorEmail)
        commitCount ++
    }
    logger.info("Committed $commitCount")
    if (commitCount != 0) {
        GitClient.push(UserSetting.remoteUserName, UserSetting.remotePassword)
    }
}
