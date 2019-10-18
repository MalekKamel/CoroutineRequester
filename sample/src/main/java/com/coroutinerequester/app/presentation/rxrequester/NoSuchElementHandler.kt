package com.coroutinerequester.app.presentation.rxrequester

import com.coroutinerequester.app.R
import com.sha.coroutinerequester.exception.handler.throwable.ThrowableHandler
import com.sha.coroutinerequester.exception.handler.throwable.ThrowableInfo
import java.util.*

class NoSuchElementHandler : ThrowableHandler<NoSuchElementException>() {

    override fun supportedErrors(): List<Class<out NoSuchElementException>> {
        return listOf(NoSuchElementException::class.java)
    }

    override fun handle(info: ThrowableInfo) {
        info.presentable.showError(R.string.no_data_entered_yet)
    }
}
