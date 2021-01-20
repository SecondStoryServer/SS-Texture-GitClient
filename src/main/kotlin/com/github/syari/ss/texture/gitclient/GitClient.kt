package com.github.syari.ss.texture.gitclient

import com.github.syari.kgit.KGit
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import java.io.File

object GitClient {
    val git = KGit.open(TextureProjects.directory)

    fun add(file: File) {
        git.add {
            addFilepattern(file.toRelativeString(TextureProjects.directory))
        }
    }

    fun clearChangeList() {
        git.reset()
    }

    fun clearChangeList(texture: Texture) {
        git.reset {
            addPath(texture.name)
        }
    }

    fun commit(message: String, authorName: String, authorEmail: String) {
        git.commit {
            this.message = message
            setAuthor(authorName, authorEmail)
            setCommitter(authorName, authorEmail)
            setSign(false)
        }
    }

    fun push(userName: String, password: String) {
        val credentialsProvider = UsernamePasswordCredentialsProvider(userName, password)
        git.push {
            setCredentialsProvider(credentialsProvider)
        }
    }
}
