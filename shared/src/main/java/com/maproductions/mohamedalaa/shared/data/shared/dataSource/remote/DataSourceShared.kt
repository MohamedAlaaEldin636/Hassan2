package com.maproductions.mohamedalaa.shared.data.shared.dataSource.remote

import com.maproductions.mohamedalaa.shared.data.home.dataSource.remote.ApiHomeServices
import com.maproductions.mohamedalaa.shared.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class DataSourceShared @Inject constructor(
    private val apiService: ApiSharedServices,
) : BaseRemoteDataSource() {

    suspend fun searchCategories(query: String, page: Int) = safeApiCall {
        apiService.searchCategories(query, page)
    }

}
