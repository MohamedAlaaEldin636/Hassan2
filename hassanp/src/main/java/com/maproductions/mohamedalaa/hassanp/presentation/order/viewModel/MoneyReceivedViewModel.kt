package com.maproductions.mohamedalaa.hassanp.presentation.order.viewModel

import android.app.Application
import android.os.Looper
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanp.presentation.order.MoneyReceivedDialogFragment
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.hassanp.presentation.order.MoneyReceivedDialogFragmentArgs
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler
import javax.inject.Inject

@HiltViewModel
class MoneyReceivedViewModel @Inject constructor(
    application: Application,
    private val args: MoneyReceivedDialogFragmentArgs,
    private val repoOrder: RepoOrder,
    private val gson: Gson,
) : AndroidViewModel(application) {

    val requiredAmount by lazy {
        "${args.amountToPay} ${myApp.getString(R.string.egp)}"
    }

    val amount = MutableLiveData("")

    fun send(view: View) {
        if (amount.value.isNullOrEmpty() || amount.value.orEmpty().toFloatOrNull() == null) {
            return view.context.showErrorToast(view.context.getString(R.string.field_required))
        }

        if (amount.value.orEmpty().toFloat() < args.amountToPay) {
            return view.context.showErrorToast(view.context.getString(R.string.same_or_more_of_required_amount_must_be_received))
        }

        val fragment = view.findFragment<MoneyReceivedDialogFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoOrder.finishOrderByProvider(
                    args.orderId,
                    amount.value.orEmpty().toFloat(),
                    args.jsonListOfRequestServiceWithCount.fromJsonOrNull(gson)
                )
            },
            afterHidingLoading = {
                val navController = fragment.findNavControllerOfProject()
                viewModelScope.launch {
                    navController.navigateUp()
                    
                    navController.executeWhenLoadingEndsIfExists {
                        navController.navigateUp()
                    }
                }
            }
        )
    }

}