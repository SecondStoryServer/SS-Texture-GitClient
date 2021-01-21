package com.github.syari.ss.texture.gitclient

import com.github.syari.ss.texture.gitclient.setting.UserSetting
import org.apache.log4j.Logger

const val ProjectName = "SS-Texture-GitClient"
const val Version = 26
val logger: Logger = Logger.getLogger(ProjectName)

fun main() {
    logger.info("Hello!! $ProjectName v$Version")
    checkUpdate()
    UserSetting.run {
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
    print("Commit Message: ")
    val commitMessage = readLine()
    if (commitMessage.isNullOrEmpty()) {
        return
    }
    makeZip()
    GitClient.commit("${TextureSetting.name}: $commitMessage", UserSetting.commitAuthorName, UserSetting.commitAuthorEmail)
    GitClient.push(UserSetting.remoteUserName, UserSetting.remotePassword)
}
