package com.sha.coroutinerequester

import com.sha.coroutinerequester.exception.handler.throwable.ThrowableHandler
import com.sha.coroutinerequester.exception.handler.throwable.ThrowableInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
class OutOfMemoryErrorHandler : ThrowableHandler<OutOfMemoryError>() {

    override fun supportedErrors(): List<Class<OutOfMemoryError>> {
        return listOf(OutOfMemoryError::class.java)
    }

    override fun handle(info: ThrowableInfo) = runBlockingTest{
        info.presentable.showError("OutOfMemoryError")
        info.retryRequest()
    }
}
