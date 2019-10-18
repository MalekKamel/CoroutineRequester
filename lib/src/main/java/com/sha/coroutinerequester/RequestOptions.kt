package com.sha.coroutinerequester

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

data class RequestOptions(
        var inlineHandling: ((Throwable) -> Boolean)? = null,
        var showLoading: Boolean = true,
        var dispatcher: CoroutineDispatcher = Dispatchers.Main
){

    class Builder {
        private val info = RequestOptions()

        fun inlineErrorHandling(callback: ((Throwable) -> Boolean)?): Builder {
            info.inlineHandling = callback
            return this
        }

        fun showLoading(show: Boolean): Builder {
            info.showLoading = show
            return this
        }

        fun build(): RequestOptions {
            return info
        }
    }

    companion object {
        fun defaultInfo(): RequestOptions {
            return Builder().build()
        }
    }

}