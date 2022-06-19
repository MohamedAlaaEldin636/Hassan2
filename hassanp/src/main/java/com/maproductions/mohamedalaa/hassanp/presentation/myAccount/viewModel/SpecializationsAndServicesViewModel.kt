package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.SpecializationsAndServicesFragmentDirections
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.adapter.RVItemSmallText
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SpecializationsAndServicesViewModel @Inject constructor(
    application: Application,
    prefsAccount: PrefsAccount,
) : AndroidViewModel(application) {

    val provider = prefsAccount.getProviderData()

    val specialization = provider.map { it?.category.orEmpty() }.asLiveData()

    val services = provider.map { it?.services.orEmpty() }.asLiveData()

    val adapter = RVItemSmallText()

    fun send(view: View) {
        view.findNavController().navigate(
            SpecializationsAndServicesFragmentDirections
                .actionDestSpecializationsAndServicesToDestEditSpecializationsAndServices()
        )
    }

}
