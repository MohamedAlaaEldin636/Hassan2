@file:OptIn(ExperimentalCoroutinesApi::class)

package com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.presentation.order.adapters.RVItemOrderCurrent
import com.maproductions.mohamedalaa.hassanu.presentation.order.adapters.RVItemOrderFinished
import com.maproductions.mohamedalaa.hassanu.presentation.order.adapters.RVItemOrderPending
import com.maproductions.mohamedalaa.shared.core.customTypes.OrdersCategory
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.domain.orders.OrdersFilter
import com.maproductions.mohamedalaa.shared.domain.orders.orEmpty
import com.maproductions.mohamedalaa.shared.domain.search.SearchType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    application: Application,
    repoOrder: RepoOrder,
    gson: Gson,
) : AndroidViewModel(application) {

    val currentCategory = MutableLiveData(OrdersCategory.PENDING)

    val filters = MutableLiveData(OrdersFilter())

    val adapterPending = RVItemOrderPending()
    val adapterCurrent = RVItemOrderCurrent(gson)
    val adapterFinished = RVItemOrderFinished(gson)

    val ordersPending = filters.asFlow().flatMapLatest {
        repoOrder.getOrdersForUser(
            OrdersCategory.PENDING,
            it?.search?.substringBefore(" "),
            it?.categoryId,
            it?.cityId,
        )
    }
    val ordersCurrent = filters.asFlow().flatMapLatest {
        repoOrder.getOrdersForUser(
            OrdersCategory.CURRENT,
            it?.search?.substringBefore(" "),
            it?.categoryId,
            it?.cityId,
        )
    }
    val ordersFinished = filters.asFlow().flatMapLatest {
        repoOrder.getOrdersForUser(
            OrdersCategory.FINISHED,
            it?.search?.substringBefore(" "),
            it?.categoryId,
            it?.cityId,
        )
    }

    fun toggleCategory(category: OrdersCategory) {
        if (currentCategory.value != category) {
            currentCategory.value = category
            /*val adapter = when (category) {
                OrdersCategory.PENDING -> adapterPending
                OrdersCategory.CURRENT -> adapterCurrent
                OrdersCategory.FINISHED -> adapterFinished
            }

            adapter.refresh()*/
        }
    }

    fun toSearchQueries(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.search.queries.with.orders.category",
            paths = arrayOf(SearchType.USER_ORDERS.name, currentCategory.value!!.name)
        )
    }

    fun filter(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.filter.orders"
        )
    }

    fun clearSearchFilter() = View.OnClickListener {
        changeSearchFilter("")
    }

    fun changeSearchFilter(search: String) {
        filters.value = filters.value.orEmpty().copy(search = search)
    }

    fun changeCategoryIdAndCityIdFilter(categoryId: Int, cityId: Int) {
        filters.value = filters.value.orEmpty().copy(categoryId = categoryId, cityId = cityId)
    }

}
