package com.muebles.ra.secrets

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.storage.*
import com.muebles.ra.utils.Config
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Service
class SignedUrlGenerator @Inject constructor(val cfg: Config, keyObtainer: KeyObtainer) {

    private val credentials = ServiceAccountCredentials.fromStream(ByteArrayInputStream(keyObtainer.getKey()))

    private val storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(cfg.projectName()).build().service

    fun uploadUrl(objectName: String): Result<URL?> = runCatching {
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