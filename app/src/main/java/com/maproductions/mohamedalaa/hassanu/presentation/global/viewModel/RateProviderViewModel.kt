package com.maproductions.mohamedalaa.hassanu.presentation.global.viewModel

import android.app.Application
import android.view.View
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.maproductions.mohamedalaa.hassanu.presentation.global.RateProviderDialogFragment
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.hassanu.presentation.global.RateProviderDialogFragmentArgs
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.orders.repository.RepoOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RateProviderViewModel @Inject constructor(
    application: Application,
    private val args: RateProviderDialogFragmentArgs,
    private val repoOrder: RepoOrder
) : AndroidViewModel(application) {

    val text by lazy {
        myApp.getString(R.string.done_service_please_rate_provider_instruction, args.categoryName)
    }

    val comment = MutableLiveData("")

    fun back(view: View) {
        view.findNavControllerOfProject().navigateUp()
    }

    fun rateProvider(ratingBar: AppCompatRatingBar) {
        val rate = (ratingBar.progress.toFloat() / ratingBar.max.toFloat() * 5)
            .roundHalfUpToIntOrFloat(1).toFloat()

        val fragment = ratingBar.findFragment<RateProviderDialogFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoOrder.rateProvider(args.providerId, rate, comment.value)
            },
            afterHidingLoading = {
                fragment.context?.showSuccessToast(fragment.getString(R.string.rate_done_successfully))

                fragment.findNavControllerOfProject().navigateUp()
            }
        )
    }

}