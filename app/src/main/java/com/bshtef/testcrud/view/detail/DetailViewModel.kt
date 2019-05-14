package com.bshtef.testcrud.view.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bshtef.testcrud.data.api.ApiClient
import com.bshtef.testcrud.data.model.Truck
import com.bshtef.testcrud.data.repository.NetworkRepository
import com.bshtef.testcrud.utils.mapNetworkErrors
import com.bshtef.testcrud.view.base.TruckDataToSimpleView
import com.bshtef.testcrud.view.base.TruckSimpleView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Error

class DetailViewModel: ViewModel()  {

    private val networkRepository = NetworkRepository(ApiClient.getClient)
    private val compositeDisposable = CompositeDisposable()

    var truck = MutableLiveData<TruckSimpleView>()
    var error = MutableLiveData<Throwable>()

    fun createTruck(name: String, price: String, comment: String){
        compositeDisposable.add(
            networkRepository.createTruck(Truck("", name, price, comment))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .mapNetworkErrors()
                .map(TruckDataToSimpleView()::transform)
                .subscribe(
                    { updatedTruck ->
                        truck.postValue(updatedTruck)
                    },
                    { throwable ->
                        error.postValue(throwable)
                    }
                )
        )

    }

    fun editTruck(id: String, name: String, price: String, comment: String){
        compositeDisposable.add(
            networkRepository.editTruck(id, Truck("", name, price, comment))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .mapNetworkErrors()
                .map(TruckDataToSimpleView()::transform)
                .subscribe(
                    { updatedTruck ->
                        truck.postValue(updatedTruck)

                    },
                    { throwable ->
                        error.postValue(throwable)
                    }
                )
        )
    }

    fun clear(){
        compositeDisposable.dispose()
    }
}