package com.github.syari.ss.texture.gitclient

import com.github.syari.kgit.KGit
import org.eclipse.jgit.lib.Constants
import java.io.File
import kotlin.system.exitProcess

fun checkUpdate() {
    if (File(Constants.DOT_GIT).exists().not()) {
        cloneRepository()
    } else {
        val fetchResult = GitClient.api.fetch()
        if (fetchResult.trackingRefUpdates.isNotEmpty()) {
            cloneRepository()
        }
    }
}

private fun cloneRepository() {
    KGit.cloneRepository {
        setURI(TextureSetting.remoteUrl)
        setDirectory(File(".clone_tmp"))
    }
    logger.info("Download update(s). Please re-execute.")
    exitProcess(0)
}
