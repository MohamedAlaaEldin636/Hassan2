package com.maproductions.mohamedalaa.hassanp.presentation.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentHomeBinding
import com.maproductions.mohamedalaa.hassanp.presentation.home.viewModel.HomeViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.main.MainActivity
import com.maproductions.mohamedalaa.hassanp.presentation.order.OrderDetailsFragment
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.search.SearchQueriesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : MABaseFragment<FragmentHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

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
        super.onCreate(savedInstanceState)

        (activity as? MainActivity)?.subscribeToChannel()
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun initializeBindingVariables() {
        binding?.activityViewModel = activityViewModel
        binding?.viewModel = viewModel
    }

    // todo will be changed to have 2 more flags orders_flag and approved == 2 meaning u have to
    //  ffreeze account so see login view model kda isa,
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
            viewModel.stopReceivingOrders(it.orZero())
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
    }

    override fun onDestroyView() {
        channelEvent.unsubscribe()

        super.onDestroyView()
    }

}
