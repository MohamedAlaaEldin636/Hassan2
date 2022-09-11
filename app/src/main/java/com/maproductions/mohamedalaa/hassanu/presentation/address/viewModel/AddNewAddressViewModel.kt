package com.maproductions.mohamedalaa.hassanu.presentation.address.viewModel

import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.maproductions.mohamedalaa.hassanu.R
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.presentation.address.AddNewAddressFragment
import com.maproductions.mohamedalaa.hassanu.presentation.address.AddNewAddressFragmentArgs
import com.maproductions.mohamedalaa.hassanu.presentation.address.MyAddressesFragment
import com.maproductions.mohamedalaa.shared.core.customTypes.*
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress
import com.maproductions.mohamedalaa.shared.presentation.location.LocationSelectionFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddNewAddressViewModel @Inject constructor(
    args: AddNewAddressFragmentArgs,
    private val repoSettings: RepoSettings,
    gson: Gson,
) : ViewModel() {

    private val locationData = args.jsonOfLocationData.fromJson<LocationData>(gson)

    val addressName = MutableLiveData("")

    val listOfCities = MutableLiveData(emptyList<IdAndName>())
    private val selectedCityIndex = MutableLiveData(-1)
    val city = switchMapMultiple2(listOfCities, selectedCityIndex) {
        getSelectedCity()?.name.orEmpty()
    }

    private val listOfAreas = MutableLiveData(emptyList<IdAndName>())
    private val selectedAreaIndex = MutableLiveData(-1)
    val area = switchMapMultiple2(listOfAreas, selectedAreaIndex) {
        getSelectedArea()?.name.orEmpty()
    }

    val streetName = MutableLiveData("")

    val extraDescription = MutableLiveData("")

    fun showGovernoratesSelection(view: View) {
        val action: (List<IdAndName>) -> Unit = { list ->
            view.showPopup(list.map { it.name }) { menuItem ->
                val title = menuItem.title?.toString().orEmpty()
                if (title == city.value) {
                    return@showPopup
                }
                listOfCities.value = list
                selectedCityIndex.value = listOfCities.value.orEmpty().indexOfFirst { it.name == title }
                loadAreas(view.findFragment())
            }
        }

        if (!listOfCities.value.isNullOrEmpty()) {
            action(listOfCities.value.orEmpty())
        }else {
            val fragment = view.findFragment<AddNewAddressFragment>()

            fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
                afterShowingLoading = {
                    repoSettings.getCitiesSuspend()
                },
                afterHidingLoading = {
                    action(it.orEmpty())
                }
            )
        }
    }

    fun showCitiesSelection(view: View) {
        view.showPopup(listOfAreas.value.orEmpty().map { it.name }) { menuItem ->
            val title = menuItem.title?.toString().orEmpty()
            selectedAreaIndex.value = listOfAreas.value.orEmpty().indexOfFirst { it.name == title }
        }
    }

    fun addAddress(view: View) {
        if (addressName.value.isNullOrEmpty()) {
            return view.context.showErrorToast(view.context.getString(SR.string.address_required))
        }

        val fragment = view.findFragment<AddNewAddressFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoSettings.addAddress(
                    addressName.value.orEmpty(),
                    streetName.value.orEmpty(),
                    extraDescription.value.orEmpty(),
                    locationData.latitude,
                    locationData.longitude,
                    locationData.address,
                    getSelectedCity()?.id,
                    getSelectedArea()?.id,
                )
            },
            afterHidingLoading = {
                val navController = fragment.findNavController()

                navController.navigateUp()

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    AddNewAddressFragment.KEY_FRAGMENT_ADDED_ADDRESS_SUCCESSFULLY,
                    true
                )
            }
        )
    }

    /*fun loadListOfGovernorates(fragment: AddNewAddressFragment) {
        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable2(
            canCancelDialog = false,
            afterShowingLoading = {
                repoSettings.getCities()
            },
            afterHidingLoading = {
                listOfAreas.value = it.data
            }
        )
    }*/

    private fun loadAreas(fragment: AddNewAddressFragment) {
        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable2(
            canCancelDialog = false,
            afterShowingLoading = {
                repoSettings.getAreas(getSelectedCity()?.id.orZero())
            },
            afterHidingLoading = {
                listOfAreas.value = it.data

                showCitiesSelection(
                    fragment.binding?.cityTextInputLayout
                        ?: return@executeOnGlobalLoadingAndAutoHandleRetryCancellable2
                )
            }
        )
    }

    private fun getSelectedCity() = listOfCities.value.orEmpty()
        .getOrNull(selectedCityIndex.value.orZero())

    private fun getSelectedArea() = listOfAreas.value.orEmpty()
        .getOrNull(selectedAreaIndex.value.orZero())

}
