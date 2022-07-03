package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel.RegisterFormViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.PersonalDataFragment
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.customTypes.map
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.domain.auth.ResponseProviderProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class PersonalDataViewModel @Inject constructor(
    application: Application,
    private val repoAuth: RepoAuth,
    private val prefsAccount: PrefsAccount,
) : AndroidViewModel(application) {

    val retryAbleFlow = RetryAbleFlow(repoAuth::getProviderProfile)

    var response: ResponseProviderProfile? = null

    val imageProfile = MutableLiveData<MAImage>()
    val name = MutableLiveData("")
    val phone = MutableLiveData("")
    val birthDate = MutableLiveData("")

    val showIdsFields = MutableLiveData(false)

    val imageFrontId = MutableLiveData<MAImage>()
    val imageBackId = MutableLiveData<MAImage>()

    val requireProfile = MutableLiveData(false)
    val requireFrontId = MutableLiveData(false)
    val requireBackId = MutableLiveData(false)

    val errorName: MutableLiveData<Int?> = name.map { null }
    val errorPhone: MutableLiveData<Int?> = phone.map { null }
    val errorBirthDate: MutableLiveData<Int?> = birthDate.map { null }

    val profileCardColorRes = requireProfile.map {
        if (it == true) R.color.card_view_stroke_error else R.color.white
    }
    val frontIdBackgroundDrawableRes = requireFrontId.map {
        if (it == true) R.drawable.dr_rounded_white_small_with_red_border else R.drawable.dr_rounded_white_small
    }
    val backIdBackgroundDrawableRes = requireBackId.map {
        if (it == true) R.drawable.dr_rounded_white_small_with_red_border else R.drawable.dr_rounded_white_small
    }

    var imageType = RegisterFormViewModel.ImageType.ID_FRONT
        private set

    fun pickImage(view: View, imageType: RegisterFormViewModel.ImageType) {
        this.imageType = imageType

        view.findFragment<PersonalDataFragment>().pickImageOrRequestPermissions()
    }

    fun setImageUri(uri: Uri) {
        val maImage = MAImage.IUri(uri)

        when (imageType) {
            RegisterFormViewModel.ImageType.PROFILE -> imageProfile.value = maImage
            RegisterFormViewModel.ImageType.ID_FRONT -> imageFrontId.value = maImage
            RegisterFormViewModel.ImageType.ID_BACK -> imageBackId.value = maImage
            else -> {}
        }
    }

    fun pickDate(view: View) {
        // Makes only dates from today forward selectable.
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(view.context.getString(R.string.determine_the_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { millis ->
            val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())

            val day = localDateTime.dayOfMonth.minLengthOrPrefixZeros(2)
            val month = localDateTime.monthValue.minLengthOrPrefixZeros(2)
            val year = localDateTime.year

            birthDate.value = "$day / $month / $year"
        }

        datePicker.show(view.findFragment<Fragment>().childFragmentManager,
            PersonalDataFragment.DIALOG_DATE_PICKER_KEY
        )
    }

    fun send(view: View) {
        var errorMsgRes: Int? = null
        if (name.value.isNullOrEmpty()) {
            errorName.value = R.string.name_required

            errorMsgRes = if (errorMsgRes == null) R.string.name_required else R.string.all_fields_required
        }
        if (phone.value.isNullOrEmpty()) {
            errorPhone.value = R.string.phone_required

            errorMsgRes = if (errorMsgRes == null) R.string.phone_required else R.string.all_fields_required
        }
        if (birthDate.value.isNullOrEmpty()) {
            errorBirthDate.value = R.string.birth_date_required

            errorMsgRes = if (errorMsgRes == null) R.string.birth_date_required else R.string.all_fields_required
        }

        if (errorMsgRes != null) {
            return view.context.showErrorToast(view.context.getString(errorMsgRes))
        }

        val errorRes = when {
            //name.value.isNullOrEmpty() -> R.string.name_required
            //phone.value.isNullOrEmpty() -> R.string.phone_required
            //birthDate.value.isNullOrEmpty() -> R.string.birth_date_required
            requireProfile.value == true && imageProfile.value == null -> R.string.profile_image_required
            requireFrontId.value == true && imageFrontId.value == null -> R.string.front_id_image_required
            requireBackId.value == true && imageBackId.value == null -> R.string.back_id_image_required
            else -> null
        }

        if (errorRes != null) {
            return view.context.showErrorToast(view.context.getString(errorRes))
        }

        if (!phone.value.isValidIraqPhoneWithoutPrefix964()) {
            return view.context.showErrorToast(view.context.getString(R.string.phone_number_is_wrong))
        }

        val profile = imageProfile.value?.let {
            (it as? MAImage.IUri)?.uri?.createMultipartBodyPart(view.context, ApiConst.Query.IMAGE)
        }
        val frontId = if (requireFrontId.value != true) null else imageFrontId.value?.let {
            (it as? MAImage.IUri)?.uri?.createMultipartBodyPart(view.context, ApiConst.Query.IMAGE_FRONT_ID)
        }
        val backId = if (requireBackId.value != true) null else imageBackId.value?.let {
            (it as? MAImage.IUri)?.uri?.createMultipartBodyPart(view.context, ApiConst.Query.IMAGE_BACK_ID)
        }

        val (birthDay, birthMonth, birthYear) = birthDate.value?.split(" / ")?.let {
            if (it.size != 3) null else Triple(
                it[0].toIntOrNull() ?: return@let null,
                it[1].toIntOrNull() ?: return@let null,
                it[2].toIntOrNull() ?: return@let null,
            )
        } ?: return view.context.showErrorToast(view.context.getString(R.string.date_ensure))

        val fragment = view.findFragment<PersonalDataFragment>()

        val phone = if (phone.value.orEmpty() == response?.phone) null else {
            phone.value.orEmpty()
        }

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoAuth.updateProviderProfileByRequestingApp(
                    profile,
                    name.value.orEmpty(),
                    phone,
                    frontId,
                    backId,
                    birthDay,
                    birthMonth,
                    birthYear
                )
            },
            afterHidingLoading = {
                fragment.context?.showSuccessToast(fragment.getString(R.string.sent_done_successfully))

                viewModelScope.launch {
                    val provider = prefsAccount.getProviderData().first()!!

                    prefsAccount.setProviderData(
                        provider.copy(
                            phone = phone ?: provider.phone,
                            name = name.value.orEmpty(),
                            birthDate = "$birthYear-$birthMonth-$birthDay",
                        )
                    )

                    fragment.findNavControllerOfProject().navigateUp()
                }
            }
        )
    }

}