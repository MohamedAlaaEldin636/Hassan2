package com.maproductions.mohamedalaa.shared.data.settings.repository

import com.maproductions.mohamedalaa.shared.core.customTypes.BasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.customTypes.mapImmediate
import com.maproductions.mohamedalaa.shared.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.maproductions.mohamedalaa.shared.data.settings.dataSource.remote.DataSourceSettings
import com.maproductions.mohamedalaa.shared.domain.settings.ItemOnBoard
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress
import com.maproductions.mohamedalaa.shared.domain.settings.ResponsePoints
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseReviews
import com.maproductions.mohamedalaa.shared.domain.wallet.ResponseWallet
import javax.inject.Inject

class RepoSettings @Inject constructor(
    private val dataSource: DataSourceSettings,
) {

    fun getMyReviewsFlow() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponseReviews>> {
        emit(dataSource.getMyReviews(1))
    }

    fun getMyReviewsPaging() = BasePaging.createFlowViaPager {
        dataSource.getMyReviews(it).mapImmediate { response ->
            MABaseResponse(response.data?.reviews, response.message, response.code)
        }
    }

    fun getWalletDetails() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponseWallet>> {
        emit(dataSource.getWalletDetails())
    }

    fun getWallet() = BasePaging.createFlowViaPager {
        dataSource.getWallet(it).mapImmediate { response ->
            MABaseResponse(response.data?.history, response.message, response.code)
        }
    }

    suspend fun sendTrackingCurrentLocation(
        orderId: Int,
        latitude: String,
        longitude: String,
    ) = dataSource.sendTrackingCurrentLocation(orderId, latitude, longitude)

    fun getOnBoardScreenFor(isUserNotProvider: Boolean) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<List<ItemOnBoard>>> {
        emit(
            if (isUserNotProvider) {
                dataSource.getOnBoardScreenForUser()
            }else {
                dataSource.getOnBoardScreenForProvider()
            }
        )
    }

    fun getTermsAndConditionsFor(isUserNotProvider: Boolean) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ItemOnBoard>> {
        emit(
            if (isUserNotProvider) {
                dataSource.getTermsAndConditionsForUser()
            }else {
                dataSource.getTermsAndConditionsForProvider()
            }
        )
    }

    fun getAboutAppFor(isUserNotProvider: Boolean) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ItemOnBoard>> {
        emit(
            if (isUserNotProvider) {
                dataSource.getAboutAppForUser()
            }else {
                dataSource.getAboutAppForProvider()
            }
        )
    }

    suspend fun contactUsOrSendComplainsOrSuggestions(
        name: String,
        email: String,
        phone: String,
        message: String,
        contactUsNotSendComplainsOrSuggestions: Boolean,
        orderId: Int?,
    ) = dataSource.contactUsOrSendComplainsOrSuggestions(name, email, phone, message, contactUsNotSendComplainsOrSuggestions, orderId)

    fun getPoints() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponsePoints>> {
        emit(dataSource.getPoints())
    }

    fun getUserAddresses() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<List<ResponseAddress>>> {
        emit(dataSource.getUserAddresses())
    }

    suspend fun deleteUserAddress(id: Int) = dataSource.deleteUserAddress(id)

    fun getCities() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<List<IdAndName>>> {
        emit(dataSource.getCities())
    }

    suspend fun getAreas(cityId: Int) = dataSource.getAreas(cityId)

    suspend fun addAddress(
        title: String,
        streetName: String,
        extraDescription: String,
        latitude: String,
        longitude: String,
        address: String,
        cityId: Int,
        areaId: Int,
    ) = dataSource.addAddress(title, streetName, extraDescription, latitude, longitude, address, cityId, areaId)

    fun getNotificationsCount() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<Int>> {
        emit(dataSource.getNotificationsCount())
    }

    fun getNotifications() = BasePaging.createFlowViaPager {
        dataSource.getNotifications(it)
    }

}
