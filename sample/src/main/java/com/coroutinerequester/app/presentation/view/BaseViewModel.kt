package com.coroutinerequester.app.presentation.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.coroutinerequester.app.R
import com.coroutinerequester.app.data.DataManager
import com.coroutinerequester.app.presentation.coroutinerequester.*
import com.sha.coroutinerequester.Presentable
import com.sha.coroutinerequester.CoroutineRequester

open class BaseViewModel(val dm: DataManager)
    : ViewModel() {

    val requester: CoroutineRequester by lazy {
        val presentable = object: Presentable {
            override fun showError(error: String) { showError.value = error }
            override fun showError(error: Int) { showErrorRes.value = error }
            override fun showLoading() { toggleLoading.value = true }
            override fun hideLoading() { toggleLoading.value = false }
            override fun onHandleErrorFailed(throwable: Throwable) { showErrorRes.value = R.string.oops_something_went_wrong }
        }

        val requester = CoroutineRequester.create(ErrorContract::class.java, presentable)

        if (CoroutineRequester.throwableHandlers.isEmpty())
            CoroutineRequester.throwableHandlers = listOf(
                    IoExceptionHandler(),
                    NoSuchElementHandler(),
                    OutOfMemoryErrorHandler()
            )
        if (CoroutineRequester.httpHandlers.isEmpty())
            CoroutineRequester.httpHandlers = listOf(
                    TokenExpiredHandler(),
                    ServerErrorHandler()
            )
        requester
    }

    val toggleLoading = MutableLiveData<Boolean>()
    val showError = MutableLiveData<String>()
    val showErrorRes = MutableLiveData<Int>()
}

