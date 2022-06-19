package com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel

import android.view.View
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maproductions.mohamedalaa.hassanu.presentation.order.RateProvider2DialogFragment
import com.maproductions.mohamedalaa.hassanu.presentation.order.RateProvider2DialogFragmentArgs
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.roundHalfUpToIntOrFloat
import com.maproductions.mohamedalaa.shared.core.extensions.showSuccessToast
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RateProvider2ViewModel @Inject constructor(
    private val args: RateProvider2DialogFragmentArgs,
    private val repoOrder: RepoOrder,
) : ViewModel() {

    val comment = MutableLiveData("")

    fun rate(ratingBar: AppCompatRatingBar) {
        val rate = (ratingBar.progress.toFloat() / ratingBar.max.toFloat() * 5)
            .roundHalfUpToIntOrFloat(1).toFloat()

        val fragment = ratingBar.findFragment<RateProvider2DialogFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoOrder.rateProvider(args.providerId, rate, comment.value)
            },
            afterHidingLoading = {
                fragment.context?.showSuccessToast(fragment.getString(R.string.rate_done_successfully))

                val navController = fragment.findNavControllerOfProject()

                navController.navigateUp()
                navController.navigateUp()
            }
        )
    }

}
