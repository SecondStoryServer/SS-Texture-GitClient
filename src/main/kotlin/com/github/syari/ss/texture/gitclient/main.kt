package com.github.syari.ss.texture.gitclient

import java.io.File

fun main() {
    updateOrInit()
}

fun updateOrInit() {
    GitClient.update()
}