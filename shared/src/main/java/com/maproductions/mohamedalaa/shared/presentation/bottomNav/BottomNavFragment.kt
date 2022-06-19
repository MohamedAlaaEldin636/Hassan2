package com.maproductions.mohamedalaa.shared.presentation.bottomNav

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.MenuItemVisibility
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.inflateGraph
import com.maproductions.mohamedalaa.shared.core.extensions.inflateMenuViaRes
import com.maproductions.mohamedalaa.shared.databinding.FragmentBottomNavBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import timber.log.Timber

abstract class BottomNavFragment : MABaseFragment<FragmentBottomNavBinding>() {

    protected abstract val destinationsShowToolbar: Set<Int>

    protected open val destinationsHideNotificationsIcon: Set<Int> = setOf()

    protected abstract val menuResOfBottomNav: Int

    protected abstract val navigationResOfGraph: Int

    protected open val initialGraphArgs: Bundle? get() = null

    protected abstract val startDestinationId: Int

    private val onDestinationChangedListener = NavController.OnDestinationChangedListener { _, destination, _ ->
        run {
            binding?.root?.post {
                activityViewModel?.showToolbar?.value = destination.id in destinationsShowToolbar
                activityViewModel?.showNotificationsIcon?.value = destination.id !in destinationsHideNotificationsIcon
                destination.label?.toString().orEmpty().also {
                    if (it.isNotEmpty()) {
                        activityViewModel?.titleToolbar?.value = it
                    }
                }
            }
        }
    }

    private var onBackPressedCallback: OnBackPressedCallback? = null

    override fun getLayoutId(): Int = R.layout.fragment_bottom_nav

    private fun adjustBottomNavigationView(navController: NavController) {
        binding?.bottomNavigationView?.inflateMenuViaRes(menuResOfBottomNav)
        getSelectedItemIdFromCurrentDestinationId(navController.currentDestination?.id)?.also {
            binding?.bottomNavigationView?.selectedItemId = it
        }
        binding?.bottomNavigationView?.setOnItemSelectedListener {
            onItemSelectedListener(navController, it)
            // change to be selected instead of ignoring the click.
        }
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedCallback = activity?.onBackPressedDispatcher?.addCallback(this, true) {
            onBackPressedCallback?.isEnabled = false

            activity?.finish()
        }
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.bottomNavNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.inflateGraph(navigationResOfGraph, initialGraphArgs, startDestinationId)

        navController.removeOnDestinationChangedListener(onDestinationChangedListener)
        navController.addOnDestinationChangedListener(onDestinationChangedListener)

        adjustBottomNavigationView(navController)
    }

    /**
     * - Example
     *
     * - binding?.bottomNavigationView?.selectedItemId = when (navController.currentDestination?.id) {
     * - R.id.dest_home_user -> R.id.action_home
     * - R.id.dest_orders_user -> R.id.action_requests
     * - R.id.dest_page_user -> R.id.action_my_page
     * - R.id.dest_more_user -> R.id.action_mode_bar
     * - else -> return
     * - }
     */
    abstract fun getSelectedItemIdFromCurrentDestinationId(destinationId: Int?): Int?

    /**
     * - when (it.itemId) {
     * - R.id.action_home -> {
     * - navController.navigate(NavUserBottomNavDirections.actionGlobalDestHomeUser())
     * - }
     * - R.id.action_requests -> {
     * - navController.navigate(NavUserBottomNavDirections.actionGlobalDestOrdersUser(true))
     * - }
     * - R.id.action_my_page -> {
     * - navController.navigate(NavUserBottomNavDirections.actionGlobalDestPageUser())
     * - }
     * - R.id.action_more -> {
     * - navController.navigate(NavUserBottomNavDirections.actionGlobalDestMoreUser(true))
     * - }
     * - }
     *
     * @return true if make the selection change of menu item
     */
    abstract fun onItemSelectedListener(navController: NavController, menuItem: MenuItem): Boolean

}
