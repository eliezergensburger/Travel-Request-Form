package com.example.travelrequestform.data.repositories

import androidx.lifecycle.LiveData
import com.example.travelrequestform.data.models.Travel

class TravelRepository {

    private val travelDataSource = TravelDataSource.instance as TravelDataSource

    fun addTravel(travel: Travel){
        travelDataSource.addTravel(travel)
    }

    fun getIsSuccess() : LiveData<Boolean> {
        return travelDataSource.isSuccess
    }
}