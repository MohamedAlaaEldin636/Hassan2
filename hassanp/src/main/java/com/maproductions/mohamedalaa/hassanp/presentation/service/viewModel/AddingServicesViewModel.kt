@file:OptIn(ExperimentalCoroutinesApi::class)

package com.maproductions.mohamedalaa.hassanp.presentation.service.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.navigation.findNavController
import androidx.paging.filter
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.presentation.service.AddingServicesFragmentArgs
import com.maproductions.mohamedalaa.hassanp.presentation.service.adapters.RVItemAddingServices
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.home.repository.RepoHome
import com.maproductions.mohamedalaa.shared.domain.home.DeliveryData
import com.maproductions.mohamedalaa.shared.domain.home.RequestServiceWithCount
import com.maproductions.mohamedalaa.shared.domain.orders.ServiceInOrderDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddingServicesViewModel @Inject constructor(
    application: Application,
    private val args: AddingServicesFragmentArgs,
    repoHome: RepoHome,
    private val gson: Gson,
) : AndroidViewModel(application) {

    val currentServices = emptyList<ServiceInOrderDetails>()/*args.jsonOfServicesInOrderDetails
        .fromJsonOrNull<List<ServiceInOrderDetails>>(gson).orEmpty()*/

    val retryAbleFlow = RetryAbleFlow {
        repoHome.getServicesOfCategoryAllPages(args.categoryId)
    }

    val search = MutableLiveData("")

    private val _services = repoHome.getServicesOfCategory(args.categoryId)

    val services = search.asFlow().flatMapLatest { query ->
        _services.mapLatest { pagingData ->
            pagingData.filter {
                query in it.name.orEmpty()
            }
        }
    }

    val adapter = RVItemAddingServices(gson)

    fun clearSearchFilter() = View.OnClickListener {
        search.value = ""
    }

    fun finishWork(view: View) {
        val list = adapter.getSelectedServices()

        if (list.isEmpty()) {
            return view.context.showErrorToast(view.context.getString(R.string.select_at_least_one_service))
        }

        val total = list.sumOf {
            it.serviceInCategory.price.toBigDecimal().multiply(
                it.count.toBigDecimal()
            )
        }.toFloat().roundHalfUpToIntOrFloat(1).toFloat()
        if (total <= args.orderMinPriceForExtra) {
            return view.context.showErrorToast(
                view.context.getString(
                    R.string.min_price_allowed_var,
                    args.orderMinPriceForExtra.toString()
                )
            )
        }

        val jsonListOfRequestServiceWithCount = list.map {
            RequestServiceWithCount(
                it.serviceInCategory.id,
                it.serviceInCategory.price,
                it.count
            )
        }.toJson(gson)

        val originalTotalWithoutAdditions = args.jsonOfServicesInOrderDetails
            .fromJsonOrNull<List<ServiceInOrderDetails>>(gson).orEmpty().map {
                it.count.toFloat() * it.price
            }.sum()

        view.findNavController().navigateDeepLinkWithoutOptions(
            "dialog-dest",
            "com.grand.hassan.shared.money.received.dialog.with.list.of.services",
            args.orderId.toString(),
            (originalTotalWithoutAdditions + total).toString(),
            jsonListOfRequestServiceWithCount
        )
    }

}
