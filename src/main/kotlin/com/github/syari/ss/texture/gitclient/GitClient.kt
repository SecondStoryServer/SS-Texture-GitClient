package com.github.syari.ss.texture.gitclient

import com.github.syari.kgit.KGit
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File

object GitClient {
    val api = KGit.open(File("."))

    fun commit(message: String, authorName: String, authorEmail: String) {
        api.commit {
            this.message = message
            setAll(true)
            setAllowEmpty(false)
            setAuthor(authorName, authorEmail)
            setCommitter(authorName, authorEmail)
            setSign(false)
        }
    }

    fun push(userName: String, password: String) {
        val credentialsProvider = UsernamePasswordCredentialsProvider(userName, password)
        api.push {
            setCredentialsProvider(credentialsProvider)
        }
    }
}
