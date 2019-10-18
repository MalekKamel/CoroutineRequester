package com.sha.coroutinerequester.exception.handler.http

import com.sha.coroutinerequester.Presentable
import com.sha.coroutinerequester.CoroutineRequester


data class HttpExceptionInfo (
        val throwable: Throwable,
        val presentable: Presentable,
        val retryRequest: suspend () -> Unit,
        val requester: CoroutineRequester,
        val errorBody: String,
        val code: Int
)
