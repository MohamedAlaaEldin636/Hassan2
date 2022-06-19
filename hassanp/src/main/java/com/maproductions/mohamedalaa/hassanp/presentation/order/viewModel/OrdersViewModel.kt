package com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.presentation.order.adapter.RVItemOrderCurrent
import com.maproductions.mohamedalaa.hassanp.presentation.order.adapter.RVItemOrderFinished
import com.maproductions.mohamedalaa.shared.core.customTypes.OrdersCategory
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.domain.orders.OrdersFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    application: Application,
    repoOrder: RepoOrder,
) : AndroidViewModel(application) {

    val currentCategoryIsCurrentNotFinished = MutableLiveData(true)

    val adapterCurrent = RVItemOrderCurrent()
    val adapterFinished = RVItemOrderFinished()

    val ordersCurrent = repoOrder.getOrders(OrdersCategory.CURRENT)
    val ordersFinished = repoOrder.getOrders(OrdersCategory.FINISHED)

    fun toggleCategory(isCurrentNotFinished: Boolean) {
        if (currentCategoryIsCurrentNotFinished.value != isCurrentNotFinished) {
            currentCategoryIsCurrentNotFinished.value = isCurrentNotFinished
        }
    }

}
