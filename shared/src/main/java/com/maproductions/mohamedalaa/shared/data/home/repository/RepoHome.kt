package com.maproductions.mohamedalaa.shared.data.home.repository

import com.maproductions.mohamedalaa.shared.core.customTypes.BasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.customTypes.MAResult
import com.maproductions.mohamedalaa.shared.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.maproductions.mohamedalaa.shared.data.auth.dataSource.remote.DataSourceAuth
import com.maproductions.mohamedalaa.shared.data.home.dataSource.remote.DataSourceHome
import com.maproductions.mohamedalaa.shared.domain.home.ResponseHomeUser
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import okhttp3.MultipartBody
import javax.inject.Inject

class RepoHome @Inject constructor(
    private val dataSource: DataSourceHome,
) {

    fun getUserHome() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponseHomeUser>> {
        emit(dataSource.getUserHome())
    }

    fun getCategories() = BasePaging.createFlowViaPager {
        dataSource.getCategories()
    }

    fun getServicesOfCategory(id: Int) = BasePaging.createFlowViaPager {
        dataSource.getServicesOfCategory(id, it)
    }

    suspend fun checkPromoCode(code: String) = dataSource.checkPromoCode(code)

    suspend fun createOrder(
        categoryId: Int,

        services: List<ServiceInCategoryWithCount>,

        addressId: Int,

        orderedAt: String,
        orderType: String,

        images: List<MultipartBody.Part>,
        extraNotes: String,

        total: Float,

        promoId: Int?,
    ): MAResult.Immediate<MABaseResponse<Int>> {
        return dataSource.createOrder(
            categoryId, services, addressId, orderedAt, orderType, images, extraNotes, total, promoId
        )
    }

}