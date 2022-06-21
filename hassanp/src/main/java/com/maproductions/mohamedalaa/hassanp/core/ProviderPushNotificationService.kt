package com.maproductions.mohamedalaa.hassanp.core

import android.app.PendingIntent
import androidx.navigation.NavDeepLinkBuilder
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanp.presentation.bottomNav.ProviderBottomNavFragmentArgs
import com.maproductions.mohamedalaa.hassanp.presentation.order.OrderDetailsFragmentArgs
import com.maproductions.mohamedalaa.shared.core.MyPushNotificationService
import com.maproductions.mohamedalaa.shared.core.customTypes.NotificationsUtils
import com.maproductions.mohamedalaa.shared.core.extensions.orZero
import com.maproductions.mohamedalaa.shared.domain.notifications.NotificationType
import com.maproductions.mohamedalaa.shared.presentation.chat.ChatDetailsFragmentArgs

class ProviderPushNotificationService : MyPushNotificationService() {

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
                        R.id.dest_provider_bottom_nav,
                        ProviderBottomNavFragmentArgs(startDestinationId = R.id.dest_orders).toBundle()
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
                        R.id.dest_provider_bottom_nav,
                        ProviderBottomNavFragmentArgs(startDestinationId = R.id.dest_my_account).toBundle()
                    )
                    .addDestination(R.id.dest_wallet)
                    .createPendingIntent()
            }
            NotificationType.CONFIRM_ADDITIONAL_SERVICES -> {
                // Should never come to provider but just in case just launch MainActivity isa.
                NotificationsUtils.getMainActivityPendingIntent(applicationContext)
            }
            NotificationType.MESSAGE -> {
                NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.nav_main)
                    .setDestination(
                        R.id.dest_provider_bottom_nav,
                        ProviderBottomNavFragmentArgs(startDestinationId = R.id.dest_conversations).toBundle()
                    )
                    .addDestination(
                        SR.id.dest_chat_details,
                        ChatDetailsFragmentArgs(otherId.orZero(), targetId.orZero()).toBundle()
                    )
                    .createPendingIntent()
            }
            NotificationType.COMPLETE_PROFILE -> {
                NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.nav_main)
                    .setDestination(
                        R.id.dest_provider_bottom_nav,
                        ProviderBottomNavFragmentArgs(
                            startDestinationId = R.id.dest_my_account,
                            suspendAccount = true
                        ).toBundle()
                    )
                    .addDestination(R.id.dest_personal_data)
                    .createPendingIntent()
            }
        }
    }
}
