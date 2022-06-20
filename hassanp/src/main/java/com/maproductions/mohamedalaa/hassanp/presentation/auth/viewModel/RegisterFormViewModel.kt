@file:OptIn(ExperimentalCoroutinesApi::class)

package com.maproductions.mohamedalaa.hassanp.presentation.auth.viewModel

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.navigation.findNavController
import androidx.paging.map
import com.google.android.material.datepicker.MaterialDatePicker
import com.maproductions.mohamedalaa.hassanp.presentation.auth.RegisterFormFragment
import com.maproductions.mohamedalaa.shared.R
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.extensions.*
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.auth.repository.RepoAuth
import com.maproductions.mohamedalaa.shared.data.home.repository.RepoHome
import com.maproductions.mohamedalaa.shared.data.settings.repository.RepoSettings
import com.maproductions.mohamedalaa.shared.domain.settings.ImageWithTextAndTitleFlag
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemTextWithCheckBoxPaging
import com.maproductions.mohamedalaa.shared.presentation.adapters.RVItemTextWithRadioPaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class RegisterFormViewModel @Inject constructor(
    repoHome: RepoHome,
    private val repoAuth: RepoAuth,
    application: Application,
) : AndroidViewModel(application) {

    companion object {
        private const val DIALOG_DATE_PICKER_KEY = "RegisterFormViewModel.DIALOG_DATE_PICKER_KEY"
    }

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

    val agreeOnTermsAndConditions = MutableLiveData(false)

    val showPickProfession = MutableLiveData(true)

    val showServices = MutableLiveData(true)

    val name = MutableLiveData("")

    val phone = MutableLiveData("")

    val address = MutableLiveData("")

    val relativePhone = MutableLiveData("")

    val dateDay = MutableLiveData("")
    val dateMonth = MutableLiveData("")
    private var dateMonthNumber = 0
    val dateYear = MutableLiveData("")

    val password = MutableLiveData("")
    val confirmPassword = MutableLiveData("")

    var imageType = ImageType.PROFILE
        private set

    val imageProfile = MutableLiveData<Uri>()
    val imageFrontId = MutableLiveData<Uri>()
    val imageBackId = MutableLiveData<Uri>()
    val imageFrontAddress = MutableLiveData<Uri>()
    val imageBackAddress = MutableLiveData<Uri>()

    val video = MutableLiveData<Uri>()

    fun goBack(view: View) {
        view.findNavController().navigateUp()
    }

    fun pickDate(view: View) {
        // Makes only dates from today forward selectable.
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(view.context.getString(R.string.determine_the_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { millis ->
            val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())

            val day = localDateTime.dayOfMonth
            val month = view.context.resources
                .getStringArray(R.array.year_months)[localDateTime.monthValue.dec()]
            val year = localDateTime.year

            dateDay.value = day.toString()
            dateMonth.value = month
            dateMonthNumber = localDateTime.monthValue
            dateYear.value = year.toString()
        }

        datePicker.show(view.findFragment<Fragment>().childFragmentManager, DIALOG_DATE_PICKER_KEY)
    }

    fun toggleAgreeOnTermsAndConditions() = agreeOnTermsAndConditions.toggleValueIfNotNull()

    fun togglePickProfession() = showPickProfession.toggleValueIfNotNull()

    fun goToTermsAndConditions(view: View) {
        view.findNavController().navigateDeepLinkWithOptions(
            "fragment-dest",
            "com.grand.hassan.shared.image.with.text.and.title",
            paths = arrayOf(ImageWithTextAndTitleFlag.PROVIDER_TERMS_AND_CONDITIONS.name)
        )
    }

    fun toggleServices() = showServices.toggleValueIfNotNull()

    fun setImageUri(uri: Uri) {
        when (imageType) {
            ImageType.PROFILE -> imageProfile.value = uri
            ImageType.ID_FRONT -> imageFrontId.value = uri
            ImageType.ID_BACK -> imageBackId.value = uri
            ImageType.ADDRESS_FRONT -> imageFrontAddress.value = uri
            ImageType.ADDRESS_BACK -> imageBackAddress.value = uri
        }
    }

    fun pickImage(view: View, imageType: ImageType) {
        this.imageType = imageType

        view.findFragment<RegisterFormFragment>().pickImageOrRequestPermissions()
    }

    fun pickOrClearVideo(view: View) {
        view.findFragment<RegisterFormFragment>().pickVideoOrRequestPermissions()
    }

    fun next(view: View) {
        if (agreeOnTermsAndConditions.value != true) {
            return view.context.showErrorToast(view.context.getString(R.string.must_accept_terms_and_conditions))
        }

        if (imageProfile.value == null || name.value.isNullOrEmpty() || phone.value.isNullOrEmpty()
            || address.value.isNullOrEmpty() || relativePhone.value.isNullOrEmpty()
            || adapterServices.getSelectedItemsIds().isEmpty() || imageFrontId.value == null
            || imageBackId.value == null || dateDay.value.isNullOrEmpty()
            || dateMonth.value.isNullOrEmpty() || dateYear.value.isNullOrEmpty()
            || password.value.isNullOrEmpty() || confirmPassword.value.isNullOrEmpty()) {
            Timber.e("${imageProfile.value == null} || ${name.value.isNullOrEmpty()} || ${phone.value.isNullOrEmpty()}\n" +
                    "            || ${address.value.isNullOrEmpty()} || ${relativePhone.value.isNullOrEmpty()}\n" +
                    "            || ${adapterServices.getSelectedItemsIds().isNotEmpty()} || ${imageFrontId.value == null}\n" +
                    "            ||  || ${dateDay.value.isNullOrEmpty()}\n" +
                    "            || ${dateMonth.value.isNullOrEmpty()} || ${dateYear.value.isNullOrEmpty()}\n" +
                    "            || ${password.value.isNullOrEmpty()} || ${confirmPassword.value.isNullOrEmpty()}")

            return view.context.showErrorToast(view.context.getString(R.string.check_required_fields))
        }

        if (password.value != confirmPassword.value) {
            return view.context.showErrorToast(view.context.getString(R.string.password_not_same_as_confirm_password))
        }

        val fragment = view.findFragment<RegisterFormFragment>()

        fragment.executeOnGlobalLoadingAndAutoHandleRetryCancellable(
            afterShowingLoading = {
                repoAuth.registerProvider(
                    imageProfile.value?.createMultipartBodyPart(myApp, ApiConst.Query.IMAGE)!!,
                    name.valueNotNull,
                    phone.valueNotNull,
                    address.valueNotNull,
                    relativePhone.valueNotNull,
                    adapterCategories.getSelectedItemOrNull()?.id!!,
                    adapterServices.getSelectedItemsIds(),
                    imageFrontId.value?.createMultipartBodyPart(myApp, ApiConst.Query.IMAGE_FRONT_ID)!!,
                    imageFrontId.value?.createMultipartBodyPart(myApp, ApiConst.Query.IMAGE_BACK_ID)!!,
                    dateDay.valueNotNull.toInt(),
                    dateMonthNumber,
                    dateYear.valueNotNull.toInt(),
                    password.valueNotNull,
                    confirmPassword.valueNotNull,
                    video.value?.createMultipartBodyPart(myApp, ApiConst.Query.INTRO_VIDEO),
                    imageFrontAddress.value?.createMultipartBodyPart(myApp, ApiConst.Query.IMAGE_FRONT_ACCOMMODATION),
                    imageBackAddress.value?.createMultipartBodyPart(myApp, ApiConst.Query.IMAGE_BACK_ACCOMMODATION),
                )
            },
            afterHidingLoading = {
                fragment.findNavControllerOfProject().navigateDeepLinkWithOptions(
                    "fragment-dest",
                    "com.grand.hassan.shared.verify.phone",
                    paths = arrayOf(false.toString(), phone.valueNotNull)
                )
            }
        )
    }

    fun goToLogIn(view: View) {
        view.findNavController().navigateUp()
    }

    enum class ImageType {
        PROFILE, ID_FRONT, ID_BACK, ADDRESS_FRONT, ADDRESS_BACK;

        companion object {
            fun fromProviderFileType(value: String?): ImageType? {
                return when (value) {
                    "image_front_id" -> ID_FRONT
                    "image_back_id" -> ID_BACK
                    "image" -> PROFILE
                    else -> null
                }
            }
        }
    }

}
