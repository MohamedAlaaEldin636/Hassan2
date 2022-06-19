package com.maproductions.mohamedalaa.hassanp.presentation.myAccount.viewModel

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel.RegisterFormViewModel
import com.maproductions.mohamedalaa.hassanp.presentation.myAccount.PersonalDataFragment
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.map
import com.maproductions.mohamedalaa.shared.core.extensions.minLengthOrPrefixZeros
import com.maproductions.mohamedalaa.shared.core.extensions.navigateDeepLinkWithOptions
import com.maproductions.mohamedalaa.shared.core.extensions.toggleValueIfNotNull
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.domain.settings.ImageWithTextAndTitleFlag
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class PersonalDataViewModel @Inject constructor(
    application: Application,
    prefsAccount: PrefsAccount,
) : AndroidViewModel(application) {

    private val providerData = prefsAccount.getProviderData().asLiveData()

    val imageUrl = providerData.map { it?.imageUrl }
    var imageUri: Uri? = null
    val name = providerData.map { it?.name }
    val phone = providerData.map { it?.phone }
    val birthDate = providerData.map {
        it?.birthDate?.split("-")?.joinToString(" / ")
    }

    // todo de kda el files elle na2sa tb w el hagat el tanya la2 de 8alat

    val address = MutableLiveData("")

    val relativePhone = MutableLiveData("")

    val showCategories = MutableLiveData(true)

    val showServices = MutableLiveData(true)

    fun toggleCategories() = showCategories.toggleValueIfNotNull()

    fun toggleServices() = showServices.toggleValueIfNotNull()

    fun pickImage(view: View) {
        view.findFragment<PersonalDataFragment>().pickImageOrRequestPermissions()
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
        // todo
    }

}