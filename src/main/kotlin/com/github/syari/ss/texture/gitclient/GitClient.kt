package com.github.syari.ss.texture.gitclient

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants

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

        status.conflicting.forEach {
            println("Conflicting: $it")
        }
        status.added.forEach {
            println("Added: $it")
        }
        status.changed.forEach {
            println("Change: $it")
        }
        status.missing.forEach {
            println("Missing: $it")
        }
        status.modified.forEach {
            println("Modification: $it")
        }
        status.removed.forEach {
            println("Removed: $it")
        }
        status.uncommittedChanges.forEach {
            println("Uncommitted: $it")
        }
        status.untracked.forEach {
            println("Untracked: $it")
        }
        status.untrackedFolders.forEach {
            println("Untracked Folder: $it")
        }
        status.conflictingStageState.entries.forEach {
            println("ConflictingState: $it")
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