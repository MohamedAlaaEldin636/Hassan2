@file:OptIn(ExperimentalCoroutinesApi::class)

package com.maproductions.mohamedalaa.hassanu.presentation.settings.viewModel

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.fragment.app.findFragment
import androidx.lifecycle.*
import com.maproductions.mohamedalaa.shared.R as SR
import com.maproductions.mohamedalaa.hassanu.presentation.settings.PersonalDataFragment
import com.maproductions.mohamedalaa.shared.core.customTypes.map
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalDataViewModel @Inject constructor(
    application: Application,
    private val prefsAccount: PrefsAccount,
    private val repoAuth: RepoAuth
) : AndroidViewModel(application) {

    private val userData = prefsAccount.getUserData().asLiveData()

    val name = userData.map { it?.name }

    val email = userData.map { it?.email }

    val phone = userData.map { it?.phone }

    val imageUrl = userData.map { it?.imageUrl }

    var imageUri: Uri? = null

    fun pickImage(view: View) {
        view.findFragment<PersonalDataFragment>().pickImageOrRequestPermissions()
    }

    fun save(view: View) {
        // Email is optional
        if (name.value.isNullOrEmpty() || phone.value.isNullOrEmpty() ||
            (imageUrl.value.isNullOrEmpty() && imageUri == null)) {
            return view.context.showErrorToast(view.context.getString(SR.string.all_fields_required_except_email))
        }

        if (!phone.value.isValidIraqPhoneWithoutPrefix964()) {
            return view.context.showErrorToast(view.context.getString(SR.string.phone_number_is_wrong))
        }

        val fragment = view.findFragment<PersonalDataFragment>()
        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoAuth.updateUserProfile(
                    imageUri?.createMultipartBodyPart(myApp, ApiConst.Query.IMAGE),
                    name.value.orEmpty(),
                    if (userData.value?.email == email.value) null else email.value,
                    if (phone.value.orEmpty() == userData.value?.phone) null else {
                        phone.value.orEmpty()
                    },
                )
            },
            afterHidingLoading = { response ->
                if (response != null) {
                    viewModelScope.launch {
                        userData.value?.also { userData ->
                            prefsAccount.setUserData(userData.copy(
                                name = response.name.orEmpty(),
                                email = response.email.orEmpty(),
                                phone = response.phone.orEmpty(),
                                imageUrl = response.imageUrl.orEmpty(),
                            ))
                        }

                        fragment.context?.apply {
                            showSuccessToast(getString(SR.string.edited_successfully))
                        }

                        fragment.findNavControllerOfProject().navigateUp()
                    }
                }
            }
        )
    }

}
