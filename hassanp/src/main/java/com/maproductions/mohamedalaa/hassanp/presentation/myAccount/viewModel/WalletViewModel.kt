package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemWallet
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    application: Application,
    repoSettings: RepoSettings
) : AndroidViewModel(application) {

    val retryAbleFlow = RetryAbleFlow(repoSettings::getWalletDetails)

    val walletHistory = repoSettings.getWallet()

    val adapter = RVItemWallet()

    val totalValueOfWallet = MutableLiveData("")

    val info = MutableLiveData("")

}
