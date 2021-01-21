package com.github.syari.ss.texture.gitclient.setting

object UserSetting : Setting("user.properties") {
    override val comments = "SS-Texture Private File. DO NOT COMMIT THIS FILE."

    var commitAuthorName by keyValue("CommitAuthorName")
    var commitAuthorEmail by keyValue("CommitAuthorEmail")
    var remoteUserName by keyValue("RemoteUserName")
    var remotePassword by keyValue("RemotePassword")
}
