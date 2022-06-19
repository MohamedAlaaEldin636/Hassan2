package com.maproductions.mohamedalaa.shared.presentation.main

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.activity.viewModels
import androidx.annotation.CallSuper
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.os.bundleOf
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.messaging.FirebaseMessaging
import com.maproductions.mohamedalaa.shared.BuildConfig
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.GlobalError
import com.maproductions.mohamedalaa.shared.core.customTypes.MenuItemVisibility
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.customTypes.StaticValues
import com.maproductions.mohamedalaa.shared.core.extensions.inflateGraph
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import com.maproductions.mohamedalaa.shared.core.extensions.setupWithNavControllerMA
import com.maproductions.mohamedalaa.shared.databinding.ActivityMainBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseActivity
import com.maproductions.mohamedalaa.shared.presentation.main.viewModels.MainViewModel
import com.pusher.pushnotifications.PushNotifications
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

abstract class SharedMainActivity : MABaseActivity<ActivityMainBinding>() {

    val viewModel by viewModels<MainViewModel>()

    protected abstract val graphNavigationRes: Int

    protected abstract val destinationsHideToolbar: Set<Int>
    private val allDestinationsHideToolbar by lazy {
        destinationsHideToolbar + setOf(
            R.id.dest_splash,
            R.id.dest_on_board,
            R.id.dest_image_with_text_and_title,
            R.id.dest_location_selection,
            R.id.dest_chat_details,
            R.id.dest_location_viewer,
            R.id.dest_location_tracking,
        )
    }

    protected abstract val destinationsIgnoreToolbarVisibility: Set<Int>
    private val allDestinationsIgnoreToolbarVisibility by lazy {
        destinationsIgnoreToolbarVisibility + setOf(
            R.id.dest_global_error_dialog,
            R.id.dest_lottie_loader_dialog,
            R.id.dest_showing_image_dialog,
            R.id.dest_video_player_dialog,
            R.id.dest_cancel_order_dialog
        )
    }

    protected abstract val topLevelDestinations: Set<Int>
    private val allTopLevelDestinations by lazy {
        topLevelDestinations/* + setOf(

        )*/
    }

    protected abstract val notTransparentToolbarDestinations: Set<Int>
    private val allNotTransparentToolbarDestinations by lazy {
        notTransparentToolbarDestinations/* + setOf(

        )*/
    }

    protected abstract val destinationsShowNotificationIcon: Set<Int>
    private val allDestinationsShowNotificationIcon by lazy {
        destinationsShowNotificationIcon
    }

    protected abstract val pusherInterests: Set<String>

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    @CallSuper
    override fun setupsInOnCreate() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.d("Firebase Token exception -> ${task.exception}")
            }else {
                Timber.d("Firebase Token -> ${task.result}")
            }
        }
        PushNotifications.start(applicationContext, PusherUtils.BEAMS_INSTANCE_ID)
        if (BuildConfig.DEBUG) {
            PushNotifications.addDeviceInterest("debug-test")
        }
        for (interest in pusherInterests) {
            PushNotifications.addDeviceInterest(interest)
        }

        // Support action bar so that fragments have easier access for toolbar menu items.
        setSupportActionBar(binding?.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Setups
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.mainNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.inflateGraph(graphNavigationRes)
        binding?.materialToolbar?.setupWithNavControllerMA(
            navController,
            AppBarConfiguration(allTopLevelDestinations)
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            run {
                if (destination.id !in allDestinationsIgnoreToolbarVisibility) {
                    viewModel.showToolbar.value = destination.id !in allDestinationsHideToolbar

                    viewModel.showNotificationsIcon.value = destination.id in allDestinationsShowNotificationIcon
                }
                destination.label?.toString().orEmpty().also {
                    if (it.isNotEmpty()) {
                        viewModel.titleToolbar.postValue(it)
                    }
                }

                if (destination.id !in allTopLevelDestinations) {
                    /*val forceNormalToolbar = destination.id == R.id.dest_search_results_providers
                        && SearchResultsProvidersFragmentArgs.fromBundle(arguments.orEmpty()).categoryName != null*/
                    val (color, drawable) = if (destination.id in allNotTransparentToolbarDestinations/* && !forceNormalToolbar*/) {
                        getColor(R.color.white) to ColorDrawable(getColor(R.color.black))
                    }else {
                        getColor(R.color.black) to ColorDrawable(getColor(R.color.app_background))
                    }
                    binding?.materialToolbar?.post {
                        (binding?.materialToolbar?.navigationIcon as? DrawerArrowDrawable)?.color = color
                        binding?.materialToolbar?.setTitleTextColor(color)
                        binding?.materialToolbar?.background = drawable
                    }
                }
            }
        }

        viewModel.globalLoading.distinctUntilChanged().observe(this) {
            if (it == true) {
                navController.navigateDeepLinkWithoutOptions(
                    "dialog-dest",
                    "com.grand.hassan.shared.lottie.loader.dialog"
                )
            }
        }

        viewModel.globalError.distinctUntilChanged().observe(this) {
            if (it is GlobalError.Show) {
                if (it.canCancelDialog) {
                    navController.navigateDeepLinkWithoutOptions(
                        "dialog-dest",
                        "com.grand.hassan.shared.global.error.dialog.cancellable",
                        if (it.errorMsg.isNullOrEmpty()) getString(R.string.something_went_wrong) else it.errorMsg,
                        true.toString()
                    )
                }else {
                    navController.navigateDeepLinkWithoutOptions(
                        "dialog-dest",
                        "com.grand.hassan.shared.global.error.dialog",
                        if (it.errorMsg.isNullOrEmpty()) getString(R.string.something_went_wrong) else it.errorMsg,
                    )
                }
            }
        }
    }

}