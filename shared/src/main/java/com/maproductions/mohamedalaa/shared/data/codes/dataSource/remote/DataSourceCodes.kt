package com.maproductions.mohamedalaa.shared.data.codes.dataSource.remote

import com.maproductions.mohamedalaa.shared.core.di.module.OkHttpModule
import com.maproductions.mohamedalaa.shared.data.api.ApiConst
import com.maproductions.mohamedalaa.shared.data.remote.BaseRemoteDataSource
import com.maproductions.mohamedalaa.shared.data.settings.dataSource.remote.ApiSettingsServices
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class DataSourceCodes @Inject constructor(
    private val apiService: ApiCodesServices,
) : BaseRemoteDataSource() {

    suspend fun replacePoints() = safeApiCall {
        apiService.replacePoints(getAuthorizationHeader())
    }

    suspend fun getPromoCodes(page: Int) = safeApiCall {
        apiService.getPromoCodes(page, getAuthorizationHeader())
    }

}
