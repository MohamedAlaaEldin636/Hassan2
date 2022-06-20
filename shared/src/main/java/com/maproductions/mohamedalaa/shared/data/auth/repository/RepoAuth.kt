package com.maproductions.mohamedalaa.shared.data.auth.repository

import com.maproductions.mohamedalaa.shared.core.customTypes.LocationData
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.maproductions.mohamedalaa.shared.data.auth.dataSource.remote.DataSourceAuth
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.domain.auth.ResponseProviderProfile
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

class RepoAuth @Inject constructor(
    private val dataSource: DataSourceAuth,
    private val prefsAccount: PrefsAccount,
) {

    fun getProviderProfile() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponseProviderProfile>> {
        emit(dataSource.getProviderProfile())
    }

    suspend fun startReceiveOrders() = dataSource.toggleCanReceiveOrders(true, null)

    /** @param hours `null` if until I turn it back on isa. */
    suspend fun stopReceiveOrders(hours: Int?) = dataSource.toggleCanReceiveOrders(false, hours)

    suspend fun updateCategoryAndServices(categoryId: Int, servicesIds: List<Int>) =
        dataSource.updateCategoryAndServices(categoryId, servicesIds)

    suspend fun socialLogin(socialId: String, phone: String) = dataSource.socialLogin(phone, socialId)

    suspend fun checkSocialLogin(socialId: String) = dataSource.checkSocialLogin(socialId)

    suspend fun forgetPassword(phone: String, password: String) = dataSource.forgetPassword(phone, password)

    suspend fun resendCode(phone: String, isUserNotProvider: Boolean) = dataSource.resendCode(phone, isUserNotProvider)

    suspend fun login(phone: String) = dataSource.login(phone)

    suspend fun loginProvider(phone: String, password: String) = dataSource.loginProvider(phone, password)

    suspend fun verifyCode(phone: String, code: String, isUserNotProvider: Boolean) =
        dataSource.verifyCode(phone, code, isUserNotProvider)

    suspend fun updateUserProfile(name: String, email: String?) = dataSource.updateUserProfile(name, email)

    suspend fun updateUserProfile(locationData: LocationData) = prefsAccount.getUserData().first()!!.let {
        dataSource.updateUserProfile(
            it.name, null,
            locationData.latitude, locationData.longitude, locationData.address
        )
    }

    suspend fun updateProviderProfile(locationData: LocationData) = dataSource.updateProviderProfile(
        locationData.latitude, locationData.longitude, locationData.address
    )

    suspend fun updateUserProfile(
        image: MultipartBody.Part?,
        name: String,
        email: String?,
        phone: String,
    ) = dataSource.updateUserProfile(image, name, email, phone)

    /*fun getOnBoardScreenFor(isUserNotProvider: Boolean) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<List<ItemOnBoard>>> {
        emit(
            if (isUserNotProvider) {
                dataSource.getOnBoardScreenForUser()
            }else {
                dataSource.getOnBoardScreenForProvider()
            }
        )
    }*/

    suspend fun registerProvider(
        image: MultipartBody.Part,
        name: String,
        phone: String,
        address: String,
        relativePhone: String,
        categoryId: Int,
        servicesIds: List<Int>,
        imageFrontId: MultipartBody.Part,
        imageBackId: MultipartBody.Part,
        birthDay: Int,
        birthMonth: Int,
        birthYear: Int,
        password: String,
        passwordConfirmation: String,
        introVideo: MultipartBody.Part?,
        imageFrontAddress: MultipartBody.Part?,
        imageBackAddress: MultipartBody.Part?,
    ) = dataSource.registerProvider(
        image,
        name,
        phone,
        address,
        relativePhone,
        categoryId,
        servicesIds,
        imageFrontId,
        imageBackId,
        birthDay,
        birthMonth,
        birthYear,
        password,
        passwordConfirmation,
        introVideo,
        imageFrontAddress,
        imageBackAddress,
    )

}
