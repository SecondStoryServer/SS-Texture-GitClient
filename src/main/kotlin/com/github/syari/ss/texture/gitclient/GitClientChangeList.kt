package com.github.syari.ss.texture.gitclient

sealed class ChangeFile {
    class SingleFile(val status: Status, val file: String): ChangeFile()
    class PairModelTexture(val json: SingleFile, val png: SingleFile): ChangeFile()
}

enum class Status {
    Add,
    Change,
    Remove
}

class ChangeList(val single: List<ChangeFile.SingleFile>, val pairModelTexture: List<ChangeFile.PairModelTexture>) {
    companion object {
        fun build(action: Builder.() -> Unit) = Builder().apply(action).build()
    }

    fun isEmpty() = single.isEmpty() && pairModelTexture.isEmpty()

    class Builder {
        private val singleFiles = mutableMapOf<String, ChangeFile.SingleFile>()
        private val pairModelTextures = mutableListOf<ChangeFile.PairModelTexture>()

        fun add(status: Status, files: Set<String>) {
            singleFiles.putAll(files.associateWith { ChangeFile.SingleFile(status, it) })
        }

        fun sort() {
            val jsons = singleFiles.filter { it.key.endsWith(".json") }
            jsons.forEach { (jsonFileName, jsonFile) ->
                val pngFileName = jsonFileName.replace("models", "textures").replace(".json", ".png")
                val pngFile = singleFiles[pngFileName] ?: return@forEach
                pairModelTextures.add(ChangeFile.PairModelTexture(jsonFile, pngFile))
                singleFiles.remove(jsonFileName)
                singleFiles.remove(pngFileName)
            }
            pairModelTextures.sortBy { it.json.file.substringAfterLast('/').substringBefore(".json").toIntOrNull() ?: 0 }
        }

        fun build() = ChangeList(singleFiles.values.toList(), pairModelTextures)
    }
}