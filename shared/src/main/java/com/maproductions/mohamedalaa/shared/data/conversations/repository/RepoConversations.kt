package com.maproductions.mohamedalaa.shared.data.conversations.repository

import com.maproductions.mohamedalaa.shared.core.customTypes.BasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.BasePagingForChat
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.customTypes.mapImmediate
import com.maproductions.mohamedalaa.shared.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.maproductions.mohamedalaa.shared.data.auth.dataSource.remote.DataSourceAuth
import com.maproductions.mohamedalaa.shared.data.conversations.dataSource.remote.DataSourceConversations
import com.maproductions.mohamedalaa.shared.data.local.preferences.PrefsAccount
import com.maproductions.mohamedalaa.shared.presentation.conversations.ResponseChatDetails
import okhttp3.MultipartBody
import javax.inject.Inject

class RepoConversations @Inject constructor(
    private val dataSource: DataSourceConversations,
    private val prefsAccount: PrefsAccount,
) {

    fun getConversationsList() = BasePaging.createFlowViaPager {
        dataSource.getConversationsList(it)
    }

    fun getChatMessagesRelatedData(
        otherAccountId: Int,
        orderId: Int
    ) = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponseChatDetails>> {
        emit(dataSource.getChatMessages(otherAccountId, orderId, 1))
    }

    fun getChatMessages(otherAccountId: Int, orderId: Int) = BasePagingForChat.createFlowViaPager {
        dataSource.getChatMessages(otherAccountId, orderId, it).mapImmediate { response ->
            MABaseResponse(response.data?.messages, response.message, response.code)
        }
    }

    suspend fun sendChatMessage(otherAccountId: Int, orderId: Int, msg: String?, image: MultipartBody.Part?) =
        dataSource.sendChatMessage(otherAccountId, orderId, msg, image)

}