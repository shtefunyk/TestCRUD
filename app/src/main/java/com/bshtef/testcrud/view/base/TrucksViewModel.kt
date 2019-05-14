package com.bshtef.testcrud.view.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bshtef.testcrud.data.api.ApiClient
import com.bshtef.testcrud.data.model.Truck
import com.bshtef.testcrud.data.repository.NetworkRepository
import com.bshtef.testcrud.utils.mapList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TrucksViewModel : ViewModel() {

    private val networkRepository = NetworkRepository(ApiClient.getClient)
    private val compositeDisposable = CompositeDisposable()

    var trucks = MutableLiveData<ArrayList<TruckSimpleView>>()
    var message = MutableLiveData<String>()

    fun getList() {
        compositeDisposable.add(
            networkRepository.getList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .mapList(TruckDataToSimpleView()::transform)
                .subscribe(
                    { list ->
                        trucks.postValue(ArrayList(list.filterNotNull().sortedByDescending { it.id.toInt() }))
                    },
                    { throwable ->
                        message.postValue(throwable.message)
                    }
                )
        )
    }

    fun removeTruck(truck: TruckSimpleView){
        compositeDisposable.add(
            networkRepository.deleteTruck(truck.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        trucks.value?.remove(truck)
                        trucks.value = trucks.value
                    },
                    { throwable ->
                        message.postValue(throwable.message)
                    }
                )
        )
    }

    fun addTruckToList(newTruck: TruckSimpleView){
        trucks.value?.add(0, newTruck)
        trucks.value = trucks.value
    }

    fun clear(){
        compositeDisposable.dispose()
    }

}