package com.sha.coroutinerequester.exception.handler.throwable

import com.sha.coroutinerequester.Presentable
import com.sha.coroutinerequester.CoroutineRequester

data class ThrowableInfo(
        var throwable: Throwable,
        var presentable: Presentable,
        val retryRequest: suspend () -> Unit,
        val requester: CoroutineRequester
)