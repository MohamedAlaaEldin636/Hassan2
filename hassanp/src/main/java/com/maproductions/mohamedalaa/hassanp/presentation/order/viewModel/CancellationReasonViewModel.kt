package com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.paging.map
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.core.changeOrderNotOnTheWay
import com.maproductions.mohamedalaa.hassanp.presentation.order.CancellationReasonDialogFragment
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanp.presentation.order.CancellationReasonDialogFragmentArgs
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import com.maproductions.mohamedalaa.shared.core.extensions.toggleValueIfNotNull
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemTextWithRadioFixed
import com.maproductions.mohamedalaa.shared.presentation.order.CancelOrderDialogFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class CancellationReasonViewModel @Inject constructor(
    application: Application,
    private val repoOrder: RepoOrder,
    private val args: CancellationReasonDialogFragmentArgs
) : AndroidViewModel(application) {

    val text by lazy {
        myApp.getString(SR.string.you_will_pay_var_in_case_of_cancellation, args.cancellationFees.toString())
    }

    val showReasons = MutableLiveData(true)

    val retryAbleCities = RetryAbleFlow(repoOrder::getCancellationReasons)

    val adapter = RVItemTextWithRadioFixed()

    fun toggleReasonsVisibility() = showReasons.toggleValueIfNotNull()

    fun cancelOrder(view: View) {
        val reasonId = adapter.getSelectedItemOrNull()?.id ?: return

        val fragment = view.findFragment<CancellationReasonDialogFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoOrder.cancelOrderWithReason(args.orderId, reasonId)
            },
            afterHidingLoading = {
                val navController = fragment.findNavControllerOfProject()

                fragment.changeOrderNotOnTheWay(args.orderId)

                navController.navigateUp()
                navController.navigateUp()
            }
        )
    }

    fun goBack(view: View) {
        view.findNavControllerOfProject().navigateUp()
    }

}
