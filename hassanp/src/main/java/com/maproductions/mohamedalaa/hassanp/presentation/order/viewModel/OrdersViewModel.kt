package com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maproductions.mohamedalaa.hassanp.presentation.order.adapter.RVItemOrderCurrent
import com.maproductions.mohamedalaa.hassanp.presentation.order.adapter.RVItemOrderFinished
import com.maproductions.mohamedalaa.shared.core.customTypes.OrdersCategory
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    application: Application,
    repoOrder: RepoOrder,
) : AndroidViewModel(application) {

    val currentCategoryIsCurrentNotFinished = MutableLiveData(true)

    val adapterCurrent = RVItemOrderCurrent()
    val adapterFinished = RVItemOrderFinished()

    val ordersCurrent = repoOrder.getOrdersForProvider(OrdersCategory.CURRENT)
    val ordersFinished = repoOrder.getOrdersForProvider(OrdersCategory.FINISHED)

    fun toggleCategory(isCurrentNotFinished: Boolean) {
        if (currentCategoryIsCurrentNotFinished.value != isCurrentNotFinished) {
            currentCategoryIsCurrentNotFinished.value = isCurrentNotFinished
        }
    }

}
