package com.github.syari.ss.texture.gitclient

fun main() {
    println(TextureProjects.projects.joinToString("\n") { it.directory.absolutePath })
}