package com.maproductions.mohamedalaa.hassanu.core

import android.app.PendingIntent
import androidx.navigation.NavDeepLinkBuilder
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.presentation.bottomNav.UserBottomNavFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.order.OrderAdditionsFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.order.OrderDetailsFragmentArgs
import com.maproductions.mohamedalaa.shared.core.MyPushNotificationService
import com.maproductions.mohamedalaa.shared.core.customTypes.NotificationsUtils
import com.maproductions.mohamedalaa.shared.core.extensions.orZero
import com.maproductions.mohamedalaa.shared.domain.notifications.NotificationType
import com.maproductions.mohamedalaa.shared.presentation.chat.ChatDetailsFragmentArgs

class UserPushNotificationService : MyPushNotificationService() {
    
    override fun onMessageReceivedGetNotificationPendingIntent(
        title: String,
        body: String,
        type: NotificationType?,
        targetId: Int?,
        otherId: Int?
    ): PendingIntent {
        return when (type) {
            null, NotificationType.ADMIN -> NotificationsUtils.getMainActivityPendingIntent(applicationContext)
            NotificationType.ORDER -> {
                NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.nav_main)
                    .setDestination(
                        R.id.dest_user_bottom_nav,
                        UserBottomNavFragmentArgs(startDestinationId = R.id.dest_orders).toBundle()
                    )
                    .addDestination(
                        R.id.dest_order_details,
                        OrderDetailsFragmentArgs(targetId.orZero()).toBundle()
                    )
                    .createPendingIntent()
            }
            NotificationType.WALLET -> {
                NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.nav_main)
                    .setDestination(
                        R.id.dest_user_bottom_nav,
                        UserBottomNavFragmentArgs(startDestinationId = R.id.dest_settings).toBundle()
                    )
                    .addDestination(R.id.dest_wallet)
                    .createPendingIntent()
            }
            NotificationType.CONFIRM_ADDITIONAL_SERVICES -> {
                NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.nav_main)
                    .setDestination(R.id.dest_user_bottom_nav)
                    .addDestination(SR.id.dest_notifications_list)
                    .addDestination(
                        // solution can be add in xml destination like dest_notification but with new name bs kda isa.
                        R.id.dest_order_additions, 
                        OrderAdditionsFragmentArgs(targetId.orZero()).toBundle()
                    )
                    .createPendingIntent()
            }
            NotificationType.MESSAGE -> {
                NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.nav_main)
                    .setDestination(
                        R.id.dest_user_bottom_nav,
                        UserBottomNavFragmentArgs(startDestinationId = R.id.dest_conversations).toBundle()
                    )
                    .addDestination(
                        SR.id.dest_chat_details,
                        ChatDetailsFragmentArgs(otherId.orZero(), targetId.orZero()).toBundle()
                    )
                    .createPendingIntent()
            }
            NotificationType.COMPLETE_PROFILE -> {
                // Should never come to user but just in case just launch MainActivity isa.
                NotificationsUtils.getMainActivityPendingIntent(applicationContext)
            }
        }
    }

}
