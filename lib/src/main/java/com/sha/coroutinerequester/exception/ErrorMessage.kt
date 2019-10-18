package com.sha.coroutinerequester.exception

/**
 * interface to be implemented in server error contract model
 */
interface ErrorMessage {
    fun errorMessage(): String
}