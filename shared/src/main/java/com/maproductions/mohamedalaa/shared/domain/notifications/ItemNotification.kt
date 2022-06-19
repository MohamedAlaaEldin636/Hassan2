package com.maproductions.mohamedalaa.shared.domain.notifications

import com.google.gson.annotations.SerializedName

data class ItemNotification(
    @SerializedName("target_id") var targetId: Int?,
    var title: String,
    var description: String,
    @SerializedName("notify_type") var notifyType: String,
    @SerializedName("created_at") var createdAt: String,
) {
    val notificationType get() = NotificationType.values().first { it.apiValue == notifyType }
}
/*
"title": "Welcome To Application",
                "description": "Welcome To Application",
                "notify_type": "admin",
                "created_at": "16 March 2022 14:50"
            }
 */
