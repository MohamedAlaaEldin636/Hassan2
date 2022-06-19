package com.maproductions.mohamedalaa.shared.presentation.main.viewModels

import android.location.Location
import android.net.Uri
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.findFragment
import androidx.lifecycle.*
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.*
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import com.maproductions.mohamedalaa.shared.databinding.ActivityMainBinding
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.location.LocationTrackingFragment
import com.maproductions.mohamedalaa.shared.presentation.main.SharedMainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prefsAccount: PrefsAccount,
    repoSettings: RepoSettings,
) : ViewModel() {

    private val database by lazy { Firebase.database }
    private val referenceLocation by lazy {
        database.getReference(LocationTrackingFragment.KEY_DATABASE_REFERENCE_BASE)
    }

    var locationIsBeingTracked = false

    private val retryAbleFlowNotificationsCount = RetryAbleFlow(repoSettings::getNotificationsCount)
    private var responseNotificationsCount: MABaseResponse<Int>? = null

    val showToolbar = MutableLiveData(false)

    val titleToolbar = MutableLiveData("")

    val globalLoading = MutableLiveData(false)

    val globalError = MutableLiveData<GlobalError>(GlobalError.Cancel)

    private val notificationsCount = prefsAccount.getNotificationsCount().asLiveData()
    val showNotificationsIcon = MutableLiveData(false)
    val menuItemNotificationsVisibility = switchMapMultiple2(notificationsCount, showNotificationsIcon) {
        if (showNotificationsIcon.value == true) {
            MutableLiveData(
                if (notificationsCount.value.orZero() > 0) {
                    MenuItemVisibility.SHOW_HAVING_NEW_DATA
                }else {
                    MenuItemVisibility.SHOW
                }
            )
        }else {
            MutableLiveData(MenuItemVisibility.HIDE)
        }
    }

    private val listOfOrdersIdsForTracking = mutableSetOf<Int>()

    var channelHasBeenSubscribed = false

    fun onNotificationsClick(view: View) {
        viewModelScope.launch {
            prefsAccount.setNotificationsCount(0)

            Timber.e("hello 0")

            val owner = DataBindingUtil.findBinding<ViewDataBinding>(view)?.lifecycleOwner
                ?: return@launch

            Timber.e("hello 1 ${owner is View} $owner ${owner.javaClass}")

            val activity = when (owner) {
                is Fragment -> owner.requireActivity()
                is FragmentActivity -> owner
                else -> kotlin.runCatching {
                    view.findFragment<Fragment>().activity
                }.getOrNull() ?: return@launch
            }

            Timber.e("hello 2")

            Navigation.findNavController(
                activity,
                R.id.mainNavHostFragment
            ).navigateDeepLinkWithOptions(
                "fragment-dest",
                "com.grand.hassan.shared.notifications.list",
            )
        }
    }

    fun <VDB : ViewDataBinding, F : MABaseFragment<VDB>> getNotificationsCount(
        fragment: F,
        isGuest: Boolean,
        onSuccess: (MABaseResponse<Int>) -> Unit
    ) {
        if (isGuest) {
            return onSuccess(MABaseResponse(0, "", 200))
        }

        responseNotificationsCount?.let {
            onSuccess(it)
        } ?: fragment.handleRetryAbleFlowWithMustHaveResultWithNullability(retryAbleFlowNotificationsCount) {
            responseNotificationsCount = it

            onSuccess(it)
        }
    }

    fun changeTrackingForOrder(orderId: Int, startTrackingElseStopIt: Boolean, listener: Listener) {
        Timber.e("abc333 orderId $orderId $startTrackingElseStopIt")

        val oldSize = listOfOrdersIdsForTracking.size
        if (startTrackingElseStopIt) {
            listOfOrdersIdsForTracking += orderId
        }else {
            listOfOrdersIdsForTracking -= orderId
        }

        Timber.e("abc333 listOfOrdersIdsForTracking $listOfOrdersIdsForTracking")

        if (listOfOrdersIdsForTracking.size != oldSize) {
            checkIfShouldRequestLocationUpdates(listener)
        }
    }

    fun checkIfShouldRequestLocationUpdates(listener: Listener) {
        if (listOfOrdersIdsForTracking.size > 0) {
            listener.startLocationTrackingAfterCheckingPermissions()
        }else {
            listener.stopLocationTracking()
        }
    }

    fun afterLocationChange(location: Location) {
        val copiedList = listOfOrdersIdsForTracking.toList()

        for (orderId in copiedList) {
            referenceLocation.child(orderId.toString()).setValue(
                LocationLatLng(
                    location.latitude.toString(),
                    location.longitude.toString()
                )
            ) { error, _ ->
                Timber.e("Periodic Location error $error\n msg -> ${error?.message}")
            }
        }
    }

    interface Listener {
        fun startLocationTrackingAfterCheckingPermissions()

        fun stopLocationTracking()
    }

}
