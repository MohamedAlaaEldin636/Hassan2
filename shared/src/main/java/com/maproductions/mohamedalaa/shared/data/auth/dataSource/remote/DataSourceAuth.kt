package com.maproductions.mohamedalaa.shared.data.auth.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.extensions.minLengthOrPrefixZeros
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.remote.BaseRemoteDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Field
import retrofit2.http.HeaderMap
import retrofit2.http.Part
import retrofit2.http.PartMap
import javax.inject.Inject

class DataSourceAuth @Inject constructor(
    private val apiService: ApiAuthServices,
) : BaseRemoteDataSource() {

    suspend fun toggleCanReceiveOrders(canReceive: Boolean, hours: Int?) = safeApiCall {
        val map = mutableMapOf<String, String>()
        if (!canReceive && hours != null) map[ApiConst.Query.HOURS] = hours.toString()

        // 0 means can get orders, so only 1 means get hours isa.
        apiService.toggleCanReceiveOrders(
            if (canReceive) 0 else 1,
            map,
            getAuthorizationHeader()
        )
    }

    suspend fun updateCategoryAndServices(categoryId: Int, servicesIds: List<Int>) = safeApiCall {
        apiService.updateCategoryAndServices(categoryId, servicesIds, getAuthorizationHeader())
    }

    suspend fun forgetPassword(phone: String, password: String) = safeApiCall {
        apiService.forgetPassword(phone, password, password, getAuthorizationHeader())
    }

    suspend fun resendCode(phone: String, isUserNotProvider: Boolean) = safeApiCall {
        val accountType = if (isUserNotProvider) ApiConst.Query.USER else ApiConst.Query.PROVIDER

        apiService.resendCode(phone, ApiConst.Query.VERIFY, accountType)
    }

    suspend fun verifyCode(phone: String, code: String, isUserNotProvider: Boolean) = safeApiCall {
        val accountType = if (isUserNotProvider) ApiConst.Query.USER else ApiConst.Query.PROVIDER

        apiService.verifyCode(phone, code, ApiConst.Query.VERIFY, accountType)
    }

    suspend fun login(phone: String) = safeApiCall {
        apiService.login(phone)
    }

    suspend fun loginProvider(phone: String, password: String) = safeApiCall {
        apiService.loginProvider(phone, password)
    }

    suspend fun socialLogin(socialId: String, phone: String) = safeApiCall {
        apiService.socialLogin(phone, socialId)
    }

    suspend fun checkSocialLogin(socialId: String) = safeApiCall {
        apiService.checkSocialLogin(socialId)
    }

    suspend fun updateUserProfile(
        name: String,
        email: String?
    ) = safeApiCall {
        val queryMap = mutableMapOf(
            ApiConst.Query.NAME to name,
            ApiConst.Query.ACCOUNT_TYPE to ApiConst.Query.USER,
        )
        if (!email.isNullOrEmpty()) {
            queryMap += ApiConst.Query.EMAIL to email
        }

        apiService.updateUserProfile(queryMap, getAuthorizationHeader())
    }

    suspend fun updateUserProfile(
        latitude: String,
        longitude: String,
        address: String,
    ) = safeApiCall {
        val queryMap = mutableMapOf(
            ApiConst.Query.LATITUDE to latitude,
            ApiConst.Query.LONGITUDE to longitude,
            ApiConst.Query.ADDRESS to address,
            ApiConst.Query.ACCOUNT_TYPE to ApiConst.Query.USER,
        )

        apiService.updateUserProfile(queryMap, getAuthorizationHeader())
    }

    suspend fun updateUserProfile(
        name: String,
        email: String?,
        latitude: String,
        longitude: String,
        address: String,
    ) = safeApiCall {
        val queryMap = mutableMapOf(
            ApiConst.Query.NAME to name,
            ApiConst.Query.LATITUDE to latitude,
            ApiConst.Query.LONGITUDE to longitude,
            ApiConst.Query.ADDRESS to address,
            ApiConst.Query.ACCOUNT_TYPE to ApiConst.Query.USER,
        )
        if (!email.isNullOrEmpty()) {
            queryMap += ApiConst.Query.EMAIL to email
        }

        apiService.updateUserProfile(queryMap, getAuthorizationHeader())
    }

    suspend fun updateProviderProfile(
        latitude: String,
        longitude: String,
        address: String,
    ) = safeApiCall {
        apiService.updateProviderProfile(latitude, longitude, address, getAuthorizationHeader())
    }

    suspend fun updateUserProfile(
        image: MultipartBody.Part?,
        name: String,
        email: String?,
        phone: String,
    ) = safeApiCall {
        val map = mutableMapOf(
            ApiConst.Query.NAME to name.toRequestBody(),
            ApiConst.Query.PHONE to phone.toRequestBody(),
            ApiConst.Query.ACCOUNT_TYPE to ApiConst.Query.USER.toRequestBody(),
        )
        if (!email.isNullOrEmpty()) {
            map[ApiConst.Query.EMAIL] = email.toRequestBody()
        }

        if (image != null) {
            apiService.updateUserProfile(
                image,
                map,
                getAuthorizationHeader()
            )
        }else {
            apiService.updateUserProfile2(
                map,
                getAuthorizationHeader()
            )
        }
    }

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
    ) = safeApiCall {
        val birthDate = birthYear.minLengthOrPrefixZeros(4) +
                "-${birthMonth.minLengthOrPrefixZeros(2)}" +
                "-${birthDay.minLengthOrPrefixZeros(2)}"

        val list = mutableListOf<MultipartBody.Part>()
        introVideo?.also { list += it }
        imageFrontAddress?.also { list += it }
        imageBackAddress?.also { list += it }

        apiService.registerProvider(
            image,
            name.toRequestBody(),
            phone.toRequestBody(),
            address.toRequestBody(),
            relativePhone.toRequestBody(),
            categoryId,
            servicesIds,
            imageFrontId,
            imageBackId,
            birthDate.toRequestBody(),
            password.toRequestBody(),
            passwordConfirmation.toRequestBody(),
            list
        )
    }

}
