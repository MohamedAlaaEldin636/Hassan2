package com.maproductions.mohamedalaa.shared.presentation.order.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemTextWithRadioFixed
import com.maproductions.mohamedalaa.shared.presentation.order.CancellationReasonDialogFragment
import com.maproductions.mohamedalaa.shared.presentation.order.CancellationReasonDialogFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CancellationReasonViewModel @Inject constructor(
    application: Application,
    private val repoOrder: RepoOrder,
    private val args: CancellationReasonDialogFragmentArgs
) : AndroidViewModel(application) {

    val text by lazy {
        myApp.getString(R.string.you_will_pay_var_in_case_of_cancellation, args.cancellationFees.toString())
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
