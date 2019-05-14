package com.bshtef.testcrud.data.api

import com.bshtef.testcrud.data.model.Truck
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {

    @GET("trucks")
    fun getTrucks(): Single<List<Truck>>

    @POST("truck/add")
    fun addTruck(@Body truck: Truck): Single<Truck>

    @PATCH("truck/{id}")
    fun editTruck(@Path("id") id: String, @Body truck: Truck): Single<Truck>

    @DELETE("truck/{id}")
    fun deleteTruck(@Path("id") id: String): Single<String>

}