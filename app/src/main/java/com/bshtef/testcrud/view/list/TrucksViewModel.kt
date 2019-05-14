package com.bshtef.testcrud.view.list

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bshtef.testcrud.data.api.ApiClient
import com.bshtef.testcrud.data.repository.NetworkRepository
import com.bshtef.testcrud.utils.NoNetworkException
import com.bshtef.testcrud.utils.isNetworkAvailable
import com.bshtef.testcrud.utils.mapList
import com.bshtef.testcrud.utils.mapNetworkErrors
import com.bshtef.testcrud.view.base.TruckDataToSimpleView
import com.bshtef.testcrud.view.base.TruckSimpleView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TrucksViewModel : ViewModel() {

    private val networkRepository = NetworkRepository(ApiClient.getClient)
    private val compositeDisposable = CompositeDisposable()

    var trucks = MutableLiveData<ArrayList<TruckSimpleView>>()
    var error = MutableLiveData<Throwable>()
    var action = MutableLiveData<Action>()

    fun getList(context: Context) {
        if (!context.isNetworkAvailable()){
            error.postValue(NoNetworkException(Throwable()))
            return
        }

        compositeDisposable.add(
            networkRepository.getList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .mapNetworkErrors()
                .mapList(TruckDataToSimpleView()::transform)
                .subscribe(
                    { list ->
                        trucks.postValue(
                            ArrayList(list
                                .filterNotNull()
                                .sortedByDescending { it.id.toInt() }))
                    },
                    { throwable ->
                        error.postValue(throwable)
                    }
                )
        )
    }

    fun removeTruck(context: Context, truck: TruckSimpleView){
        if (!context.isNetworkAvailable()){
            error.postValue(NoNetworkException(Throwable()))
            return
        }

        compositeDisposable.add(
            networkRepository.deleteTruck(truck.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .mapNetworkErrors()
                .subscribe(
                    {
                        trucks.value?.remove(truck)
                        trucks.value = trucks.value
                    },
                    { throwable ->
                        error.postValue(throwable)
                    }
                )
        )
    }

    fun addTruckToList(newTruck: TruckSimpleView){
        trucks.value?.add(0, newTruck)
        trucks.value = trucks.value
        action.postValue(Action.SHOW_LIST_AFTER_ADD)
    }

    fun clear(){
        compositeDisposable.dispose()
    }

}