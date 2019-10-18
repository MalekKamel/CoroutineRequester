package com.sha.coroutinerequester.exception


import com.sha.coroutinerequester.Presentable
import com.sha.coroutinerequester.CoroutineRequester

data class InterceptorArgs(
        val requester: CoroutineRequester,
        val presentable: Presentable,
        val serverErrorContract: Class<*>?,
        var inlineHandling: ((Throwable) -> Boolean)?,
        var retryRequest: suspend () -> Unit
)

class RxExceptionInterceptor(private val args: InterceptorArgs) {

    fun accept(throwable: Throwable) {
        throwable.printStackTrace()

        // inline handling of the error
        if (args.inlineHandling != null && args.inlineHandling!!(throwable))
            return

        ExceptionProcessor.process(
                throwable = throwable,
                presentable = args.presentable,
                serverErrorContract = args.serverErrorContract,
                retryRequest =  args.retryRequest,
                requester = args.requester
        )
    }


}