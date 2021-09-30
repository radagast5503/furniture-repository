package com.muebles.ra.server.config

import com.muebles.ra.pubsub.PubSubSubscriber
import io.ktor.application.*
import org.springframework.context.annotation.AnnotationConfigApplicationContext

fun Application.pubSubSubscriberCreateSubscription(ctx: AnnotationConfigApplicationContext) {
    ctx.getBean(PubSubSubscriber::class.java).createSubscription()
}