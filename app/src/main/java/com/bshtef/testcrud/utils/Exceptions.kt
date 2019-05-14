package com.bshtef.testcrud.utils

import android.content.Context
import com.bshtef.testcrud.R


open class NetworkException(error: Throwable): RuntimeException(error)

class NoNetworkException(error: Throwable): NetworkException(error)

class ServerUnreachableException(error: Throwable): NetworkException(error)

class HttpCallFailureException(error: Throwable): NetworkException(error)


fun Context.getErrorMessage(error: Throwable): String{
    return when(error){
        is NoNetworkException -> getString(R.string.error_no_network)
        is ServerUnreachableException -> getString(R.string.error_server_inreachable)
        is HttpCallFailureException -> getString(R.string.error_call_failed)
        else -> error.message ?: getString(R.string.something_went_wrong)
    }
}