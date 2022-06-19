package com.maproductions.mohamedalaa.hassanp.presentation.home.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AcceptedOrderViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    fun done(view: View) {
        view.findNavControllerOfProject().navigateUp()
    }
}