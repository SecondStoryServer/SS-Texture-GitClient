package com.github.syari.ss.texture.gitclient

import com.github.syari.ss.texture.gitclient.UserSetting.PropertiesValue.Companion.keyValue
import java.io.File
import java.util.Properties
import kotlin.reflect.KProperty

object UserSetting {
    private const val FileName = "ss-texture.properties"
    private val file = File(FileName)
    private lateinit var properties: Properties

    fun load() {
        if (file.exists().not()) {
            file.createNewFile()
        }
        properties = Properties().apply { load(file.inputStream()) }
    }

    var commitAuthorName by keyValue("CommitAuthorName")
    var commitAuthorEmail by keyValue("CommitAuthorEmail")
    var remoteUserName by keyValue("RemoteUserName")
    var remotePassword by keyValue("RemotePassword")

    fun save() {
        if (file.exists().not()) {
            file.createNewFile()
        }
        properties.store(file.outputStream(), "SS-Texture Private File. DO NOT COMMIT THIS FILE.")
    }

    class PropertiesValue(private val key: String) {
        companion object {
            fun keyValue(key: String) = PropertiesValue(key)
        }

        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return properties.getProperty(key).orEmpty()
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            if (value.isEmpty()) {
                properties.remove(key)
            } else {
                properties.setProperty(key, value)
            }
        }
    }
}