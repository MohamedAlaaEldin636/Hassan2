package com.maproductions.mohamedalaa.shared.core

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.maproductions.mohamedalaa.shared.core.customTypes.NotificationsUtils
import com.maproductions.mohamedalaa.shared.core.customTypes.StaticValues
import org.json.JSONObject
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Timber.e("fffffffffffffffffffffff onNewToken - $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        /*NotificationsUtils.showNotificationToLaunchMainActivity(
            applicationContext,
            "title",
            "NotificationsService -> isUser ${StaticValues.isUserNotProvider} -> " +
                    "Got a remote message -> ${kotlin.runCatching { remoteMessage.data }.getOrNull()}"
        )*/

        Timber.e("fffffffffffffffffffffff message key/value pairs -> ${remoteMessage.data}")

        val (title, body) = remoteMessage.data.getValue("message").orEmpty()
            .getNotificationModel()

        Timber.e("fffffffffffffffffffffff hmmmmmmmmmmmmmmmmmmmmm")

        /*NotificationUtils.showNotificationToLaunchMainActivityForFirebaseNotification(
            this,
            title,
            body
        )*/
    }

    private data class NotificationModel(
        val title: String,
        val body: String,
    )

    private fun String.getNotificationModel(): NotificationModel {
        val jsonObject = JSONObject(this)

        val title = jsonObject.getString("title")
        val body = jsonObject.getString("body")

        return NotificationModel(title, body)
    }
    /*
    public static BaseNotificationModel getNotification(String mJsonString) {
        JsonParser parser = new JsonParser();
        JsonElement mJson = parser.parse(mJsonString);
        Gson gson = new Gson();
        return gson.fromJson(mJson, BaseNotificationModel.class);
    }
     */

}
