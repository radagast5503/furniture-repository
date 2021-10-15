package com.muebles.ra.messaging

import com.google.firebase.messaging.*
import com.muebles.ra.api.NotificationStatus
import com.muebles.ra.api.PubSubNotification
import org.springframework.stereotype.Component

@Component
class PushSenderService {

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
        println("Successfully sent message: $response")
    }

    private fun title(status: String) : String {
        return if (NotificationStatus.SUCCESS.statusName == status)  "Tu mueble esta listo!" else "Tuvimos un problema :("
    }

    private fun body(status: String) : String {
        return if (NotificationStatus.SUCCESS.statusName == status)  "Baja tu mueble para empezar a usarlo."
                else "Hubo un problema al virtualizar tu mueble. Por favor intentalo de nuevo."
    }
}