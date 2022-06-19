package com.maproductions.mohamedalaa.shared.data.shared.repository

import com.maproductions.mohamedalaa.shared.core.customTypes.*
import com.maproductions.mohamedalaa.shared.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.maproductions.mohamedalaa.shared.data.home.dataSource.remote.DataSourceHome
import com.maproductions.mohamedalaa.shared.data.shared.dataSource.remote.DataSourceShared
import com.maproductions.mohamedalaa.shared.domain.home.ResponseHomeUser
import javax.inject.Inject

class RepoShared @Inject constructor(
    private val dataSource: DataSourceShared,
) {

    suspend fun searchCategories(query: String) = BasePaging.createFlowViaPager {
        if (query.isNotEmpty()) {
            dataSource.searchCategories(query, it)
        }else {
            MAResult.Success(MABaseResponse(MABasePaging(emptyList(), null), "", 200))
        }
    }

}
