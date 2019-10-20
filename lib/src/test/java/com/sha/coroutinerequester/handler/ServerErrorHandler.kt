package com.sha.coroutinerequester.handler

import com.sha.coroutinerequester.exception.handler.http.HttpExceptionHandler
import com.sha.coroutinerequester.exception.handler.http.HttpExceptionInfo


class ServerErrorHandler : HttpExceptionHandler() {

    override fun supportedErrors(): List<Int> {
        return listOf(500)
    }

    override fun handle(info: HttpExceptionInfo) {
        info.presentable.showError("500 server error")
    }

}