package com.maproductions.mohamedalaa.hassanu.presentation.service.viewModel

import android.view.View
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.core.extensions.defaultAnimationsNavOptionsBuilder
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PendingProviderServiceRequestViewModel @Inject constructor() : ViewModel() {

    fun backToMainPage(view: View) {
        view.findNavControllerOfProject().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.user.bottom.nav",
            defaultAnimationsNavOptionsBuilder()
                .setPopUpTo(R.id.dest_user_bottom_nav, true)
                .build()
        )
    }

}
