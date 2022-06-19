package com.maproductions.mohamedalaa.shared.data.conversations.dataSource.remote

import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.auth.dataSource.remote.ApiAuthServices
import com.maproductions.mohamedalaa.shared.data.remote.BaseRemoteDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class DataSourceConversations @Inject constructor(
    private val apiService: ApiConversationsServices,
) : BaseRemoteDataSource() {

    suspend fun getConversationsList(page: Int) = safeApiCall {
        apiService.getConversationsList(page, getAuthorizationHeader())
    }

    suspend fun getChatMessages(otherAccountId: Int, orderId: Int, page: Int) = safeApiCall {
        apiService.getChatMessages(otherAccountId, orderId, page, getAuthorizationHeader())
    }

    suspend fun sendChatMessage(
        otherAccountId: Int,
        orderId: Int,
        message: String?,
        image: MultipartBody.Part?
    ) = safeApiCall {
        val map = mutableMapOf<String, @JvmSuppressWildcards RequestBody>()
        if (!message.isNullOrEmpty()) {
            map[ApiConst.Query.MESSAGE] = message.toRequestBody()
        }

        apiService.sendChatMessage(
            otherAccountId, orderId, map, listOfNotNull(image), getAuthorizationHeader()
        )
    }

}
