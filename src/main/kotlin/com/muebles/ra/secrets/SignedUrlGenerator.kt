package com.muebles.ra.secrets

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.storage.*
import com.muebles.ra.utils.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface URLObtainer {
    fun uploadUrl(objectName: String?): Result<URL?>
}

@Configuration
open class SpringConfig {
    @Bean
    open fun urlObtainer(cfg: Config, keyObtainer: KeyObtainer): URLObtainer {
        val env = System.getenv("ENVIRONMENT") ?: "local"
        if(!env.equals("local")) {
            return com.muebles.ra.secrets.SignedUrlGenerator(cfg,keyObtainer)
        }
        return FakeUrlObtainer()
    }
}

class FakeUrlObtainer: URLObtainer {
    override fun uploadUrl(objectName: String?): Result<URL?> {
        return Result.success(URL("https://fake.com/itstrue"))
    }

}

class SignedUrlGenerator constructor(val cfg: Config, keyObtainer: KeyObtainer): URLObtainer {

    private val credentials = ServiceAccountCredentials.fromStream(ByteArrayInputStream(keyObtainer.getKey()))

    private val storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(cfg.projectName()).build().service

    override fun uploadUrl(objectName: String?): Result<URL?> = runCatching {
        val blobInfo = BlobInfo.newBuilder(BlobId.of(cfg.bucketName(), objectName)).build()
        val urlDuration = cfg.urlDuration()
        checkNotNull(urlDuration) { "could not read duration from config" }
        storage.signUrl(
            blobInfo, urlDuration, TimeUnit.MINUTES,
            Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
            Storage.SignUrlOption.withV4Signature()
        )
    }
}