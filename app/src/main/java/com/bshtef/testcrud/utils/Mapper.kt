package com.bshtef.testcrud.utils

interface Mapper<T, R> {
    fun transform(from: T): R
}