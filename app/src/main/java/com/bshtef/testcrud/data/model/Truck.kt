package com.bshtef.testcrud.data.model

import com.google.gson.annotations.SerializedName

data class Truck(
    @SerializedName("id") val id: String? = null,
    @SerializedName("nameTruck") val nameTruck: String? = null,
    @SerializedName("price") val price: String? = null,
    @SerializedName("comment") val comment: String? = null
)