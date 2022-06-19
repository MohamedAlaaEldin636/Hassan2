@file:OptIn(ExperimentalCoroutinesApi::class)

package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel

import android.app.Application
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.map
import com.maproductions.mohamedalaa.hassanp.R
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.EditSpecializationsAndServicesFragment
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.home.repository.RepoHome
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemTextWithCheckBoxPaging
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemTextWithRadioPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class EditSpecializationsAndServicesViewModel @Inject constructor(
    application: Application,
    repoHome: RepoHome,
    private val repoAuth: RepoAuth,
) : AndroidViewModel(application) {

    val showCategory = MutableLiveData(true)

    val showServices = MutableLiveData(true)

    val categories = repoHome.getCategories().mapLatest { pagingData ->
        pagingData.map {
            IdAndName(
                it.id,
                it.name
            )
        }
    }

    val selectedCategoryId = MutableLiveData<Int>()

    val services = selectedCategoryId.asFlow().flatMapLatest { id ->
        if (id == null) emptyFlow() else repoHome.getServicesOfCategory(id).mapLatest { pagingData ->
            pagingData.map {
                IdAndName(
                    it.id,
                    it.name.orEmpty()
                )
            }
        }
    }

    val adapterCategories = RVItemTextWithRadioPaging(object : RVItemTextWithRadioPaging.Listener {
        override fun onChangeSelection(id: Int) {
            selectedCategoryId.value = id
        }
    })

    val adapterServices = RVItemTextWithCheckBoxPaging()

    fun toggleCategory() = showCategory.toggleValueIfNotNull()

    fun toggleServices() = showServices.toggleValueIfNotNull()

    fun send(view: View) {
        val categoryId = adapterCategories.getSelectedItemOrNull()?.id
        val servicesIds = adapterServices.getSelectedItemsIds()

        if (categoryId == null || servicesIds.isEmpty()) {
            return view.context.showErrorToast(view.context.getString(com.maproductions.mohamedalaa.shared.R.string.all_fields_required))
        }

        val fragment = view.findFragment<EditSpecializationsAndServicesFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoAuth.updateCategoryAndServices(categoryId, servicesIds)
            },
            afterHidingLoading = {
                fragment.context?.showSuccessToast(fragment.getString(com.maproductions.mohamedalaa.shared.R.string.sent_done_successfully))

                val navController = fragment.findNavControllerOfProject()

                navController.navigateUp()
                navController.navigateUp()
            }
        )
    }

}