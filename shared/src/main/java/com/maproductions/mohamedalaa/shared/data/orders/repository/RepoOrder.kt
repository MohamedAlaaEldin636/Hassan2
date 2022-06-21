package com.maproductions.mohamedalaa.shared.data.orders.repository

import androidx.paging.PagingData
import com.maproductions.mohamedalaa.shared.core.customTypes.*
import com.maproductions.mohamedalaa.shared.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.data.orders.dataSource.remote.DataSourceOrder
import com.maproductions.mohamedalaa.shared.domain.home.RequestServiceWithCount
import com.maproductions.mohamedalaa.shared.domain.orders.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class RepoOrder @Inject constructor(
    private val dataSource: DataSourceOrder,
    private val prefsAccount: PrefsAccount,
) {

    fun getCancellationReasons() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<List<ItemCancellationReason>>> {
        emit(dataSource.getCancellationReasons())
    }

    fun getAdditionalServices(orderId: Int) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponseAdditionalServices>> {
        emit(dataSource.getAdditionalServices(orderId))
    }

    fun searchOrdersForUser(
        query: String,
    ): Flow<PagingData<ResponseOrder>> = BasePaging.createFlowViaPager {
        if (query.isNotEmpty()) {
            dataSource.searchOrdersForUser(query, null, null, it).mapImmediate { response ->
                MABaseResponse(response.data?.orders, response.message, response.code)
            }
        }else {
            MAResult.Success(MABaseResponse(MABasePaging(
                prefsAccount.getSearchUserOrdersSuggestions().firstOrNull().orEmpty()
            ), "", 200))
        }
    }

    fun searchOrdersForProvider(
        ordersCategory: OrdersCategory,
        query: String,
    ): Flow<PagingData<ResponseOrder>> = BasePaging.createFlowViaPager {
        if (query.isNotEmpty()) {
            dataSource.getOrdersForProvider(ordersCategory, query, null, null, it).mapImmediate { response ->
                MABaseResponse(response.data?.orders, response.message, response.code)
            }
        }else {
            MAResult.Success(MABaseResponse(MABasePaging(
                prefsAccount.getSearchProviderOrdersSuggestions().firstOrNull().orEmpty()
            ), "", 200))
        }
    }

    fun getOrdersForProvider(
        status: OrdersCategory,
        text: String? = null,
        categoryId: Int? = null,
        cityId: Int? = null,
    ): Flow<PagingData<ResponseOrder>> = BasePaging.createFlowViaPager {
        dataSource.getOrdersForProvider(status, text, categoryId, cityId, it).mapImmediate { response ->
            MABaseResponse(response.data?.orders, response.message, response.code)
        }
    }

    fun getOrdersForUser(
        status: OrdersCategory,
        text: String? = null,
        categoryId: Int? = null,
        cityId: Int? = null,
    ): Flow<PagingData<ResponseOrder>> = BasePaging.createFlowViaPager {
        dataSource.getOrdersForUser(status, text, categoryId, cityId, it).mapImmediate { response ->
            MABaseResponse(response.data?.orders, response.message, response.code)
        }
    }

    fun getAllStatuses() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponseListOfOrders>> {
        emit(
            dataSource.getOrdersForProvider(
                OrdersCategory.PENDING, null, null, null, 1
            )
        )
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