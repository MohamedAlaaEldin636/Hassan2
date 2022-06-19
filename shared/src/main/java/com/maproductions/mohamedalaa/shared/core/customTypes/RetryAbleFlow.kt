package com.maproductions.mohamedalaa.shared.core.customTypes

import kotlinx.coroutines.flow.Flow

class RetryAbleFlow<T>(private val getFlow: () -> Flow<MAResult<MABaseResponse<T>>>) {

    var value: Flow<MAResult<MABaseResponse<T>>> = getFlow()
        private set

    fun retry() {
        value = getFlow()
    }

}

