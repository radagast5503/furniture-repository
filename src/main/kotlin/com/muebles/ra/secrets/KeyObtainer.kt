package com.muebles.ra.secrets

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import com.muebles.ra.utils.Config
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.inject.Inject
import java.util.Base64

const val LATEST_VERSION = "latest"

@Component
class KeyObtainer @Inject constructor(private val cfg: Config) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val client: SecretManagerServiceClient = SecretManagerServiceClient.create()

    private val cachedItems by lazy {
        val name = cfg.projectName()
        val secretName = cfg.privateKeyName()
        checkNotNull(name) { "could not obtain project name" }
        checkNotNull(secretName) { "could not obtain private key name" }
        Base64.getDecoder().decode(
            client.accessSecretVersion(SecretVersionName.of(name, secretName, LATEST_VERSION)).payload.toByteArray()
        )
    }

    fun getKey(): ByteArray? = cachedItems
}