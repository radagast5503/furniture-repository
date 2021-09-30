package com.muebles.ra.messaging

import com.google.firebase.messaging.*
import com.muebles.ra.api.NotificationStatus
import com.muebles.ra.api.PubSubNotification
import org.springframework.stereotype.Component

@Component
class PushSenderService {

    fun sendNotificationToPhone(notification: PubSubNotification) {
        val message: Message = Message.builder()
            .setNotification(Notification.builder().setBody(body(notification.status))
                .setTitle(title(notification.status)).setImage("https://image.flaticon.com/icons/png/512/1198/1198317.png")
                .build())
            .putData("furniture_status", notification.status)
            .putData("furniture_name", notification.furnitureName)
            .setAndroidConfig(AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                    .setIcon("https://serving.photos.photobox.com/560818053260870777ccf605967aac30263cfd812e74836ebdb724893126c3dd476cc3a0.jpg")
                    .setColor("#715696")
                    .build())
                .setPriority(AndroidConfig.Priority.HIGH)
                .build())
            .setToken(notification.fcm)
            .build()
        // Send a message to the device corresponding to the provided
        // registration token.
        val response = FirebaseMessaging.getInstance().send(message)
        // Response is a message ID string.
        println("Successfully sent message: $response")
    }

    private fun title(status: String) : String {
        return if (NotificationStatus.SUCCESS.statusName == status)  "Tu mueble esta listo!" else "Tuvimos un error :("
    }

    private fun body(status: String) : String {
        return if (NotificationStatus.SUCCESS.statusName == status)  "Baja tu mueble para empezar a usarlo."
                else "Hubo un problema al virtualizar tu mueble. Por favor intentalo de nuevo."
    }
}