package com.maproductions.mohamedalaa.shared.data.home.repository

import com.maproductions.mohamedalaa.shared.core.customTypes.*
import com.maproductions.mohamedalaa.shared.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.maproductions.mohamedalaa.shared.data.auth.dataSource.remote.DataSourceAuth
import com.maproductions.mohamedalaa.shared.data.home.dataSource.remote.DataSourceHome
import com.maproductions.mohamedalaa.shared.domain.home.ResponseHomeUser
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategory
import com.maproductions.mohamedalaa.shared.domain.home.ServiceInCategoryWithCount
import com.maproductions.mohamedalaa.shared.domain.orders.ServiceInOrderDetails
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

    fun getServicesOfCategoryAllPages(id: Int) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<List<ServiceInCategory>>> {
        val list = mutableListOf<ServiceInCategory>()

        var page = 1

        while (true) {
            val maResult = dataSource.getServicesOfCategory(id, page)
            if (maResult is MAResult.Success) {
                list += maResult.value.data?.data.orEmpty()

                if (list.isEmpty() || maResult.value.data?.links?.nextPageUrl == null) {
                    emit(MAResult.Success(
                        MABaseResponse(
                            list,
                            "",
                            200
                        )
                    ))

                    break
                }else {
                    page++
                }
            }else {
                emit(maResult.mapImmediate {
                    MABaseResponse(
                        it.data?.data.orEmpty(),
                        it.message,
                        it.code
                    )
                })

                break
            }
        }
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