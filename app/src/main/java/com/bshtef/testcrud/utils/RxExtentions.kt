package com.bshtef.testcrud.utils

import io.reactivex.Single

inline fun <T, R> Single<out Iterable<T>>.mapList(
    noinline mapper: (T) -> R
): Single<List<R>> = map { it.map(mapper::invoke) }