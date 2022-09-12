package com.maproductions.mohamedalaa.shared.core

import android.app.PendingIntent
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.RemoteMessage
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.NotificationsUtils
import com.maproductions.mohamedalaa.shared.core.customTypes.StaticValues
import com.maproductions.mohamedalaa.shared.domain.notifications.NotificationType
import com.pusher.pushnotifications.fcm.MessagingService
import org.json.JSONObject
import timber.log.Timber

abstract class MyPushNotificationService : MessagingService() {

    /**
     * @return `null` to do nothing as a result, otherwise launch the notification with that
     * [PendingIntent] isa.
     */
    abstract fun onMessageReceivedGetNotificationPendingIntent(
        title: String, body: String, type: NotificationType?, targetId: Int?, otherId: Int?
    ): PendingIntent?

    final override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.e("NotificationsService -> isUser ${StaticValues.isUserNotProvider} -> " +
                "Got a remote message -> {remoteMessage.data}\n${remoteMessage.data.toList().joinToString(".\n."){
                    "${it.first} : ${it.second}"
                }}")

        val model = getModelFromMap(remoteMessage.data)

        val title = model?.title ?: getString(R.string.app_name)
        val body = model?.body ?: getString(R.string.app_name)

        val otherId = remoteMessage.data["message-details"]?.let { json ->
            kotlin.runCatching {
                JSONObject(json).optInt("sender_id")
            }.getOrNull()
        }

        val pendingIntent = onMessageReceivedGetNotificationPendingIntent(
            title, body, model?.type, model?.targetId, otherId
        )

        NotificationsUtils.showNotification(
            pendingIntent ?: return,
            applicationContext,
            title,
            body,
            model?.type,
            model?.sound
        )
    }

    /**
    * example -> target_id=63, body= تم إضافة خدمات إضافية للطلب, type=order, sound=default, title= خدمات إضافية}
    */
    private fun getModelFromMap(map: Map<String, String>): NotificationModel? {
        val title = map["title"] ?: return null
        val body = map["body"] ?: return null
        val type = map["type"]?.let { type ->
            NotificationType.values().firstOrNull { it.apiValue == type }
        } ?: return null
        //val sound = map.getValue("sound")
        val targetId = map["target_id"]?.toInt() ?: return null

        return NotificationModel(title, body, type, targetId, map["sound"])
    }

    data class NotificationModel(
        var title: String,
        var body: String,
        var type: NotificationType,
        //var sound: String,
        var targetId: Int,
        var sound: String?,
    )

}
