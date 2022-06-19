package com.maproductions.mohamedalaa.shared.data.orders.repository

import com.maproductions.mohamedalaa.shared.core.customTypes.*
import com.maproductions.mohamedalaa.shared.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.maproductions.mohamedalaa.shared.data.home.dataSource.remote.DataSourceHome
import com.maproductions.mohamedalaa.shared.data.orders.dataSource.remote.DataSourceOrder
import com.maproductions.mohamedalaa.shared.domain.home.RequestServiceWithCount
import com.maproductions.mohamedalaa.shared.domain.home.ResponseHomeUser
import com.maproductions.mohamedalaa.shared.domain.orders.ItemCancellationReason
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseAdditionalServices
import com.maproductions.mohamedalaa.shared.domain.orders.ResponseOrderDetails
import javax.inject.Inject

class RepoOrder @Inject constructor(
    private val dataSource: DataSourceOrder,
) {

    fun getCancellationReasons() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<List<ItemCancellationReason>>> {
        emit(dataSource.getCancellationReasons())
    }

    fun getAdditionalServices(orderId: Int) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponseAdditionalServices>> {
        emit(dataSource.getAdditionalServices(orderId))
    }

    fun searchOrders(
        query: String,
    ) = BasePaging.createFlowViaPager {
        if (query.isNotEmpty()) {
            dataSource.searchOrders(query, null, null, it)
        }else {
            MAResult.Success(MABaseResponse(MABasePaging(emptyList()), "", 200))
        }
    }

    fun searchOrders(
        ordersCategory: OrdersCategory,
        query: String,
    ) = BasePaging.createFlowViaPager {
        if (query.isNotEmpty()) {
            dataSource.getOrders(ordersCategory, query, null, null, it)
        }else {
            MAResult.Success(MABaseResponse(MABasePaging(emptyList()), "", 200))
        }
    }

    fun getOrders(
        status: OrdersCategory,
        text: String? = null,
        categoryId: Int? = null,
        cityId: Int? = null,
    ) = BasePaging.createFlowViaPager {
        dataSource.getOrders(status, text, categoryId, cityId, it)
    }

    suspend fun cancelOrder(id: Int) = dataSource.updateStatusForOrder(id, ApiOrderStatus.CANCELLED)

    suspend fun cancelOrderWithReason(id: Int, reasonId: Int) = dataSource.cancelOrderWithReason(id, reasonId)

    suspend fun rejectOrder(id: Int) = dataSource.updateStatusForOrder(id, ApiOrderStatus.REJECTED)

    suspend fun acceptOrder(id: Int) = dataSource.updateStatusForOrder(id, ApiOrderStatus.ACCEPTED)

    suspend fun changeOrderStatus(id: Int, apiOrderStatus: ApiOrderStatus) = dataSource.updateStatusForOrder(id, apiOrderStatus)

    suspend fun finishOrderByProvider(
        id: Int,
        collectedMoney: Float? = null,
        additionalServices: List<RequestServiceWithCount>? = null
    ) = dataSource.updateStatusForOrder(id, ApiOrderStatus.FINISHED, collectedMoney, additionalServices)

    suspend fun finishOrderByUserByAccepting(
        id: Int,
    ) = dataSource.finishOrderForUser(id, null, 0)
    suspend fun finishOrderByUserByRejecting(
        id: Int,
        collectedMoney: Float,
    ) = dataSource.finishOrderForUser(id, collectedMoney, 1)

    fun getOrderDetails(id: Int) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponseOrderDetails>> {
        emit(dataSource.getOrderDetails(id))
    }

    suspend fun rateProvider(providerId: Int, rate: Float, review: String?) = dataSource.rateProvider(
        providerId, rate, review
    )

}