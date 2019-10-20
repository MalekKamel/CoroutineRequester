package com.sha.coroutinerequester

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.sha.coroutinerequester.exception.ExceptionProcessor
import com.sha.coroutinerequester.handler.OutOfMemoryErrorHandler
import com.sha.coroutinerequester.handler.ServerErrorHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalCoroutinesApi
class ExceptionProcessorTest {

    private lateinit var presentable: Presentable
    lateinit var coroutineRequester: CoroutineRequester

    @Before
    fun setup() {
        presentable = mock()
        coroutineRequester = CoroutineRequester.create(presentable)
        CoroutineRequester.throwableHandlers = listOf(OutOfMemoryErrorHandler())
        CoroutineRequester.httpHandlers = listOf(ServerErrorHandler())
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

    @Test
    fun process_httpException() {

        val body = ResponseBody.create(MediaType.parse("text/plain"), "content")
        val response: Response<Int> = Response.error(500, body)
        val httpException = HttpException(response)

        ExceptionProcessor.process(
                throwable = httpException,
                presentable = presentable,
                serverErrorContract = null,
                retryRequest = {  },
                requester = coroutineRequester
        )

        verify(presentable).showError("500 server error")

    }

    @Test
    fun process_handleErrorFailed() {

        val body = ResponseBody.create(MediaType.parse("text/plain"), "content")
        val response: Response<Int> = Response.error(402, body)
        val httpException = HttpException(response)

        ExceptionProcessor.process(
                throwable = httpException,
                presentable = presentable,
                serverErrorContract = null,
                retryRequest = {  },
                requester = coroutineRequester
        )

        verify(presentable).onHandleErrorFailed(httpException)

    }
}

