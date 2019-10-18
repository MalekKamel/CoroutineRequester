package com.sha.coroutinerequester.exception

import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.sha.coroutinerequester.Presentable
import com.sha.coroutinerequester.CoroutineRequester
import com.sha.coroutinerequester.exception.handler.http.HttpExceptionInfo
import com.sha.coroutinerequester.exception.handler.throwable.ThrowableInfo
import retrofit2.HttpException

/**
 * Created by Sha on 10/9/17.
 */

internal object ExceptionProcessor {

    fun process(
            throwable: Throwable,
            presentable: Presentable,
            serverErrorContract: Class<*>?,
            retryRequest: suspend () -> Unit,
            requester: CoroutineRequester
            ) {
        try {

            if (throwable is HttpException) {
                handleHttpException(throwable, retryRequest, serverErrorContract, presentable, requester)
                return
            }

            handleThrowable(throwable, retryRequest, presentable, requester)

        } catch (e: Exception) {
            e.printStackTrace()
            // Retrofit throws an exception
            uncaughtException(presentable, throwable)
        }
    }

    private fun handleHttpException(
            throwable: Throwable,
            retryRequest: suspend () -> Unit,
            serverErrorContract: Class<*>?,
            presentable: Presentable,
            requester: CoroutineRequester
    ) {
        val httpException = throwable as HttpException

        val body = httpException.error()
        val code = httpException.errorCode()

        if (code == null) {
            uncaughtException(presentable, throwable)
            return
        }

        val optHandler = CoroutineRequester.httpHandlers.firstOrNull { it.canHandle(code) }

        if (optHandler == null) {
            if (serverErrorContract != null)
                showOriginalHttpMessage(body, presentable, throwable, serverErrorContract)
            else
                uncaughtException(presentable, throwable)
            return
        }

        val info = HttpExceptionInfo(
                throwable = throwable,
                presentable = presentable,
                retryRequest = retryRequest,
                requester = requester,
                errorBody = body,
                code = code
        )

        optHandler.handle(info)

    }

    private fun showOriginalHttpMessage(
            body: String,
            presentable: Presentable,
            throwable: Throwable,
            serverErrorContract: Class<*>
    ) {
        val contract = parseErrorContract(body, serverErrorContract)

        if (TextUtils.isEmpty(contract.errorMessage())) {
            uncaughtException(presentable, throwable)
            return
        }

        presentable.showError(contract.errorMessage())
    }

    private fun handleThrowable(
            throwable: Throwable,
            retryRequest: suspend () -> Unit,
            presentable: Presentable,
            requester: CoroutineRequester
    ) {
        val optHandler = CoroutineRequester.throwableHandlers.firstOrNull { it.canHandle(throwable) }

        if (optHandler == null) {
            uncaughtException(presentable, throwable)
            return
        }

        val info = ThrowableInfo(
                throwable = throwable,
                presentable = presentable,
                retryRequest = retryRequest,
                requester = requester
                )

        optHandler.handle(info)
    }

    private fun uncaughtException(presentable: Presentable, throwable: Throwable) {
        presentable.onHandleErrorFailed(throwable)
    }

    private fun parseErrorContract(body: String, serverErrorContract: Class<*>): ErrorMessage {
        return GsonBuilder().create().fromJson(body, serverErrorContract) as ErrorMessage
    }

}
