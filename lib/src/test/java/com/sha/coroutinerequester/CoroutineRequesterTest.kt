package com.sha.coroutinerequester

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.sha.coroutinerequester.util.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CoroutineRequesterTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var presentable: Presentable
    lateinit var coroutineRequester: CoroutineRequester

    @Before
    fun setup() {
        presentable = mock()
        coroutineRequester = CoroutineRequester.create(presentable)
        CoroutineRequester.throwableHandlers = listOf(OutOfMemoryErrorHandler())
    }

    @Test
    fun request_loadingTogglesAndInvokeBlock() = runBlockingTest {

        var isBlockInvoked = false

        val block: suspend () -> Unit =  { isBlockInvoked = true }

        coroutineRequester.request(block = block)

        assert(isBlockInvoked)

        verify(presentable).showLoading()

        verify(presentable).hideLoading()
    }

}