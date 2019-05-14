package com.bshtef.testcrud.utils

import io.reactivex.Single
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <T, R> Single<out Iterable<T>>.mapList(
    noinline mapper: (T) -> R
): Single<List<R>> = map { it.map(mapper::invoke) }

fun <T> Single<T>.mapNetworkErrors(): Single<T> =
    this.onErrorResumeNext { error ->
        when (error) {
            is Exception -> Single.error(NoNetworkException(error))
            is UnknownHostException -> Single.error(ServerUnreachableException(error))
            is HttpException -> Single.error(HttpCallFailureException(error))
            else -> Single.error(error)
        }
    }
