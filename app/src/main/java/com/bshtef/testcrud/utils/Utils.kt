package com.bshtef.testcrud.utils

import android.content.Context
import android.net.ConnectivityManager

fun validatePrice(price: String): String{
    try {
        price.toDouble()
    }catch (e: Exception){
        return "undefined price"
    }
    return price
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}