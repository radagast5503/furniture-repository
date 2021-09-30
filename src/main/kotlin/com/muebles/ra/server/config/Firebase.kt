package com.muebles.ra.server.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.application.*


fun Application.initializeFirebase() {
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .setServiceAccountId("AAAAMXTaxWg:APA91bEgOPr1j2oBHQxhmmJKtSRHkq91ppZxivRtYsIotoGt6Ww2ox7Yj8tTvCvYL1gdXm0_XIFHd35wR_wwKkliVa6NPyGoFVMh-0AZdN5dbuZtWb2rtR3MSx5GKnHSCacfsyQJc-hA")
        .setProjectId("muebles-ra-a108")
        .build()

    FirebaseApp.initializeApp(options)
}