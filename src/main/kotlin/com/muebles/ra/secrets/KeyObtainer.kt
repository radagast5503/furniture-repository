package com.muebles.ra.secrets

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient
import com.google.cloud.secretmanager.v1.SecretVersionName
import com.muebles.ra.utils.Config
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.inject.Inject

const val LATEST_VERSION = "latest"

@Component
class KeyObtainer @Inject constructor(private val cfg: Config) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val client: SecretManagerServiceClient = SecretManagerServiceClient.create()

    fun getKey(): ByteArray? {
        val name = cfg.projectName()
        val secretName = cfg.privateKeyName()
        checkNotNull(name) { "could not obtain project name" }
        checkNotNull(secretName) { "could not obtain private key name" }
        return client.accessSecretVersion(SecretVersionName.of(name, secretName, LATEST_VERSION)).payload.toByteArray()
    }
}