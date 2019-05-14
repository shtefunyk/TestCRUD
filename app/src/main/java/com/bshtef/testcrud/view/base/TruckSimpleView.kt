package com.bshtef.testcrud.view.base

import java.io.Serializable

data class TruckSimpleView(
    val id: String,
    val name: String,
    val price: String,
    val comment: String
): Serializable