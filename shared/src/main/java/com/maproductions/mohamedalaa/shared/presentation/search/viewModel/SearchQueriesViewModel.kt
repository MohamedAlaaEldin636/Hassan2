@file:OptIn(ExperimentalCoroutinesApi::class)

package com.maproductions.mohamedalaa.shared.presentation.search.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.data.shared.repository.RepoShared
import com.maproductions.mohamedalaa.shared.domain.search.SearchType
import com.maproductions.mohamedalaa.shared.presentation.search.SearchQueriesFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.google.gson.Gson
import com.maproductions.mohamedalaa.shared.core.customTypes.OrdersCategory
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.domain.home.DeliveryData
import com.maproductions.mohamedalaa.shared.domain.home.SliderHomeCategory
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrder
import com.maproductions.mohamedalaa.shared.presentation.search.SearchQueriesFragment
import com.maproductions.mohamedalaa.shared.presentation.search.adapters.RVItemSearchQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@HiltViewModel
class SearchQueriesViewModel @Inject constructor(
    application: Application,
    val args: SearchQueriesFragmentArgs,
    repoShared: RepoShared,
    private val gson: Gson,
    repoOrder: RepoOrder,
    private val prefsAccount: PrefsAccount,
) : AndroidViewModel(application), RVItemSearchQuery.Listener {

    val hintText by lazy {
        when (args.searchType) {
            SearchType.USER_HOME -> R.string.search_for_services
            SearchType.USER_ORDERS -> R.string.search_by_order_number_provider_service
            SearchType.PROVIDER_HOME -> R.string.search_by_client_name_or_order_number
        }.let { myApp.getString(it) }
    }

    val search = MutableLiveData("")

    val adapter = RVItemSearchQuery(this, gson)

    val categories = search.asFlow().flatMapLatest {
        repoShared.searchCategories(it.orEmpty())
    }

    val suggestionsOfUserOrders = search.asFlow().flatMapLatest {
        repoOrder.searchOrdersForUser(it.orEmpty())
    }

    val suggestionsOfProviderHomeOrders = search.asFlow().flatMapLatest {
        repoOrder.searchOrdersForProvider(OrdersCategory.PENDING, it.orEmpty())
    }

    override fun onClick(view: View, jsonOfAny: String) {
        if (args.isGuest) {
            view.findNavControllerOfProject().navigateDeepLinkWithoutOptions(
                "fragment-dest",
                "com.grand.hassan.shared.guest.please.login.dialog"
            )

            return
        }

        when (args.searchType) {
            SearchType.USER_HOME -> {
                val category = jsonOfAny.fromJson<SliderHomeCategory>(gson)

                viewModelScope.launch {
                    prefsAccount.addSearchCategoriesSuggestions(category)

                    view.findNavControllerOfProject().navigateDeepLinkWithOptions(
                        "fragment-dest",
                        "com.grand.hassan.shared.services.selection",
                        paths = arrayOf(
                            category.name,
                            category.id.toString(),
                            DeliveryData(
                                category.deliveryFees,
                                category.orderMinPrice,
                                category.orderMinPriceForExtra
                            ).toJson(gson)
                        )
                    )
                }
            }
            SearchType.USER_ORDERS -> {
                val order = jsonOfAny.fromJson<ResponseOrder>(gson)

                viewModelScope.launch {
                    prefsAccount.addSearchUserOrdersSuggestions(order)

                    val navController = view.findNavControllerOfProject()

                    val provider = order.provider?.let { provider ->
                        " - ${provider.name} "
                    } ?: " "

                    val text = "${order.orderNumber}$provider- ${order.category}"

                    navController.navigateUp()

                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        SearchQueriesFragment.SAVED_STATE_TEXT,
                        text
                    )
                }
            }
            SearchType.PROVIDER_HOME -> {
                val order = jsonOfAny.fromJson<ResponseOrder>(gson)

                viewModelScope.launch {
                    prefsAccount.addSearchProviderOrdersSuggestions(order)

                    val navController = view.findNavControllerOfProject()

                    val text = "${order.user.name} - ${order.orderNumber}"

                    navController.navigateUp()

                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        SearchQueriesFragment.SAVED_STATE_TEXT,
                        text
                    )
                }
            }
        }
    }

}
