package com.sha.coroutinerequester

data class RequestOptions(
        var inlineHandling: ((Throwable) -> Boolean)? = null,
        var showLoading: Boolean = true
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
        fun default(): RequestOptions {
            return Builder().build()
        }

        fun create(block: RequestOptions.() -> Unit): RequestOptions {
            return Builder().build().apply(block)
        }
    }

}