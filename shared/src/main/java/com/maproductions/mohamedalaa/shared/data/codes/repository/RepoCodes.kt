package com.maproductions.mohamedalaa.shared.data.codes.repository

import com.maproductions.mohamedalaa.shared.core.customTypes.BasePaging
import com.maproductions.mohamedalaa.shared.core.customTypes.IdAndName
import com.maproductions.mohamedalaa.shared.core.customTypes.MABaseResponse
import com.maproductions.mohamedalaa.shared.core.customTypes.mapImmediate
import com.maproductions.mohamedalaa.shared.core.extensions.flowInitialLoadingWithMinExecutionTime
import com.maproductions.mohamedalaa.shared.data.codes.dataSource.remote.DataSourceCodes
import com.maproductions.mohamedalaa.shared.data.settings.dataSource.remote.DataSourceSettings
import com.maproductions.mohamedalaa.shared.domain.settings.ItemOnBoard
import com.maproductions.mohamedalaa.shared.domain.settings.ResponseAddress
import com.maproductions.mohamedalaa.shared.domain.settings.ResponsePoints
import com.maproductions.mohamedalaa.shared.domain.wallet.ResponseWallet
import javax.inject.Inject

class RepoCodes @Inject constructor(
    private val dataSource: DataSourceCodes,
) {

    suspend fun replacePoints() = dataSource.replacePoints()

    fun getPromoCodes() = BasePaging.createFlowViaPager {
        dataSource.getPromoCodes(it)
    }

}