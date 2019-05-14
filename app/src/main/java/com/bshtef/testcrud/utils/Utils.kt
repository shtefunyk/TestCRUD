package com.bshtef.testcrud.utils

import java.lang.Exception

fun validatePrice(price: String): String{
    try {
        price.toDouble()
    }catch (e: Exception){
        return "undefined price"
    }
    return price
}