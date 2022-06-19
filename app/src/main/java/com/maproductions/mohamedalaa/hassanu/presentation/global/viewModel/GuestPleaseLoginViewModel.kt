package com.maproductions.mohamedalaa.hassanu.presentation.global.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.myApp
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithoutOptions
import com.maproductions.mohamedalaa.shared.core.extensions.popAllBackStacks
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GuestPleaseLoginViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    fun goToPerformLogin(view: View) {
        val navController = view.findNavControllerOfProject()

        navController.popAllBackStacks()

        navController.navigateDeepLinkWithoutOptions(
            "fragment-dest",
            "com.grand.hassan.shared.log.in",
        )
    }

    fun back(view: View) {
        view.findNavControllerOfProject().navigateUp()
    }

}
