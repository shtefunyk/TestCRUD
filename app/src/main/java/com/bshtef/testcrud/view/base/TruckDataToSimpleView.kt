package com.bshtef.testcrud.view.base

import com.bshtef.testcrud.data.model.Truck
import com.bshtef.testcrud.utils.Mapper

class TruckDataToSimpleView : Mapper<Truck, TruckSimpleView?> {

    override fun transform(from: Truck) = with(from)  {

        if (id == null || nameTruck == null || price == null || comment == null){
            null
        }else{
            TruckSimpleView(
                id = id,
                name = nameTruck,
                price = 0.0,
                comment = comment
            )
        }
    }
}