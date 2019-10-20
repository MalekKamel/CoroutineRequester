package com.sha.coroutinerequester

import com.sha.coroutinerequester.exception.ErrorMessage
import com.sha.coroutinerequester.exception.InterceptorArgs
import com.sha.coroutinerequester.exception.ExceptionInterceptor
import com.sha.coroutinerequester.exception.handler.http.HttpExceptionHandler
import com.sha.coroutinerequester.exception.handler.throwable.ThrowableHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CoroutineRequester private constructor(
        private val serverErrorContract: Class<*>?,
        private val presentable: Presentable
){

    companion object {
        @JvmStatic
        var httpHandlers = listOf<HttpExceptionHandler>()
        @JvmStatic
        var throwableHandlers = listOf<ThrowableHandler<*>>()

        /**
         * create requester instance
         */
        fun <T: ErrorMessage> create(
                serverErrorContract: Class<T>? = null,
                presentable: Presentable): CoroutineRequester {
            return CoroutineRequester(serverErrorContract, presentable)
        }

        /**
         * utility to support Java overloading
         */
        fun create(presentable: Presentable): CoroutineRequester {
            return create<ErrorMessage>(null, presentable)
        }
    }

    /**
     * Helper function to call a data load function with a loading spinner, errors will trigger a
     * snackbar.
     *
     * By marking `block` as `suspend` this creates a suspend lambda which can call suspend
     * functions.
     *
     * @param block lambda to actually load data. It is called in the viewModelScope. Before calling the
     *              lambda the loading spinner will display, after completion or error the loading
     *              spinner will stop
     */
    suspend fun request(
            requestInfo: RequestOptions = RequestOptions.defaultInfo(),
            block: suspend () -> Unit
    ) {
        try {
            toggleLoading(show = true)
            block()
        } catch (error: Exception) {
            val args = InterceptorArgs(
                    requester = this@CoroutineRequester,
                    presentable = presentable,
                    serverErrorContract = serverErrorContract,
                    inlineHandling = requestInfo.inlineHandling,
                    retryRequest = { request(requestInfo, block) }
            )
            ExceptionInterceptor(args).accept(error)
            toggleLoading(show = false)
        } finally {
            toggleLoading(show = false)
        }
    }

    private suspend fun toggleLoading(show: Boolean) {
        withContext(Dispatchers.Main) {
            if (show) {
                presentable.showLoading()
                return@withContext
            }
            presentable.hideLoading()
        }
    }

}