package com.maproductions.mohamedalaa.hassanu.presentation.order

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
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.hassanu.databinding.FragmentOrdersBinding
import com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel.OrdersViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.ApiOrderStatus
import com.maproductions.mohamedalaa.shared.core.customTypes.PusherUtils
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.domain.pusher.ResponsePusherOrder
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import com.maproductions.mohamedalaa.shared.presentation.order.CancelOrderDialogFragment
import com.maproductions.mohamedalaa.shared.presentation.search.SearchQueriesFragment
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
                runBlocking { prefsAccount.getUserData().first()!!.id }
            ),
            PusherUtils.EVENT_NAME_ON_CHANGE_ORDER_STATUS_FOR_ACCOUNT
        ) { data ->
            Timber.e("order status change for account ->\n$data")

            //val response = ResponsePusherOrder.fromJson(data, gson)//data.fromJson<>(gson)

            Handler(Looper.getMainLooper()).post {
                Timber.e("status is changed")

                viewModel.adapterPending.refresh()
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

        binding?.pendingRecyclerView?.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL, false
        )
        binding?.pendingRecyclerView?.adapter = viewModel.adapterPending.withDefaultHeaderAndFooterAdapters()

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
                    viewModel.ordersPending.collectLatest {
                        viewModel.adapterPending.submitData(it)
                    }
                }
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
            CancelOrderDialogFragment.SAVED_STATE_DONE_CANCEL_ORDER,
            false,
            viewLifecycleOwner,
            { it == true }
        ) {
            viewModel.adapterPending.refresh()
        }

        findNavControllerOfProject().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            FilterOrdersFragment.SAVED_STATE_CATEGORY_ID_AND_CITY_ID,
            "",
            viewLifecycleOwner,
            { !it.isNullOrEmpty() }
        ) {
            val (categoryId, cityId) = it.fromJson<Pair<Int, Int>>(gson)

            viewModel.changeCategoryIdAndCityIdFilter(categoryId, cityId)
        }

        findNavControllerOfProject().currentBackStackEntry?.savedStateHandle?.actOnGetIfNotInitialValueOrGetLiveData(
            SearchQueriesFragment.SAVED_STATE_TEXT,
            "",
            viewLifecycleOwner,
            { !it.isNullOrEmpty() }
        ) {
            viewModel.changeSearchFilter(it.orEmpty())
        }
    }

    override fun onDestroyView() {
        channelEvent.unsubscribe()

        super.onDestroyView()
    }

}
