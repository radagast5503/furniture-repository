package com.muebles.ra.utils

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

public fun Config.tryGetLong(path: String): Long? = if (hasPath(path)) getLong(path) else null


@Component
class Config {
    val config: Config = ConfigFactory.load()

    fun projectName(): String? = config.tryGetString("gcp.project-name")

    fun privateKeyName(): String? = config.tryGetString("gcp.signing.secret-name")

    fun bucketName(): String? = config.tryGetString("gcp.storage.bucket.name")

    fun urlDuration(): Long? = config.tryGetLong("gcp.signing.url-duration")
}