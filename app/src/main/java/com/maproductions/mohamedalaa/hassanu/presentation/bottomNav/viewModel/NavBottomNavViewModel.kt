package com.maproductions.mohamedalaa.hassanu.presentation.bottomNav.viewModel

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.executeOnGlobalLoadingAndAutoHandleRetryCancellable
import com.maproductions.mohamedalaa.shared.core.extensions.handleRetryAbleFlowWithMustHaveResultWithNullability
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.home.repository.RepoHome
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import com.maproductions.mohamedalaa.shared.domain.home.ResponseHomeUser
import com.maproductions.mohamedalaa.shared.presentation.base.MABaseFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavBottomNavViewModel @Inject constructor(
    repoHome: RepoHome,
) : ViewModel() {

    private val retryAbleFlowHome = RetryAbleFlow(repoHome::getUserHome)
    private var responseHome: MABaseResponse<ResponseHomeUser>? = null

    fun <VDB : ViewDataBinding, F : MABaseFragment<VDB>> getHomeResponseHandleRetryAble(
        fragment: F,
        onSuccess: (MABaseResponse<ResponseHomeUser>) -> Unit
    ) {
        responseHome?.let {
            onSuccess(it)
        } ?: fragment.handleRetryAbleFlowWithMustHaveResultWithNullability(retryAbleFlowHome) {
            responseHome = it

            onSuccess(it)
        }
    }

}
