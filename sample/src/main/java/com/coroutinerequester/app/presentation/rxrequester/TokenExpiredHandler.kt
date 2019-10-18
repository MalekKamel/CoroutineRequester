package com.coroutinerequester.app.presentation.rxrequester

import com.sha.coroutinerequester.exception.handler.http.HttpExceptionHandler
import com.sha.coroutinerequester.exception.handler.http.HttpExceptionInfo

class TokenExpiredHandler : HttpExceptionHandler() {

    override fun supportedErrors(): List<Int> {
        return listOf(401)
    }

    override fun handle(info: HttpExceptionInfo) {
//        refresh token then call retryRequest method to run the request again
//        pseudo code:
//        info.requester.request { refreshTokenApi() }
//        info.retryRequest()
    }

}
