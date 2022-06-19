package com.maproductions.mohamedalaa.shared.data.myAccount.dataSource.remote

import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.remote.BaseRemoteDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class DataSourceMyAccount @Inject constructor(
    private val apiService: ApiMyAccountServices,
) : BaseRemoteDataSource() {

    suspend fun getPreviousWorkData() = safeApiCall {
        apiService.getPreviousWorkData(getAuthorizationHeader())
    }

    suspend fun addOrUpdatePreviousWorkData(
        description: String?,
        images: List<MultipartBody.Part>,
        video: MultipartBody.Part?,
    ) = safeApiCall {
        val map = mutableMapOf<String, RequestBody>()
        if (!description.isNullOrEmpty()) {
            map[ApiConst.Query.DESCRIPTION] = description.toRequestBody()
        }

        when {
            images.isNotEmpty() && video != null -> {
                apiService.addOrUpdatePreviousWorkData(images, video, map, getAuthorizationHeader())
            }
            video != null -> {
                apiService.addOrUpdatePreviousWorkData(video, map, getAuthorizationHeader())
            }
            images.isNotEmpty() -> {
                apiService.addOrUpdatePreviousWorkData(images, map, getAuthorizationHeader())
            }
            else -> {
                apiService.addOrUpdatePreviousWorkData(map, getAuthorizationHeader())
            }
        }
    }

    suspend fun deleteImageOrVideoOfPreviousWorkData(id: Int) = safeApiCall {
        apiService.deleteImageOrVideoOfPreviousWorkData(id, getAuthorizationHeader())
    }

}