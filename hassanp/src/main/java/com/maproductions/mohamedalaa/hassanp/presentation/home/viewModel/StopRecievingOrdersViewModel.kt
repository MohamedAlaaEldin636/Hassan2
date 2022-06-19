package com.maproductions.mohamedalaa.hassanp.presentation.home.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maproductions.mohamedalaa.hassanp.presentation.home.StopRecievingOrdersDialogFragment
import com.maproductions.mohamedalaa.shared.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.orZero
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StopRecievingOrdersViewModel @Inject constructor(
    application: Application,
    private val repoAuth: RepoAuth,
) : AndroidViewModel(application) {

    /** `0` indicates full turn off until turn on manually */
    val selectionHour = MutableLiveData(1)

    fun toggleSelection(hour: Int) {
        selectionHour.value = hour
    }

    fun confirm(view: View) {
        val fragment = view.findFragment<StopRecievingOrdersDialogFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                val hours = selectionHour.value?.let {
                    if (it == 0) null else it
                }

                repoAuth.stopReceiveOrders(hours)
            },
            afterHidingLoading = {
                val navController = fragment.findNavControllerOfProject()

                navController.navigateUp()

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    StopRecievingOrdersDialogFragment.SAVED_STATE_HOURS,
                    selectionHour.value.orZero()
                )
            }
        )
    }

}