package com.github.syari.ss.texture.gitclient

import com.github.syari.ss.texture.gitclient.setting.Setting
import java.io.File

object TextureSetting : Setting("project.properties") {
    val name by keyValue("Name")
    val textureDirectory = File(name)
    val remoteUrl by keyValue("RemoteUrl")
    val zipDirectoryPath by keyValue("ZipDirectory")
    val zipDirectory = File(zipDirectoryPath)
}
