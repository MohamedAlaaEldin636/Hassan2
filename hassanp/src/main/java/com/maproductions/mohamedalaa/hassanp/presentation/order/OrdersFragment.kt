package com.maproductions.mohamedalaa.hassanp.presentation.order

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.databinding.FragmentOrdersBinding
import com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel.OrdersViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.actOnGetIfNotInitialValueOrGetLiveData
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.withDefaultHeaderAndFooterAdapters
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.order.CancelOrderDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class OrdersFragment : MABaseFragment<FragmentOrdersBinding>() {

    private val viewModel by viewModels<OrdersViewModel>()

    @Inject
    lateinit var gson: Gson

    private val channelEvent by lazy {
        PusherUtils.getChannelEvent(
            PusherUtils.getChannelNameOnChangeOrderStatusForAccount(
                runBlocking { prefsAccount.getProviderData().first()!!.id }
            ),
            PusherUtils.EVENT_NAME_ON_CHANGE_ORDER_STATUS_FOR_ACCOUNT
        ) { data ->
            Timber.e("order status change for account ->\n$data")

            //val response = ResponsePusherOrder.fromJson(data, gson)//data.fromJson<>(gson)

            Handler(Looper.getMainLooper()).post {
                Timber.e("status is changed")

                viewModel.adapterCurrent.refresh()
                viewModel.adapterFinished.refresh()
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_orders

    override fun initializeBindingVariables() {
        binding?.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        channelEvent.subscribe()

        binding?.currentRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.currentRecyclerView?.adapter = viewModel.adapterCurrent.withDefaultHeaderAndFooterAdapters()

        binding?.finishedRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.finishedRecyclerView?.adapter = viewModel.adapterFinished.withDefaultHeaderAndFooterAdapters()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.ordersCurrent.collectLatest {
                        viewModel.adapterCurrent.submitData(it)
                    }
                }
                launch {
                    viewModel.ordersFinished.collectLatest {
                        viewModel.adapterFinished.submitData(it)
                    }
                }
            }
        }

        findNavControllerOfProject().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            OrderDetailsFragment.SAVED_STATE_ORDER_DETAILS_MADE_CHANGE,
            false,
            viewLifecycleOwner,
            { it == true }
        ) {
            viewModel.adapterCurrent.refresh()
            viewModel.adapterFinished.refresh()
        }
    }

    override fun onDestroyView() {
        channelEvent.unsubscribe()

        super.onDestroyView()
    }

}
