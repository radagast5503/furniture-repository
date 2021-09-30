package com.muebles.ra.pubsub

import com.google.cloud.pubsub.v1.AckReplyConsumer
import com.google.cloud.pubsub.v1.MessageReceiver
import com.google.cloud.pubsub.v1.Subscriber
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.pubsub.v1.PubsubMessage
import com.muebles.ra.api.PubSubNotification
import com.muebles.ra.messaging.PushSenderService
import com.muebles.ra.utils.Config
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.inject.Inject

@Component
class PubSubSubscriber @Inject constructor(private val pushSenderService: PushSenderService) {

    @Throws(Exception::class)
    fun createSubscription() {
        // TODO(developer): Replace these variables before running the sample.
        val projectId = "muebles-ra-a108"
        val subscriptionId = "generated-furniture-sub"
        subscribeAsync(projectId, subscriptionId)
    }

    private fun subscribeAsync(projectId: String?, subscriptionId: String?) {
        val subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId)
        val receiver = messageReceiver()
        var subscriber: Subscriber? = null
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build()
            subscriber.startAsync().awaitRunning()
            System.out.printf("Listening for messages on %s:\n", subscriptionName.toString())
            subscriber.awaitTerminated(30, TimeUnit.SECONDS)
        } catch (timeoutException: TimeoutException) {
            subscriber?.stopAsync()
        }
    }

    private fun messageReceiver(): MessageReceiver {
        return MessageReceiver { message: PubsubMessage, consumer: AckReplyConsumer ->
            println("Data: " + message.data.toStringUtf8())
            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
            val notification = gson.fromJson(message.data.toStringUtf8(), PubSubNotification::class.java)
            pushSenderService.sendNotificationToPhone(notification)
            consumer.ack()
        }
    }
}