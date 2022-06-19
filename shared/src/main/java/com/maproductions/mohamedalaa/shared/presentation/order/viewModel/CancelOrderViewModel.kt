package com.maproductions.mohamedalaa.shared.presentation.order.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import com.maproductions.mohamedalaa.shared.NavSharedArgs
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import com.maproductions.mohamedalaa.shared.presentation.order.CancelOrderDialogFragment
import com.maproductions.mohamedalaa.shared.presentation.order.CancelOrderDialogFragmentArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CancelOrderViewModel @Inject constructor(
    application: Application,
    private val args: CancelOrderDialogFragmentArgs,
    private val repoOrder: RepoOrder,
    //navArgs: NavSharedArgs,
) : AndroidViewModel(application) {

    val text by lazy {
        args.text
    }

    fun cancelOrder(view: View) {
        val fragment = view.findFragment<CancelOrderDialogFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoOrder.cancelOrder(args.id)
            },
            afterHidingLoading = {
                val navController = fragment.findNavControllerOfProject()

                navController.navigateUp()

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    CancelOrderDialogFragment.SAVED_STATE_DONE_CANCEL_ORDER,
                    true
                )
            }
        )
    }

    fun goBack(view: View) {
        view.findNavControllerOfProject().navigateUp()
    }

}
