package com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServicesSelectionFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.service.ServicesSelectionFragmentDirections
import com.maproductions.mohamedalaa.hassanu.presentation.service.adapters.RVServiceInSelection
import com.maproductions.mohamedalaa.shared.core.extensions.fromJson
import com.maproductions.mohamedalaa.shared.core.extensions.roundHalfUpToIntOrFloat
import com.maproductions.mohamedalaa.shared.core.extensions.showErrorToast
import com.maproductions.mohamedalaa.shared.core.extensions.toJson
import com.maproductions.mohamedalaa.shared.data.home.repository.RepoHome
import com.maproductions.mohamedalaa.shared.domain.home.DeliveryData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ServicesSelectionViewModel @Inject constructor(
    repoHome: RepoHome,
    private val args: ServicesSelectionFragmentArgs,
    private val gson: Gson,
) : ViewModel() {

    val services = repoHome.getServicesOfCategory(args.categoryId)

    val adapter = RVServiceInSelection(gson)

    fun next(view: View) {
        val list = adapter.getSelectedServices()

        if (list.isEmpty()) {
            return view.context.showErrorToast(view.context.getString(SR.string.select_at_least_one_service))
        }

        val total = list.sumOf {
            it.serviceInCategory.price.toBigDecimal().multiply(
                it.count.toBigDecimal()
            )
        }.toFloat().roundHalfUpToIntOrFloat(1).toFloat()
        val deliveryData = args.jsonDeliveryData.fromJson<DeliveryData>(gson)
        if (total <= deliveryData.orderMinPrice) {
            return view.context.showErrorToast(
                view.context.getString(
                    SR.string.min_price_allowed_var,
                    deliveryData.orderMinPrice.toString()
                )
            )
        }

        view.findNavController().navigate(
            ServicesSelectionFragmentDirections.actionDestServicesSelectionToDestServicesLocationSelection(
                args.categoryId,
                args.categoryName,
                list.toJson(gson),
                args.jsonDeliveryData
            )
        )
    }


}
