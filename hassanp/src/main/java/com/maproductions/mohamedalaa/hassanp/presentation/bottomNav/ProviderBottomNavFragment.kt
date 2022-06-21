package com.maproductions.mohamedalaa.hassanp.presentation.bottomNav

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.NavBottomNavDirections
import com.maproductions.mohamedalaa.hassanp.R
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
class ProviderBottomNavFragment : BottomNavFragment() {

    private val args by navArgs<ProviderBottomNavFragmentArgs>()

    @Inject
    lateinit var gson: Gson

    private val channelEvent by lazy {
        PusherUtils.getChannelEvent(
            PusherUtils.getChannelNewOrder(
                runBlocking { prefsAccount.getProviderData().first()!!.id }
            ),
            PusherUtils.EVENT_NAME_NEW_ORDER
        ) { data ->
            Timber.e("chat details messages ->\n$data\n" +
                    "${kotlin.runCatching { ResponsePusherOrder.fromJson(data, gson).order }.getOrElse { it }}")

            Handler(Looper.getMainLooper()).post {
                kotlin.runCatching {
                    val navController = findNavControllerOfProject()

                    navController.executeWhenLoadingEndsIfExists {
                        navController.navigateDeepLinkWithoutOptions(
                            "dialog-dest",
                            "com.grand.hassan.shared.new.order.dialog",
                            data
                        )
                    }
                }
            }
        }
    }

    override val destinationsShowToolbar: Set<Int> = setOf(
        R.id.dest_orders,
        R.id.dest_conversations,
        R.id.dest_my_account,
        R.id.dest_more,
    )

    override val destinationsHideNotificationsIcon: Set<Int> = setOf(
        R.id.dest_orders,
        R.id.dest_conversations,
        R.id.dest_my_account,
        R.id.dest_more,
    )

    override val menuResOfBottomNav: Int = R.menu.menu_bottom_nav

    override val navigationResOfGraph: Int = R.navigation.nav_bottom_nav

    override val startDestinationId: Int get() = if (args.suspendAccount) {
        R.id.dest_my_account
    }else {
        args.startDestinationId
    }

    override fun getSelectedItemIdFromCurrentDestinationId(destinationId: Int?): Int? {
        return when (destinationId) {
            R.id.dest_home -> R.id.action_home
            R.id.dest_conversations -> R.id.action_conversations
            R.id.dest_orders -> R.id.action_orders
            R.id.dest_my_account -> R.id.action_my_account
            R.id.dest_more -> R.id.action_more
            else -> null
        }
    }

    override fun onItemSelectedListener(navController: NavController, menuItem: MenuItem): Boolean {
        if (args.suspendAccount && menuItem.itemId != R.id.action_my_account) {
            context?.showErrorToast(getString(com.maproductions.mohamedalaa.shared.R.string.you_must_fill_your_data))

            return false
        }

        val directions = when (menuItem.itemId) {
            R.id.action_home -> NavBottomNavDirections.actionGlobalDestHome()
            R.id.action_conversations -> NavBottomNavDirections.actionGlobalDestConversations()
            R.id.action_orders -> NavBottomNavDirections.actionGlobalDestOrders()
            R.id.action_my_account -> NavBottomNavDirections.actionGlobalDestMyAccount(args.suspendAccount)
            R.id.action_more -> NavBottomNavDirections.actionGlobalDestMore()
            else -> return false
        }

        navController.navigate(directions)

        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activityViewModel?.getNotificationsCount(this, false) {
            Timber.e("count of notification is ${it.data}")

            lifecycleScope.launch {
                prefsAccount.setNotificationsCount(it.data.orZero())
            }

            super.onViewCreated(view, savedInstanceState)

            channelEvent.subscribe()
        }
    }

    override fun onDestroyView() {
        channelEvent.unsubscribe()

        super.onDestroyView()
    }

}
