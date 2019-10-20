package com.sha.coroutinerequester

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.sha.coroutinerequester.exception.ExceptionProcessor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ExceptionProcessorTest {

    private lateinit var presentable: Presentable
    lateinit var coroutineRequester: CoroutineRequester

    @Before
    fun setup() {
        presentable = mock()
        coroutineRequester = CoroutineRequester.create(presentable)
        CoroutineRequester.throwableHandlers = listOf(OutOfMemoryErrorHandler())
    }

    @Test
    fun process_outOfMemory() {
        var isRetryInvoked = false
        ExceptionProcessor.process(
                throwable = OutOfMemoryError(),
                presentable = presentable,
                serverErrorContract = null,
                retryRequest = { isRetryInvoked = true },
                requester = coroutineRequester
        )

        verify(presentable).showError("OutOfMemoryError")

        assert(isRetryInvoked)

    }

}

