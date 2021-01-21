package com.github.syari.ss.texture.gitclient.setting

import java.util.Properties
import kotlin.reflect.KProperty

class PropertiesValue(private val properties: Properties, private val key: String) {
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
