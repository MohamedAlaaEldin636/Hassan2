package com.maproductions.mohamedalaa.hassanu.presentation.bottomNav

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.NavBottomNavDirections
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.pusher.ResponsePusherOrder
import com.maproductions.mohamedalaa.shared.presentation.bottomNav.BottomNavFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class UserBottomNavFragment : BottomNavFragment() {

    private val args by navArgs<UserBottomNavFragmentArgs>()

    override val destinationsShowToolbar: Set<Int> = setOf(
        R.id.dest_orders,
        R.id.dest_conversations,
        R.id.dest_settings,
    )

    override val menuResOfBottomNav: Int = R.menu.menu_bottom_nav

    override val navigationResOfGraph: Int = R.navigation.nav_bottom_nav

    override val initialGraphArgs: Bundle get() = args.toBundle()

    override val startDestinationId: Int get() = args.startDestinationId

    @Inject
    lateinit var gson: Gson

    private val channelEvent by lazy {
        if (args.isGuest) null else PusherUtils.getChannelEvent(
            PusherUtils.getChannelNameOnChangeOrderStatusForAccount(
                runBlocking { prefsAccount.getUserData().first()!!.id }
            ),
            PusherUtils.EVENT_NAME_ON_CHANGE_ORDER_STATUS_FOR_ACCOUNT
        ) { data ->
            Timber.e("order status data ->\n$data")

            val response = ResponsePusherOrder.fromJson(data, gson)

            if (response.order?.statusOfOrder == ApiOrderStatus.FINISHED) {
                Handler(Looper.getMainLooper()).post {
                    Timber.e("status is finished")

                    kotlin.runCatching {
                        val navController = findNavControllerOfProject()

                        navController.executeWhenLoadingEndsIfExists {
                            navController.navigateDeepLinkWithoutOptions(
                                "dialog-dest",
                                "com.grand.hassan.shared.rate.provider.dialog",
                                paths = arrayOf(
                                    response.order?.provider?.id.orZero().toString(),
                                    response.order?.category?.name.orEmpty()
                                )
                            )
                        }
                    }
                }
            }else if (response.order?.statusOfOrder == ApiOrderStatus.ACCEPTED) {
                Handler(Looper.getMainLooper()).post {
                    Timber.e("status is ACCEPTED")

                    kotlin.runCatching {
                        val navController = findNavControllerOfProject()

                        navController.executeWhenLoadingEndsIfExists {
                            navController.navigateDeepLinkWithoutOptions(
                                "dialog-dest",
                                "com.grand.hassan.shared.accepted.provider.service.request.dialog",
                                paths = arrayOf(
                                    response.order?.id.orZero().toString(),
                                    response.order?.provider?.name.orEmpty()
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun getSelectedItemIdFromCurrentDestinationId(destinationId: Int?): Int? {
        return when (destinationId) {
            R.id.dest_home -> R.id.action_home
            R.id.dest_conversations -> R.id.action_conversations
            R.id.dest_orders -> R.id.action_orders
            R.id.dest_settings -> R.id.action_options
            else -> null
        }
    }

    override fun onItemSelectedListener(navController: NavController, menuItem: MenuItem): Boolean {
        val directions = when (menuItem.itemId) {
            R.id.action_home -> NavBottomNavDirections.actionGlobalDestHome(args.isGuest)
            R.id.action_orders -> {
                if (args.isGuest) {
                    findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                        "fragment-dest",
                        "com.grand.hassan.shared.guest.please.login.dialog"
                    )

                    return false
                }

                NavBottomNavDirections.actionGlobalDestOrders()
            }
            R.id.action_conversations -> {
                if (args.isGuest) {
                    findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                        "fragment-dest",
                        "com.grand.hassan.shared.guest.please.login.dialog"
                    )

                    return false
                }

                NavBottomNavDirections.actionGlobalDestConversations()
            }
            R.id.action_options -> NavBottomNavDirections.actionGlobalDestSettings(args.isGuest)
            else -> return false
        }

        navController.navigate(directions)

        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        channelEvent?.subscribe()

        activityViewModel?.getNotificationsCount(this, args.isGuest) {
            Timber.e("count of notification is ${it.data}")

            lifecycleScope.launch {
                prefsAccount.setNotificationsCount(it.data.orZero())
            }

            super.onViewCreated(view, savedInstanceState)
        }
    }

    override fun onDestroyView() {
        channelEvent?.unsubscribe()

        super.onDestroyView()
    }

}
