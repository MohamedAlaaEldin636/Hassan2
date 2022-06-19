package com.maproductions.mohamedalaa.shared.core.customTypes

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.domain.notifications.NotificationType

object NotificationsUtils {

    private const val NOTIFICATIONS_CHANNEL_ID = "NOTIFICATIONS_CHANNEL_ID"
    private const val NOTIFICATIONS_NOTIFICATION_ID = 48

    private const val ORDER_DETAILS_CHANNEL_ID = "ORDER_DETAILS_CHANNEL_ID"
    private const val ORDER_DETAILS_NOTIFICATION_ID = 49

    private const val WALLET_DETAILS_CHANNEL_ID = "WALLET_DETAILS_CHANNEL_ID"
    private const val WALLET_DETAILS_NOTIFICATION_ID = 50

    private const val CONFIRM_ADDITIONAL_SERVICES_DETAILS_CHANNEL_ID = "CONFIRM_ADDITIONAL_SERVICES_DETAILS_CHANNEL_ID"
    private const val CONFIRM_ADDITIONAL_SERVICES_DETAILS_NOTIFICATION_ID = 51

    private const val MESSAGE_DETAILS_CHANNEL_ID = "MESSAGE_DETAILS_CHANNEL_ID"
    private const val MESSAGE_DETAILS_NOTIFICATION_ID = 52

    fun getMainActivityPendingIntent(applicationContext: Context): PendingIntent {
        val intent = Intent(applicationContext, StaticValues.mainActivityClazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        return PendingIntent.getActivity(
            applicationContext,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun showNotification(
        pendingIntent: PendingIntent,
        appContext: Context,
        title: String,
        body: String,
        type: NotificationType?,
    ) {
        val (channelId, channelName, notificationId) = when (type) {
            null, NotificationType.ADMIN -> Triple(
                NOTIFICATIONS_CHANNEL_ID,
                appContext.getString(R.string.the_notifications),
                NOTIFICATIONS_NOTIFICATION_ID
            )
            NotificationType.ORDER -> Triple(
                ORDER_DETAILS_CHANNEL_ID,
                appContext.getString(R.string.order_details),
                ORDER_DETAILS_NOTIFICATION_ID
            )
            NotificationType.WALLET -> Triple(
                WALLET_DETAILS_CHANNEL_ID,
                appContext.getString(R.string.wallet),
                WALLET_DETAILS_NOTIFICATION_ID
            )
            NotificationType.CONFIRM_ADDITIONAL_SERVICES -> Triple(
                CONFIRM_ADDITIONAL_SERVICES_DETAILS_CHANNEL_ID,
                appContext.getString(R.string.addition_of_services),
                CONFIRM_ADDITIONAL_SERVICES_DETAILS_NOTIFICATION_ID
            )
            NotificationType.MESSAGE -> Triple(
                MESSAGE_DETAILS_CHANNEL_ID,
                appContext.getString(R.string.chatting),
                MESSAGE_DETAILS_NOTIFICATION_ID
            )
        }

        showNotificationToLaunchPendingIntent(
            pendingIntent,
            appContext,
            title,
            body,
            channelId,
            channelName,
            notificationId
        )
    }

    private fun showNotificationToLaunchPendingIntent(
        pendingIntent: PendingIntent,
        applicationContext: Context,
        title: String,
        body: String,
        channelId: String,
        channelName: String,
        notificationId: Int,
        uri: Uri? = null
    ) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setSmallIcon(R.mipmap.ic_application_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_application_launcher))
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setAutoCancel(true)
            .setShowWhen(true)
            .setSound(uri ?: defaultSoundUri)
            .setDefaults(/*Notification.DEFAULT_SOUND or */Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
            .setContentIntent(pendingIntent)

        val notificationManager = applicationContext.getSystemService<NotificationManager>() ?: return

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            val audioAttrs = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                .build()
            channel.setSound(uri ?: defaultSoundUri, audioAttrs)
            notificationBuilder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}
