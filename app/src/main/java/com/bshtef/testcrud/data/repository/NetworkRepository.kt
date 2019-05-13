package com.bshtef.testcrud.data.repository

import com.bshtef.testcrud.data.api.ApiService
import com.bshtef.testcrud.data.model.Truck
import io.reactivex.Single

class NetworkRepository(private val apiService: ApiService) {

    fun getList(): Single<List<Truck>> = apiService.getTrucks()

    fun deleteTruck(id: String): Single<String> = apiService.deleteTruck(id)

    fun createTruck(truck: Truck): Single<Truck> = apiService.addTruck(truck)

}