package com.muebles.ra.secrets

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.storage.*
import com.muebles.ra.api.Furniture
import com.muebles.ra.utils.Config
import com.muebles.ra.utils.Version
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream
import java.net.URL
import java.util.concurrent.TimeUnit

interface URLObtainer {
    fun uploadUrl(objectName: Furniture): Result<URL?>
    fun downloadUrl(objectName: Furniture): Result<URL?>
}

@Configuration
open class SpringConfig {
    @Bean
    open fun urlObtainer(cfg: Config, keyObtainer: KeyObtainer): URLObtainer {
        val env = Version.environment()
        if (!env.equals("local")) {
            return com.muebles.ra.secrets.SignedUrlGenerator(cfg, keyObtainer)
        }
        return FakeUrlObtainer()
    }
}

class FakeUrlObtainer : URLObtainer {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun uploadUrl(objectName: Furniture): Result<URL?> {
        return Result.success(URL("https://fake.com/upload/${objectName.name}?saraza=pepe&lalala=saraza"))
    }

    override fun downloadUrl(objectName: Furniture): Result<URL?> {
        return Result.success(URL(("https://fake.com/download/${objectName.name}?saraza=pepe&lalala=saraza")))
    }

}

class SignedUrlGenerator constructor(val cfg: Config, keyObtainer: KeyObtainer) : URLObtainer {

    private val credentials = ServiceAccountCredentials.fromStream(ByteArrayInputStream(keyObtainer.getKey()))

    private val storage =
        StorageOptions.newBuilder().setCredentials(credentials).setProjectId(cfg.projectName()).build().service

    private fun signURL(objectName: String?, method: HttpMethod, vararg options: Storage.SignUrlOption): URL? {
        val blobInfo = BlobInfo.newBuilder(BlobId.of(cfg.bucketName(), objectName)).build()
        val urlDuration = cfg.urlDuration()
        checkNotNull(urlDuration) { "could not read duration from config" }
        return storage.signUrl(
            blobInfo, urlDuration, TimeUnit.MINUTES,
            Storage.SignUrlOption.httpMethod(method),
            Storage.SignUrlOption.withV4Signature(),
            *options
        )
    }

    override fun uploadUrl(furniture: Furniture): Result<URL?> = runCatching {
        signURL(
            "${furniture.deviceId}/${furniture.name}",
            HttpMethod.POST,
            Storage.SignUrlOption.withExtHeaders(mapOf("x-goog-resumable" to "start")),
            Storage.SignUrlOption.withQueryParams(mapOf("uploadType" to "resumable"))
        )
    }

    override fun downloadUrl(furniture: Furniture): Result<URL?> = runCatching {
        signURL("${furniture.deviceId}/${furniture.name}-virtualized", HttpMethod.GET)
    }
}