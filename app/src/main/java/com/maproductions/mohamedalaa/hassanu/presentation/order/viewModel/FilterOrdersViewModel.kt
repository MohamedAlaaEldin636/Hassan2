@file:OptIn(ExperimentalCoroutinesApi::class)

package com.maproductions.mohamedalaa.hassanu.presentation.order.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.paging.map
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.presentation.order.FilterOrdersFragment
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.extensions.findNavControllerOfProject
import com.maproductions.mohamedalaa.shared.core.extensions.toJson
import com.maproductions.mohamedalaa.shared.data.home.repository.RepoHome
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemTextWithRadioFixed
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemTextWithRadioPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@HiltViewModel
class FilterOrdersViewModel @Inject constructor(
    application: Application,
    repoSettings: RepoSettings,
    repoHome: RepoHome,
    private val gson: Gson,
) : AndroidViewModel(application) {

    val showServicesTypes = MutableLiveData(true)

    val showServicesCities = MutableLiveData(true)

    val categories = repoHome.getCategories().mapLatest { pagingData ->
        pagingData.map {
            IdAndName(
                it.id,
                it.name
            )
        }
    }

    val retryAbleCities = RetryAbleFlow(repoSettings::getCities)

    val adapterCategories = RVItemTextWithRadioPaging()

    val adapterCities = RVItemTextWithRadioFixed()

    fun toggleServicesTypes() {
        showServicesTypes.value = showServicesTypes.value!!.not()
    }

    fun toggleServicesCities() {
        showServicesCities.value = showServicesCities.value!!.not()
    }

    fun search(view: View) {
        val categoryId = adapterCategories.getSelectedItemOrNull()?.id ?: return
        val cityId = adapterCities.getSelectedItemOrNull()?.id ?: return

        val navController = view.findNavControllerOfProject()

        navController.navigateUp()

        navController.currentBackStackEntry?.savedStateHandle?.set(
            FilterOrdersFragment.SAVED_STATE_CATEGORY_ID_AND_CITY_ID,
            (categoryId to cityId).toJson(gson)
        )
    }

}
