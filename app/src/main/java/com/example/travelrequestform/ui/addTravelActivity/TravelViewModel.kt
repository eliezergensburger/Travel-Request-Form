package com.example.travelrequestform.ui.addTravelActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travelrequestform.data.models.Travel
import com.example.travelrequestform.data.repositories.TravelRepository

class TravelViewModel : ViewModel() {

    private val travelRepo = TravelRepository()

    fun addTravel(travel: Travel) {
        travelRepo.addTravel(travel)
    }


    fun getIsSuccess(): LiveData<Boolean> {
        return travelRepo.getIsSuccess()
    }
}