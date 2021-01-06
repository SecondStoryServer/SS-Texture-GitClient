package com.github.syari.ss.texture.gitclient

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants
import java.io.File

object GitClient {
    private val git = Git.open(TextureProjects.directory)

    fun addDirectory(directory: File) {
        git.add().addFilepattern(directory.name).call()
    }

    sealed class UpdateResult(val message: String) {
        class Success(message: String): UpdateResult(message)
        class NoChange(message: String): UpdateResult(message)
        class Conflict(message: String): UpdateResult(message)
        class FailPull(message: String): UpdateResult(message)
    }

    fun printStatus() {
        val status = git.status().call()

        status.added.forEach {
            println("追加: $it")
        }
        status.changed.forEach {
            println("変更: $it")
        }
        status.removed.forEach {
            println("削除: $it")
        }
    }

    fun update(): UpdateResult {
        val lastHead = git.repository.resolve(Constants.HEAD)
        val pullResult = git.pull().call()
        return if (pullResult.isSuccessful) {
            val mergeResult = pullResult.mergeResult
            val conflicts = mergeResult.conflicts
            if (conflicts != null) {
                val mergedCommits = mergeResult.mergedCommits
                UpdateResult.Conflict(
                    buildString {
                        conflicts.forEach { (path, conflict) ->
                            appendLine("Conflicts in file $path")
                            for (i in conflict.indices) {
                                appendLine("  Conflict #$i")
                                for (j in conflict[i].indices) {
                                    if (0 <= conflict[i][j]) {
                                        appendLine("    Chunk for ${mergedCommits[j]} starts on line #${conflict[i][j]}")
                                    }
                                }
                            }
                        }
                    }
                )
            } else {
                val newHead = mergeResult.newHead
                if (lastHead != newHead) {
                    UpdateResult.Success("Successful. ${lastHead.name} -> ${newHead.name}")
                } else {
                    UpdateResult.NoChange("Successful. No Changes.")
                }
            }
        } else {
            UpdateResult.FailPull("Failure Pull")
        }
    }
}