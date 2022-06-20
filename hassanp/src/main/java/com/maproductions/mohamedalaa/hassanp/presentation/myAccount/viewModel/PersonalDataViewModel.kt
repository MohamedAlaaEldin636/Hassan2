package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.material.datepicker.MaterialDatePicker
import com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel.RegisterFormViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.PersonalDataFragment
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.MAImage
import com.maproductions.mohamedalaa.shared.core.customTypes.RetryAbleFlow
import com.maproductions.mohamedalaa.shared.core.customTypes.map
import com.maproductions.mohamedalaa.shared.core.extensions.minLengthOrPrefixZeros
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class PersonalDataViewModel @Inject constructor(
    application: Application,
    private val repoAuth: RepoAuth,
) : AndroidViewModel(application) {

    val retryAbleFlow = RetryAbleFlow(repoAuth::getProviderProfile)

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
        // todo w mtnsash on success navUp + toast sent successfully isa.
    }

}