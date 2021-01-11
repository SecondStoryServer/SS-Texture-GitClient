package com.github.syari.ss.texture.gitclient

import com.github.syari.kgit.KGit
import org.eclipse.jgit.lib.Constants

sealed class UpdateResult(val message: String) {
    class Success(message: String): UpdateResult(message)
    class NoChange(message: String): UpdateResult(message)
    class Conflict(message: String): UpdateResult(message)
    class FailPull(message: String): UpdateResult(message)
}

fun KGit.update(): UpdateResult {
    val lastHead = repository.resolve(Constants.HEAD)
    val pullResult = pull()
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