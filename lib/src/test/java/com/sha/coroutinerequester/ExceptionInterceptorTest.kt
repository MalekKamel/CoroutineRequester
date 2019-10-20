package com.sha.coroutinerequester

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.sha.coroutinerequester.exception.InterceptorArgs
import com.sha.coroutinerequester.exception.ExceptionInterceptor
import com.sha.coroutinerequester.handler.OutOfMemoryErrorHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ExceptionInterceptorTest {
    private lateinit var presentable: Presentable
    lateinit var coroutineRequester: CoroutineRequester

    @Before
    fun setup() {
        presentable = mock()
        coroutineRequester = CoroutineRequester.create(presentable)
        CoroutineRequester.throwableHandlers = listOf(OutOfMemoryErrorHandler())
    }

    @Test
    fun accept_outOfMemoryError() = runBlockingTest {
        val args = InterceptorArgs(
                requester = coroutineRequester,
                presentable = presentable,
                serverErrorContract = null,
                inlineHandling = { false },
                retryRequest = {  }
        )
        ExceptionInterceptor(args).accept(OutOfMemoryError())
        verify(presentable).showError("OutOfMemoryError")
    }

    @Test
    fun accept_inlineHandlingIsNull() = runBlockingTest {
        val args = InterceptorArgs(
                requester = coroutineRequester,
                presentable = presentable,
                serverErrorContract = null,
                inlineHandling = null,
                retryRequest = {  }
        )
        ExceptionInterceptor(args).accept(OutOfMemoryError())

        verify(presentable).showError("OutOfMemoryError")
    }

    @Test
    fun accept_invokeRetryRequest() = runBlockingTest {
        var isRetryInvoked = false
        val args = InterceptorArgs(
                requester = coroutineRequester,
                presentable = presentable,
                serverErrorContract = null,
                inlineHandling = null,
                retryRequest = { isRetryInvoked = true }
        )
        ExceptionInterceptor(args).accept(OutOfMemoryError())

        verify(presentable).showError("OutOfMemoryError")
        assert(isRetryInvoked)
    }

    @Test
    fun accept_inlineHandlingReturnTrue() = runBlockingTest {
        var isInlineHandlingInvoked = false
        val args = InterceptorArgs(
                requester = coroutineRequester,
                presentable = presentable,
                serverErrorContract = null,
                inlineHandling = {
                    isInlineHandlingInvoked = true
                    true
                },
                retryRequest = {  }
        )
        ExceptionInterceptor(args).accept(OutOfMemoryError())

        assert(isInlineHandlingInvoked)
    }


}