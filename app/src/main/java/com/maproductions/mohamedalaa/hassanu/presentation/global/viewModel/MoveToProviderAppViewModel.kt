package com.maproductions.mohamedalaa.hassanu.presentation.global.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.launchProviderAppOnGooglePlay
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoveToProviderAppViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    fun goBack(view: View) {
        view.findNavControllerOfProject().navigateUp()
    }

    fun move(view: View) {
        view.context.launchProviderAppOnGooglePlay()
    }

}
