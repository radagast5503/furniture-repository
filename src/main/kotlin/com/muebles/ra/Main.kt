package com.muebles.ra

import com.muebles.ra.messaging.PushSenderService
import com.muebles.ra.pubsub.PubSubSubscriber
import com.muebles.ra.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(Netty,8080, host = "0.0.0.0") {
        initializeFirebase()
        val pushSenderApplication = PushSenderService();
        PubSubSubscriber(pushSenderApplication).createSubscription()
        router(injectionManagement())
        httpConfig()
        monitoring()
        serialization()
    }.start(wait = true)
}