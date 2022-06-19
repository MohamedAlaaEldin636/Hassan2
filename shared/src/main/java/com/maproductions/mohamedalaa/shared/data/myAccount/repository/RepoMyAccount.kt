package com.maproductions.mohamedalaa.shared.data.myAccount.repository

import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.maproductions.mohamedalaa.shared.data.myAccount.dataSource.remote.DataSourceMyAccount
import com.maproductions.mohamedalaa.shared.presentation.myAccount.ResponsePreviousWorkData
import okhttp3.MultipartBody
import javax.inject.Inject

class RepoMyAccount @Inject constructor(
    private val dataSource: DataSourceMyAccount,
) {

    fun getPreviousWorkData() = flowInitialLoadingWithMinExecutionTime<MABaseResponse<ResponsePreviousWorkData>> {
        emit(dataSource.getPreviousWorkData())
    }

    suspend fun addOrUpdatePreviousWorkData(
        description: String?,
        images: List<MultipartBody.Part>,
        video: MultipartBody.Part?,
    ) = dataSource.addOrUpdatePreviousWorkData(description, images, video)

    suspend fun deleteImageOrVideoOfPreviousWorkData(id: Int) =
        dataSource.deleteImageOrVideoOfPreviousWorkData(id)

}