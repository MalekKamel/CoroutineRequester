package com.coroutinerequester.app.presentation.rxrequester

import com.coroutinerequester.app.R
import com.sha.coroutinerequester.exception.handler.throwable.ThrowableHandler
import com.sha.coroutinerequester.exception.handler.throwable.ThrowableInfo

class OutOfMemoryErrorHandler : ThrowableHandler<OutOfMemoryError>() {

    override fun supportedErrors(): List<Class<OutOfMemoryError>> {
        return listOf(OutOfMemoryError::class.java)
    }

    override fun handle(info: ThrowableInfo) {
        info.presentable.showError(R.string.no_memory_free_up_space)
    }
}
