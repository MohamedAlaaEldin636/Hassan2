package com.maproductions.mohamedalaa.hassanp.presentation.home

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.core.shouldUpdateProfileLocationToApi
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentHomeBinding
import com.maproductions.mohamedalaa.hassanp.presentation.home.viewModel.HomeViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.main.MainActivity
import com.maproductions.mohamedalaa.hassanp.presentation.order.OrderDetailsFragment
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationData
import com.maproductions.mohamedalaa.shared.core.customTypes.LocationHandler
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.splash.SplashInitialLaunch
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.search.SearchQueriesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.lang.Exception

@AndroidEntryPoint
class HomeFragment : MABaseFragment<FragmentHomeBinding>(), LocationHandler.Listener {

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var locationHandler: LocationHandler

    private val channelEvent by lazy {
        PusherUtils.getChannelEvent(
            PusherUtils.getChannelNewOrder(
                runBlocking { prefsAccount.getProviderData().first()!!.id }
            ),
            PusherUtils.EVENT_NAME_NEW_ORDER
        ) { data ->
            Timber.e("chat details messages ->\n$data")

            Handler(Looper.getMainLooper()).post {
                viewModel.adapter.refresh()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        locationHandler = LocationHandler(
            this,
            lifecycle,
            requireContext(),
            this
        )

        super.onCreate(savedInstanceState)

        (activity as? MainActivity)?.subscribeToChannel()
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initializeBindingVariables() {
        binding?.activityViewModel = activityViewModel
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        channelEvent.subscribe()

        binding?.recyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.recyclerView?.adapter = viewModel.adapter.withDefaultHeaderAndFooterAdapters()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.orders.collectLatest {
                    viewModel.adapter.submitData(it)
                }
            }
        }

        val savedStateHandle = findNavControllerOfProject().currentBackStackEntry?.savedStateHandle

        savedStateHandle?.actOnGetLiveDataToResetKey<Int?>(
            StopRecievingOrdersDialogFragment.SAVED_STATE_HOURS,
            null,
            viewLifecycleOwner,
            { it != null }
        ) {
            viewModel.stopReceivingOrders(it.orZero().toLong())
        }

        savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            SearchQueriesFragment.SAVED_STATE_TEXT,
            "",
            viewLifecycleOwner,
            { !it.isNullOrEmpty() }
        ) {
            viewModel.search.value = it.orEmpty()
        }

        savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            OrderDetailsFragment.SAVED_STATE_ORDER_DETAILS_MADE_CHANGE,
            false,
            viewLifecycleOwner,
            { it == true }
        ) {
            viewModel.adapter.refresh()
        }

        handleRetryAbleFlowWithMustHaveResultWithNullability(viewModel.retryAbleFlowAllStatuses) { response ->
            // Check suspended account flag
            if (response.data?.isSuspendedAccount == true) {
                viewModel.viewModelScope.launch {
                    prefsSplash.setInitialLaunch(SplashInitialLaunch.PROVIDER_ACCOUNT_SUSPENDED)

                    val navController = findNavController()

                    navController.popAllBackStacks()

                    navController.navigateDeepLinkWithOptions(
                        "fragment-dest",
                        "com.grand.hassan.shared.provider.bottom.nav.suspend.account",
                        paths = arrayOf(true.toString())
                    )
                }

                return@handleRetryAbleFlowWithMustHaveResultWithNullability
            }

            // Check orders flag isa.
            val countDown = response.data?.getHoursAndMinutesAndSeconds()
            if (countDown != null) {
                viewModel.stopReceivingOrders(countDown.first, countDown.second, countDown.third)
            }else if (response.data?.ordersFlag == 0) {
                viewModel.stopReceivingOrders(0L)
            }

            val onTheWayOrdersIds = response.data?.onTheWayOrders?.map { it.id }.orEmpty()
            if (shouldUpdateProfileLocationToApi()) {
                viewModel.onTheWayOrdersIds = onTheWayOrdersIds

                locationHandler.requestCurrentLocation(true)
            }else {
                // Check on the way orders
                (activity as? MainActivity)?.trackOrders(onTheWayOrdersIds)
            }
        }
    }

    override fun onCurrentLocationResultSuccess(location: Location) {
        val address = context?.getAddressFromLatitudeAndLongitude(
            location.latitude,
            location.longitude
        ) ?: getString(com.maproductions.mohamedalaa.shared.R.string.your_address_has_been_selected_successfully)

        executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            canCancelDialog = false,
            afterShowingLoading = {
                viewModel.repoAuth.updateProviderProfile(
                    LocationData(
                        location.latitude.toString(),
                        location.longitude.toString(),
                        address,
                    )
                )
            },
            afterHidingLoading = {
                // tam taghyeer 3nwanak b naga7 isa.
                Timber.e("tam taghyeer 3enwanak b naga7")

                // Check on the way orders
                (activity as? MainActivity)?.trackOrders(viewModel.onTheWayOrdersIds)

                viewModel.onTheWayOrdersIds = emptyList()
            }
        )
    }

    override fun onCurrentLocationResultFailure(context: Context?, exception: Exception?) {
        executeShowingErrorOnce(
            false,
            getString(com.maproductions.mohamedalaa.shared.R.string.there_is_an_error_in_detecting_your_location)
        ) {
            locationHandler.requestCurrentLocation(true)
        }
    }

    override fun onDestroyView() {
        channelEvent.unsubscribe()

        super.onDestroyView()
    }

}
