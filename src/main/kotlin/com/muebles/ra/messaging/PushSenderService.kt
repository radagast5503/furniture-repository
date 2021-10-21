package com.muebles.ra.messaging

import com.google.firebase.messaging.*
import com.muebles.ra.api.NotificationStatus
import com.muebles.ra.api.PubSubNotification
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PushSenderService {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun sendNotificationToPhone(notification: PubSubNotification) {
        val message: Message = Message.builder()
            .putData("furniture_status", notification.status)
            .putData("furniture_name", notification.furnitureName)
            .setToken(notification.fcm)
            .build()
        // Send a message to the device corresponding to the provided
        // registration token.
        val response = FirebaseMessaging.getInstance().send(message)
        // Response is a message ID string.
        logger.info("Successfully sent message: $response")
    }
}