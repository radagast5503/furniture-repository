package com.muebles.ra.server.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.muebles.ra.utils.Config
import io.ktor.application.*
import org.springframework.context.annotation.AnnotationConfigApplicationContext


fun Application.initializeFirebase(ctx: AnnotationConfigApplicationContext) {
    val config = ctx.getBean(Config::class.java)
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .setServiceAccountId(config.firebaseServiceAccountId())
        .setProjectId(config.projectName())
        .build()

    FirebaseApp.initializeApp(options)
}