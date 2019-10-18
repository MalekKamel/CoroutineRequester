package com.coroutinerequester.app.presentation.rxrequester

import com.coroutinerequester.app.R
import com.sha.coroutinerequester.exception.handler.http.HttpExceptionHandler
import com.sha.coroutinerequester.exception.handler.http.HttpExceptionInfo


class ServerErrorHandler : HttpExceptionHandler() {

    override fun supportedErrors(): List<Int> {
        return listOf(500)
    }

    override fun handle(info: HttpExceptionInfo) {
        info.presentable.showError(R.string.oops_something_went_wrong)
    }

}