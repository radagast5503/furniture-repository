package com.muebles.ra.utils

import java.util.*


object Version {
    private val versionProperties = Properties()

    init {
        versionProperties.load(this.javaClass.getResourceAsStream("/version.properties"))
    }

    fun version() : String = versionProperties.getProperty("version") ?: "no version"

    fun environment(): String = System.getenv("ENVIRONMENT") ?: "local"
}