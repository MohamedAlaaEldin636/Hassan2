package com.maproductions.mohamedalaa.hassanp.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.presentation.order.OrderDetailsFragment
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationHandler
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.executeShowingErrorOnce
import com.maproductions.mohamedalaa.shared.core.extensions.showNormalToast
import com.maproductions.mohamedalaa.shared.core.extensions.showSuccessToast
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.domain.pusher.ResponsePusherOrder
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.shared.presentation.main.SharedMainActivity
import com.maproductions.mohamedalaa.shared.presentation.main.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : SharedMainActivity(), MainViewModel.Listener, LocationHandler.Listener {

    override val graphNavigationRes: Int
        get() = R.navigation.nav_main

    override val destinationsHideToolbar = setOf(
        R.id.dest_provider_bottom_nav,
        R.id.dest_log_in,
        R.id.dest_register_form,
    )

    override val destinationsIgnoreToolbarVisibility = setOf(
        R.id.dest_provider_bottom_nav,
        R.id.dest_accepted_order_dialog,
        R.id.dest_confirm_finishing_work_dialog,
        R.id.dest_money_received_dialog,
        R.id.dest_cancellation_reason_dialog,
        R.id.dest_new_order_dialog,
        R.id.dest_registration_done_dialog,
        R.id.dest_stop_recieving_orders_dialog,
    )

    override val topLevelDestinations = setOf(
        R.id.dest_provider_bottom_nav,
    )

    override val notTransparentToolbarDestinations = setOf(
        -1
    )

    override val destinationsShowNotificationIcon = setOf(
        R.id.dest_provider_bottom_nav,
    )

    override val pusherInterests = setOf(
        "providers",
    )

    @Inject
    lateinit var prefsAccount: PrefsAccount

    @Inject
    lateinit var gson: Gson

    private val channelEvent by lazy {
        PusherUtils.getChannelEvent(
            PusherUtils.getChannelNameOnChangeOrderStatusForAccount(
                runBlocking { prefsAccount.getProviderData().first()!!.id }
            ),
            PusherUtils.EVENT_NAME_ON_CHANGE_ORDER_STATUS_FOR_ACCOUNT
        ) { data ->
            Handler(Looper.getMainLooper()).post {
                val response = ResponsePusherOrder.fromJson(data, gson)

                Timber.e("MainActivity -> status is changed ${response.order?.apiOrderStatus}")

                val orderId = response.order?.id ?: return@post

                viewModel.changeTrackingForOrder(
                    orderId,
                    response.order?.apiOrderStatus == ApiOrderStatus.ON_THE_WAY,
                    this
                )
            }
        }
    }

    private lateinit var locationHandler: LocationHandler

    override fun setupsInOnCreate() {
        locationHandler = LocationHandler(
            this,
            lifecycle,
            this,
            this
        )

        super.setupsInOnCreate()

        viewModel.checkIfShouldRequestLocationUpdates(this)

        //subscribeToChannel()
    }

    fun subscribeToChannel() {
        runBlocking {
            if (prefsAccount.getProviderData().firstOrNull()?.id != null) {
                if (!viewModel.channelHasBeenSubscribed) {
                    Timber.e("Starting subscribing")

                    viewModel.channelHasBeenSubscribed = true

                    channelEvent.subscribe()
                }
            }
        }
    }

    override fun onDestroy() {
        channelEvent.unsubscribe()

        super.onDestroy()
    }

    override fun startLocationTrackingAfterCheckingPermissions() {
        if (viewModel.locationIsBeingTracked) {
            return
        }else {
            viewModel.locationIsBeingTracked = true
        }

        locationHandler.requestLocationUpdates()
    }

    override fun stopLocationTracking() {
        viewModel.locationIsBeingTracked = false

        locationHandler.stopLocationUpdates()
    }

    override fun onChangeLocationSuccess(location: Location) {
        showSuccessToast(getString(com.maproductions.mohamedalaa.shared.R.string.your_location_is_being_tracked))

        viewModel.afterLocationChange(location)
    }

    override fun onChangeLocationFailure(context: Context?, exception: Exception?) {
        executeShowingErrorOnce(
            false,
            getString(com.maproductions.mohamedalaa.shared.R.string.there_is_an_error_in_detecting_your_location)
        ) {
            locationHandler.requestLocationUpdates()
        }
    }

}